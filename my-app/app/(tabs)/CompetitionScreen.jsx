import React, { useCallback, useEffect, useMemo, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  Image,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { useRouter } from "expo-router";

import { acceptCompetitionInvite } from "../../api/competition/acceptCompetitionInvite";
import { getApiErrorMessage } from "../../api/client";
import { createCompetition } from "../../api/competition/createCompetition";
import { declineCompetitionInvite } from "../../api/competition/declineCompetitionInvite";
import { getCompetitionLeaderboard } from "../../api/competition/getCompetitionLeaderboard";
import { getFriendsLeaderboard } from "../../api/competition/getFriendsLeaderboard";
import { getGlobalLeaderboard } from "../../api/competition/getGlobalLeaderboard";
import { getPersonalCompetitions } from "../../api/competition/getPersonalCompetitions";
import { getAllExercises } from "../../api/exercises/getAllExercises";
import { getFriends } from "../../api/friends/getFriends";
import { get_user_profile } from "../../api/user/get_user_profile";
import { useUserId } from "../../hooks/useUserId";

const TABS = [
  { key: "global", label: "Общий прогресс" },
  { key: "friends", label: "Среди друзей" },
  { key: "personal", label: "Персональные" },
];

const PERIODS = [1, 3, 6];

const GOAL_TYPES = [
  { key: "PROGRESS_SCORE", label: "По прогрессу" },
  { key: "EXERCISE_REPS", label: "Повторения упражнения" },
  { key: "WORKOUT_COUNT", label: "Количество тренировок" },
];

const getInitials = (name) => {
  if (!name) {
    return "U";
  }

  return name
    .split(" ")
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase() ?? "")
    .join("");
};

const isCreateFormValid = (form) => {
  if (!form.title.trim() || !form.targetValue.trim()) {
    return false;
  }

  const targetValue = Number(form.targetValue);
  if (!Number.isFinite(targetValue) || targetValue <= 0) {
    return false;
  }

  if (form.goalType === "EXERCISE_REPS" && !form.exerciseId) {
    return false;
  }

  return true;
};

const buildFriendOption = async (item, currentUserId) => {
  const relatedUserId = item.senderId === currentUserId ? item.receiverId : item.senderId;

  try {
    const profile = await get_user_profile(relatedUserId);
    const fullName = `${profile?.firstName ?? ""} ${profile?.lastName ?? ""}`.trim();
    return {
      id: relatedUserId,
      name: fullName || `Пользователь #${relatedUserId}`,
      avatarUrl: profile?.avatarUrl ?? null,
    };
  } catch (error) {
    return {
      id: relatedUserId,
      name: `Пользователь #${relatedUserId}`,
      avatarUrl: null,
    };
  }
};

const formatMonthlyCountdown = (endsAt, now) => {
  if (!endsAt) {
    return "";
  }

  const endDate = new Date(endsAt);
  if (Number.isNaN(endDate.getTime())) {
    return "";
  }

  const diffMs = endDate.getTime() - now.getTime();
  if (diffMs <= 0) {
    return "Обновление рейтинга выполняется сейчас.";
  }

  const totalMinutes = Math.floor(diffMs / (1000 * 60));
  const days = Math.floor(totalMinutes / (60 * 24));
  const hours = Math.floor((totalMinutes % (60 * 24)) / 60);
  const minutes = totalMinutes % 60;

  if (days > 0) {
    return `До конца месяца: ${days} дн. ${hours} ч. ${minutes} мин.`;
  }

  if (hours > 0) {
    return `До конца месяца: ${hours} ч. ${minutes} мин.`;
  }

  return `До конца месяца: ${Math.max(1, minutes)} мин.`;
};

const LeaderboardRow = ({ item, onPress }) => (
  <TouchableOpacity
    style={[styles.leaderboardRow, item.currentUser && styles.currentUserRow]}
    onPress={() => onPress(item.userId)}
    activeOpacity={0.82}
  >
    <Text style={[styles.rankText, item.currentUser && styles.currentUserText]}>{item.rank}</Text>

    {item.avatarUrl ? (
      <Image source={{ uri: item.avatarUrl }} style={styles.rowAvatar} />
    ) : (
      <View style={styles.rowAvatarFallback}>
        <Text style={styles.rowAvatarFallbackText}>{getInitials(item.userName)}</Text>
      </View>
    )}

    <View style={styles.rowMain}>
      <Text style={[styles.rowName, item.currentUser && styles.currentUserText]} numberOfLines={1}>
        {item.userName}
      </Text>
      <Text style={[styles.rowSubtitle, item.currentUser && styles.currentUserSubtitle]}>
        {item.subtitle}
      </Text>
    </View>

    <View style={styles.rowValueWrap}>
      <Text style={[styles.rowValue, item.currentUser && styles.currentUserText]}>
        {Math.round(item.currentValue * 10) / 10}
      </Text>
      {item.targetValue ? (
        <Text style={[styles.rowTarget, item.currentUser && styles.currentUserSubtitle]}>
          из {Math.round(item.targetValue * 10) / 10}
        </Text>
      ) : null}
    </View>
  </TouchableOpacity>
);

export default function CompetitionScreen() {
  const router = useRouter();
  const [userId, , isLoaded] = useUserId();
  const [activeTab, setActiveTab] = useState("global");
  const [now, setNow] = useState(() => new Date());
  const [globalLeaderboard, setGlobalLeaderboard] = useState(null);
  const [friendsLeaderboard, setFriendsLeaderboard] = useState(null);
  const [personalCompetitions, setPersonalCompetitions] = useState([]);
  const [selectedCompetitionId, setSelectedCompetitionId] = useState(null);
  const [selectedCompetitionLeaderboard, setSelectedCompetitionLeaderboard] = useState(null);
  const [friendsOptions, setFriendsOptions] = useState([]);
  const [exercises, setExercises] = useState([]);
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [form, setForm] = useState({
    title: "",
    description: "",
    goalType: "PROGRESS_SCORE",
    targetValue: "",
    exerciseId: null,
    exerciseName: "",
    periodMonths: 1,
    invitedUserIds: [],
  });

  const pendingInvites = useMemo(
    () => personalCompetitions.filter((item) => item.currentUserStatus === "PENDING"),
    [personalCompetitions]
  );
  const acceptedCompetitions = useMemo(
    () => personalCompetitions.filter((item) => item.currentUserStatus === "ACCEPTED"),
    [personalCompetitions]
  );

  const leaderboardData =
    activeTab === "global"
      ? globalLeaderboard
      : activeTab === "friends"
      ? friendsLeaderboard
      : selectedCompetitionLeaderboard;

  const countdownLabel = useMemo(
    () =>
      activeTab === "personal"
        ? ""
        : formatMonthlyCountdown(leaderboardData?.endsAt, now),
    [activeTab, leaderboardData?.endsAt, now]
  );

  const openUserProfile = (targetUserId) => {
    if (!targetUserId) {
      return;
    }
    router.push(`/(tabs)/(profile)/${targetUserId}`);
  };

  const loadGlobal = useCallback(async () => {
    if (!isLoaded || userId == null) {
      return;
    }

    setIsLoading(true);
    try {
      const data = await getGlobalLeaderboard(userId, 1);
      setGlobalLeaderboard(data);
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось загрузить общий рейтинг соревнования.")
      );
    } finally {
      setIsLoading(false);
    }
  }, [isLoaded, userId]);

  const loadFriends = useCallback(async () => {
    if (!isLoaded || userId == null) {
      return;
    }

    setIsLoading(true);
    try {
      const data = await getFriendsLeaderboard(userId, 1);
      setFriendsLeaderboard(data);
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось загрузить рейтинг друзей.")
      );
    } finally {
      setIsLoading(false);
    }
  }, [isLoaded, userId]);

  const loadPersonal = useCallback(async () => {
    if (!isLoaded || userId == null) {
      return;
    }

    setIsLoading(true);
    try {
      const [competitionItems, friendRequests, exerciseItems] = await Promise.all([
        getPersonalCompetitions(userId),
        getFriends(userId),
        getAllExercises(),
      ]);

      setPersonalCompetitions(competitionItems);
      setExercises(exerciseItems);

      const friendProfiles = await Promise.all(
        (friendRequests ?? []).map((item) => buildFriendOption(item, userId))
      );
      setFriendsOptions(friendProfiles);
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось загрузить персональные соревнования.")
      );
    } finally {
      setIsLoading(false);
    }
  }, [isLoaded, userId]);

  const loadSelectedCompetitionLeaderboard = useCallback(
    async (competitionId) => {
      if (!isLoaded || userId == null || !competitionId) {
        return;
      }

      try {
        const data = await getCompetitionLeaderboard(competitionId, userId);
        setSelectedCompetitionLeaderboard(data);
      } catch (error) {
        Alert.alert(
          "Ошибка",
          getApiErrorMessage(error, "Не удалось загрузить рейтинг выбранного соревнования.")
        );
      }
    },
    [isLoaded, userId]
  );

  useEffect(() => {
    const intervalId = setInterval(() => {
      setNow(new Date());
    }, 60000);

    return () => clearInterval(intervalId);
  }, []);

  useEffect(() => {
    if (activeTab === "global") {
      loadGlobal();
    } else if (activeTab === "friends") {
      loadFriends();
    } else {
      loadPersonal();
    }
  }, [activeTab, loadFriends, loadGlobal, loadPersonal]);

  useEffect(() => {
    if (activeTab === "personal" && selectedCompetitionId) {
      loadSelectedCompetitionLeaderboard(selectedCompetitionId);
    }
  }, [activeTab, loadSelectedCompetitionLeaderboard, selectedCompetitionId]);

  const toggleFriendInvite = (targetUserId) => {
    setForm((prev) => {
      const exists = prev.invitedUserIds.includes(targetUserId);
      return {
        ...prev,
        invitedUserIds: exists
          ? prev.invitedUserIds.filter((id) => id !== targetUserId)
          : [...prev.invitedUserIds, targetUserId],
      };
    });
  };

  const handleChangeGoalType = (goalType) => {
    setForm((prev) => ({
      ...prev,
      goalType,
      exerciseId: goalType === "EXERCISE_REPS" ? prev.exerciseId : null,
      exerciseName: goalType === "EXERCISE_REPS" ? prev.exerciseName : "",
    }));
  };

  const handleExerciseSelect = (exercise) => {
    setForm((prev) => ({
      ...prev,
      exerciseId: exercise.id,
      exerciseName: exercise.name,
    }));
  };

  const resetForm = () => {
    setForm({
      title: "",
      description: "",
      goalType: "PROGRESS_SCORE",
      targetValue: "",
      exerciseId: null,
      exerciseName: "",
      periodMonths: 1,
      invitedUserIds: [],
    });
  };

  const handleCreateCompetition = async () => {
    if (userId == null || !isCreateFormValid(form) || isSubmitting) {
      return;
    }

    setIsSubmitting(true);
    try {
      await createCompetition({
        creatorId: userId,
        title: form.title.trim(),
        description: form.description.trim(),
        goalType: form.goalType,
        targetValue: Number(form.targetValue),
        exerciseId: form.goalType === "EXERCISE_REPS" ? form.exerciseId : null,
        exerciseName: form.goalType === "EXERCISE_REPS" ? form.exerciseName : null,
        periodMonths: form.periodMonths,
        invitedUserIds: form.invitedUserIds,
      });

      Alert.alert("Успех", "Соревнование создано.");
      setShowCreateForm(false);
      resetForm();
      await loadPersonal();
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось создать персональное соревнование.")
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleRespondToInvite = async (competitionId, action) => {
    if (userId == null) {
      return;
    }

    try {
      if (action === "accept") {
        await acceptCompetitionInvite(competitionId, userId);
      } else {
        await declineCompetitionInvite(competitionId, userId);
      }

      await loadPersonal();
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось обработать приглашение в соревнование.")
      );
    }
  };

  const handleToggleCompetition = async (competitionId) => {
    if (selectedCompetitionId === competitionId) {
      setSelectedCompetitionId(null);
      setSelectedCompetitionLeaderboard(null);
      return;
    }

    setSelectedCompetitionId(competitionId);
    await loadSelectedCompetitionLeaderboard(competitionId);
  };

  return (
    <SafeAreaView style={styles.container} edges={["top"]}>
      <View style={styles.header}>
        <Text style={styles.title}>Соревнования</Text>
        <Text style={styles.subtitle}>
          Общий рейтинг, рейтинг среди друзей и персональные соревнования с приглашениями.
        </Text>
      </View>

      <View style={styles.tabsRow}>
        {TABS.map((tab) => (
          <TouchableOpacity
            key={tab.key}
            style={[styles.tabButton, activeTab === tab.key && styles.tabButtonActive]}
            onPress={() => setActiveTab(tab.key)}
          >
            <Text
              style={[
                styles.tabButtonText,
                activeTab === tab.key && styles.tabButtonTextActive,
              ]}
            >
              {tab.label}
            </Text>
          </TouchableOpacity>
        ))}
      </View>

      {isLoading ? (
        <View style={styles.loaderWrap}>
          <ActivityIndicator size="large" color="#2563EB" />
        </View>
      ) : (
        <ScrollView contentContainerStyle={styles.content}>
          {activeTab !== "personal" ? (
            <>
              <View style={styles.sectionCard}>
                <Text style={styles.sectionTitle}>{leaderboardData?.title ?? "Рейтинг"}</Text>
                <Text style={styles.sectionSubtitle}>
                  {leaderboardData?.description ?? "Загрузка рейтинга соревнования."}
                </Text>
                <Text style={styles.rankBadge}>
                  Ваше место: {leaderboardData?.currentUserRank ?? "не определено"}
                </Text>
                {countdownLabel ? (
                  <View style={styles.timerCard}>
                    <Text style={styles.timerTitle}>До ежемесячного сброса</Text>
                    <Text style={styles.timerText}>{countdownLabel}</Text>
                  </View>
                ) : null}
              </View>

              <View style={styles.listCard}>
                {(leaderboardData?.entries ?? []).map((item) => (
                  <LeaderboardRow key={`${activeTab}-${item.userId}`} item={item} onPress={openUserProfile} />
                ))}
                {!leaderboardData?.entries?.length ? (
                  <Text style={styles.emptyText}>Пока нет данных для отображения рейтинга.</Text>
                ) : null}
              </View>
            </>
          ) : (
            <>
              <TouchableOpacity
                style={styles.createToggleButton}
                onPress={() => setShowCreateForm((prev) => !prev)}
              >
                <Text style={styles.createToggleButtonText}>
                  {showCreateForm ? "Скрыть форму создания" : "Создать своё соревнование"}
                </Text>
              </TouchableOpacity>

              {showCreateForm ? (
                <View style={styles.formCard}>
                  <Text style={styles.formTitle}>Новое персональное соревнование</Text>

                  <TextInput
                    value={form.title}
                    onChangeText={(value) => setForm((prev) => ({ ...prev, title: value }))}
                    placeholder="Название соревнования"
                    placeholderTextColor="#94A3B8"
                    style={styles.input}
                  />

                  <TextInput
                    value={form.description}
                    onChangeText={(value) => setForm((prev) => ({ ...prev, description: value }))}
                    placeholder="Описание"
                    placeholderTextColor="#94A3B8"
                    style={[styles.input, styles.multilineInput]}
                    multiline
                  />

                  <Text style={styles.formSectionTitle}>Цель соревнования</Text>
                  <View style={styles.choiceWrap}>
                    {GOAL_TYPES.map((goal) => (
                      <TouchableOpacity
                        key={goal.key}
                        style={[
                          styles.choiceChip,
                          form.goalType === goal.key && styles.choiceChipActive,
                        ]}
                        onPress={() => handleChangeGoalType(goal.key)}
                      >
                        <Text
                          style={[
                            styles.choiceChipText,
                            form.goalType === goal.key && styles.choiceChipTextActive,
                          ]}
                        >
                          {goal.label}
                        </Text>
                      </TouchableOpacity>
                    ))}
                  </View>

                  <TextInput
                    value={form.targetValue}
                    onChangeText={(value) => setForm((prev) => ({ ...prev, targetValue: value }))}
                    placeholder="Целевое значение"
                    placeholderTextColor="#94A3B8"
                    keyboardType="numeric"
                    style={styles.input}
                  />

                  <Text style={styles.formSectionTitle}>Период соревнования</Text>
                  <View style={styles.periodRowInline}>
                    {PERIODS.map((period) => (
                      <TouchableOpacity
                        key={`create-${period}`}
                        style={[
                          styles.periodChip,
                          form.periodMonths === period && styles.periodChipActive,
                        ]}
                        onPress={() => setForm((prev) => ({ ...prev, periodMonths: period }))}
                      >
                        <Text
                          style={[
                            styles.periodChipText,
                            form.periodMonths === period && styles.periodChipTextActive,
                          ]}
                        >
                          {period} мес.
                        </Text>
                      </TouchableOpacity>
                    ))}
                  </View>

                  {form.goalType === "EXERCISE_REPS" ? (
                    <>
                      <Text style={styles.formSectionTitle}>Упражнение</Text>
                      <ScrollView
                        horizontal
                        showsHorizontalScrollIndicator={false}
                        contentContainerStyle={styles.horizontalOptions}
                      >
                        {exercises.map((exercise) => (
                          <TouchableOpacity
                            key={exercise.id}
                            style={[
                              styles.exerciseChoice,
                              form.exerciseId === exercise.id && styles.exerciseChoiceActive,
                            ]}
                            onPress={() => handleExerciseSelect(exercise)}
                          >
                            <Text
                              style={[
                                styles.exerciseChoiceTitle,
                                form.exerciseId === exercise.id && styles.exerciseChoiceTitleActive,
                              ]}
                            >
                              {exercise.name}
                            </Text>
                            <Text
                              style={[
                                styles.exerciseChoiceSubtitle,
                                form.exerciseId === exercise.id && styles.exerciseChoiceTitleActive,
                              ]}
                            >
                              {exercise.muscleGroup}
                            </Text>
                          </TouchableOpacity>
                        ))}
                      </ScrollView>
                    </>
                  ) : null}

                  <Text style={styles.formSectionTitle}>Пригласить друзей</Text>
                  <View style={styles.friendInviteWrap}>
                    {friendsOptions.length ? (
                      friendsOptions.map((friend) => {
                        const selected = form.invitedUserIds.includes(friend.id);
                        return (
                          <TouchableOpacity
                            key={friend.id}
                            style={[
                              styles.friendInviteRow,
                              selected && styles.friendInviteRowSelected,
                            ]}
                            onPress={() => toggleFriendInvite(friend.id)}
                          >
                            <Text
                              style={[
                                styles.friendInviteName,
                                selected && styles.friendInviteNameSelected,
                              ]}
                            >
                              {friend.name}
                            </Text>
                            <Text
                              style={[
                                styles.friendInviteCheck,
                                selected && styles.friendInviteCheckSelected,
                              ]}
                            >
                              {selected ? "Выбран" : "Пригласить"}
                            </Text>
                          </TouchableOpacity>
                        );
                      })
                    ) : (
                      <Text style={styles.emptyText}>
                        Нет друзей для приглашения в персональное соревнование.
                      </Text>
                    )}
                  </View>

                  <TouchableOpacity
                    style={[
                      styles.submitButton,
                      (!isCreateFormValid(form) || isSubmitting) && styles.submitButtonDisabled,
                    ]}
                    onPress={handleCreateCompetition}
                    disabled={!isCreateFormValid(form) || isSubmitting}
                  >
                    <Text style={styles.submitButtonText}>
                      {isSubmitting ? "Создание..." : "Создать соревнование"}
                    </Text>
                  </TouchableOpacity>
                </View>
              ) : null}

              {pendingInvites.length ? (
                <View style={styles.sectionCard}>
                  <Text style={styles.sectionTitle}>Приглашения</Text>
                  {pendingInvites.map((item) => (
                    <View key={`invite-${item.id}`} style={styles.personalCompetitionCard}>
                      <Text style={styles.personalCompetitionTitle}>{item.title}</Text>
                      <Text style={styles.personalCompetitionGoal}>{item.goalLabel}</Text>
                      <Text style={styles.personalCompetitionDescription}>{item.description}</Text>
                      <View style={styles.inviteActions}>
                        <TouchableOpacity
                          style={[styles.inlineActionButton, styles.acceptButton]}
                          onPress={() => handleRespondToInvite(item.id, "accept")}
                        >
                          <Text style={styles.inlineActionText}>Принять</Text>
                        </TouchableOpacity>
                        <TouchableOpacity
                          style={[styles.inlineActionButton, styles.declineButton]}
                          onPress={() => handleRespondToInvite(item.id, "decline")}
                        >
                          <Text style={styles.inlineActionText}>Отклонить</Text>
                        </TouchableOpacity>
                      </View>
                    </View>
                  ))}
                </View>
              ) : null}

              <View style={styles.sectionCard}>
                <Text style={styles.sectionTitle}>Персональные соревнования</Text>
                <Text style={styles.sectionSubtitle}>
                  Здесь находятся ваши соревнования и приглашения, которые вы приняли.
                </Text>
              </View>

              {acceptedCompetitions.length ? (
                acceptedCompetitions.map((item) => (
                  <TouchableOpacity
                    key={`competition-${item.id}`}
                    style={[
                      styles.personalCompetitionCard,
                      selectedCompetitionId === item.id && styles.personalCompetitionCardActive,
                    ]}
                    onPress={() => handleToggleCompetition(item.id)}
                  >
                    <Text style={styles.personalCompetitionTitle}>{item.title}</Text>
                    <Text style={styles.personalCompetitionGoal}>{item.goalLabel}</Text>
                    <Text style={styles.personalCompetitionDescription}>{item.description}</Text>
                    <View style={styles.personalCompetitionStats}>
                      <Text style={styles.personalCompetitionStat}>
                        Текущее значение: {Math.round(item.currentValue * 10) / 10}
                      </Text>
                      <Text style={styles.personalCompetitionStat}>
                        Цель: {Math.round((item.targetValue ?? 0) * 10) / 10}
                      </Text>
                      <Text style={styles.personalCompetitionStat}>
                        Участников: {item.acceptedParticipantsCount}
                      </Text>
                    </View>
                    <Text style={styles.personalCompetitionStatus}>
                      {item.targetReached
                        ? "Цель достигнута"
                        : `Прогресс: ${Math.round(item.progressPercent)}%`}
                    </Text>
                  </TouchableOpacity>
                ))
              ) : (
                <Text style={styles.emptyText}>
                  Пока нет принятых персональных соревнований.
                </Text>
              )}

              {selectedCompetitionLeaderboard ? (
                <View style={styles.listCard}>
                  <Text style={styles.sectionTitle}>{selectedCompetitionLeaderboard.title}</Text>
                  <Text style={styles.sectionSubtitle}>
                    Ваше место: {selectedCompetitionLeaderboard.currentUserRank ?? "не определено"}
                  </Text>
                  {selectedCompetitionLeaderboard.entries.map((item) => (
                    <LeaderboardRow key={`personal-${item.userId}`} item={item} onPress={openUserProfile} />
                  ))}
                </View>
              ) : null}
            </>
          )}
        </ScrollView>
      )}
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#F4F7FB",
  },
  header: {
    paddingHorizontal: 20,
    paddingTop: 8,
    marginBottom: 12,
  },
  title: {
    fontSize: 30,
    fontWeight: "700",
    color: "#0F172A",
  },
  subtitle: {
    fontSize: 14,
    lineHeight: 20,
    color: "#64748B",
    marginTop: 6,
  },
  tabsRow: {
    flexDirection: "row",
    paddingHorizontal: 16,
    gap: 8,
    marginBottom: 14,
  },
  tabButton: {
    flex: 1,
    borderRadius: 18,
    backgroundColor: "#E2E8F0",
    paddingVertical: 12,
    paddingHorizontal: 8,
    alignItems: "center",
  },
  tabButtonActive: {
    backgroundColor: "#2563EB",
  },
  tabButtonText: {
    fontSize: 13,
    fontWeight: "700",
    color: "#334155",
    textAlign: "center",
  },
  tabButtonTextActive: {
    color: "#FFFFFF",
  },
  loaderWrap: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  content: {
    paddingHorizontal: 16,
    paddingBottom: 36,
    gap: 14,
  },
  sectionCard: {
    backgroundColor: "#FFFFFF",
    borderRadius: 22,
    padding: 18,
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: "700",
    color: "#0F172A",
  },
  sectionSubtitle: {
    marginTop: 8,
    color: "#64748B",
    lineHeight: 20,
  },
  rankBadge: {
    marginTop: 12,
    alignSelf: "flex-start",
    backgroundColor: "#DBEAFE",
    color: "#1D4ED8",
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 999,
    fontWeight: "700",
  },
  timerCard: {
    marginTop: 14,
    backgroundColor: "#0F172A",
    borderRadius: 16,
    padding: 14,
  },
  timerTitle: {
    color: "#93C5FD",
    fontWeight: "700",
    marginBottom: 6,
  },
  timerText: {
    color: "#E2E8F0",
    lineHeight: 20,
  },
  listCard: {
    backgroundColor: "#FFFFFF",
    borderRadius: 22,
    padding: 12,
    gap: 8,
  },
  leaderboardRow: {
    flexDirection: "row",
    alignItems: "center",
    borderRadius: 18,
    paddingVertical: 12,
    paddingHorizontal: 10,
    backgroundColor: "#F8FAFC",
  },
  currentUserRow: {
    backgroundColor: "#DBEAFE",
  },
  rankText: {
    width: 26,
    fontSize: 17,
    fontWeight: "700",
    color: "#0F172A",
  },
  currentUserText: {
    color: "#1D4ED8",
  },
  currentUserSubtitle: {
    color: "#1E40AF",
  },
  rowAvatar: {
    width: 42,
    height: 42,
    borderRadius: 21,
    marginHorizontal: 10,
  },
  rowAvatarFallback: {
    width: 42,
    height: 42,
    borderRadius: 21,
    marginHorizontal: 10,
    backgroundColor: "#BFDBFE",
    alignItems: "center",
    justifyContent: "center",
  },
  rowAvatarFallbackText: {
    color: "#1D4ED8",
    fontWeight: "700",
  },
  rowMain: {
    flex: 1,
  },
  rowName: {
    fontSize: 15,
    fontWeight: "700",
    color: "#0F172A",
  },
  rowSubtitle: {
    marginTop: 4,
    fontSize: 12,
    color: "#64748B",
  },
  rowValueWrap: {
    alignItems: "flex-end",
  },
  rowValue: {
    fontSize: 18,
    fontWeight: "700",
    color: "#0F172A",
  },
  rowTarget: {
    marginTop: 4,
    fontSize: 12,
    color: "#64748B",
  },
  emptyText: {
    textAlign: "center",
    color: "#64748B",
    lineHeight: 22,
    paddingVertical: 12,
  },
  createToggleButton: {
    backgroundColor: "#0F172A",
    borderRadius: 18,
    paddingVertical: 16,
    alignItems: "center",
  },
  createToggleButtonText: {
    color: "#FFFFFF",
    fontSize: 15,
    fontWeight: "700",
  },
  formCard: {
    backgroundColor: "#FFFFFF",
    borderRadius: 22,
    padding: 18,
    gap: 12,
  },
  formTitle: {
    fontSize: 20,
    fontWeight: "700",
    color: "#0F172A",
  },
  input: {
    borderRadius: 16,
    borderWidth: 1,
    borderColor: "#CBD5E1",
    backgroundColor: "#F8FAFC",
    paddingHorizontal: 14,
    paddingVertical: 14,
    color: "#0F172A",
  },
  multilineInput: {
    minHeight: 92,
    textAlignVertical: "top",
  },
  formSectionTitle: {
    marginTop: 4,
    fontSize: 15,
    fontWeight: "700",
    color: "#334155",
  },
  choiceWrap: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 8,
  },
  choiceChip: {
    borderRadius: 999,
    paddingHorizontal: 12,
    paddingVertical: 10,
    backgroundColor: "#E2E8F0",
  },
  choiceChipActive: {
    backgroundColor: "#2563EB",
  },
  choiceChipText: {
    color: "#334155",
    fontWeight: "600",
  },
  choiceChipTextActive: {
    color: "#FFFFFF",
  },
  periodRowInline: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 8,
    marginBottom: 8,
  },
  periodChip: {
    backgroundColor: "#E2E8F0",
    borderRadius: 999,
    paddingHorizontal: 14,
    paddingVertical: 10,
  },
  periodChipActive: {
    backgroundColor: "#2563EB",
  },
  periodChipText: {
    color: "#334155",
    fontWeight: "600",
  },
  periodChipTextActive: {
    color: "#FFFFFF",
  },
  horizontalOptions: {
    gap: 10,
    paddingVertical: 4,
  },
  exerciseChoice: {
    width: 150,
    borderRadius: 18,
    padding: 12,
    backgroundColor: "#EEF2FF",
  },
  exerciseChoiceActive: {
    backgroundColor: "#2563EB",
  },
  exerciseChoiceTitle: {
    fontWeight: "700",
    color: "#1E293B",
  },
  exerciseChoiceSubtitle: {
    marginTop: 6,
    fontSize: 12,
    color: "#64748B",
  },
  exerciseChoiceTitleActive: {
    color: "#FFFFFF",
  },
  friendInviteWrap: {
    gap: 8,
  },
  friendInviteRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    borderRadius: 16,
    backgroundColor: "#F8FAFC",
    paddingHorizontal: 14,
    paddingVertical: 14,
  },
  friendInviteRowSelected: {
    backgroundColor: "#DBEAFE",
  },
  friendInviteName: {
    flex: 1,
    color: "#0F172A",
    fontWeight: "600",
    marginRight: 12,
  },
  friendInviteNameSelected: {
    color: "#1D4ED8",
  },
  friendInviteCheck: {
    color: "#64748B",
    fontWeight: "700",
  },
  friendInviteCheckSelected: {
    color: "#1D4ED8",
  },
  submitButton: {
    marginTop: 6,
    borderRadius: 16,
    backgroundColor: "#16A34A",
    paddingVertical: 16,
    alignItems: "center",
  },
  submitButtonDisabled: {
    opacity: 0.5,
  },
  submitButtonText: {
    color: "#FFFFFF",
    fontSize: 16,
    fontWeight: "700",
  },
  personalCompetitionCard: {
    backgroundColor: "#FFFFFF",
    borderRadius: 22,
    padding: 18,
    marginBottom: 12,
  },
  personalCompetitionCardActive: {
    borderWidth: 2,
    borderColor: "#2563EB",
  },
  personalCompetitionTitle: {
    fontSize: 18,
    fontWeight: "700",
    color: "#0F172A",
  },
  personalCompetitionGoal: {
    marginTop: 8,
    color: "#1D4ED8",
    fontWeight: "700",
  },
  personalCompetitionDescription: {
    marginTop: 8,
    color: "#475569",
    lineHeight: 20,
  },
  personalCompetitionStats: {
    marginTop: 12,
    gap: 6,
  },
  personalCompetitionStat: {
    color: "#334155",
  },
  personalCompetitionStatus: {
    marginTop: 12,
    color: "#0F766E",
    fontWeight: "700",
  },
  inviteActions: {
    flexDirection: "row",
    gap: 10,
    marginTop: 14,
  },
  inlineActionButton: {
    flex: 1,
    borderRadius: 14,
    paddingVertical: 14,
    alignItems: "center",
  },
  acceptButton: {
    backgroundColor: "#16A34A",
  },
  declineButton: {
    backgroundColor: "#DC2626",
  },
  inlineActionText: {
    color: "#FFFFFF",
    fontWeight: "700",
  },
});
