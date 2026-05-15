import { useCallback, useEffect, useMemo, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { useLocalSearchParams, useRouter } from "expo-router";

import { acceptFriendRequest } from "../../../api/friends/acceptFriendRequest";
import { getApiErrorMessage } from "../../../api/client";
import { cancelFriendRequest } from "../../../api/friends/cancelFriendRequest";
import { getFriendRelationship } from "../../../api/friends/getFriendRelationship";
import { removeFriend } from "../../../api/friends/removeFriend";
import { sendFriendRequest } from "../../../api/friends/sendFriendRequest";
import { fetchSocialPersonalization } from "../../../api/social/fetchSocialPersonalization";
import { get_user_profile } from "../../../api/user/get_user_profile";
import { fetchPersonalizationProfile } from "../../../api/workout/fetchPersonalization";
import { fetchWorkoutProgress } from "../../../api/workout/fetchProgress";
import SpiderChart from "../../../components/charts/SpiderChart";
import AchievementsPreview from "../../../components/profile/AchievementsPreview";
import PersonalRecordsCard from "../../../components/profile/PersonalRecordsCard";
import UserProfileCard from "../../../components/profile/UserProfileCard";
import WellnessInsightsCard from "../../../components/profile/WellnessInsightsCard";
import { useUserId } from "../../../hooks/useUserId";
import { combineAchievements, summarizeAchievements } from "../../../utils/personalization";

const PERIODS = [1, 3, 6];

const relationMeta = {
  NONE: {
    label: "Добавить в друзья",
    color: "#2563EB",
    action: "add",
  },
  INCOMING_PENDING: {
    label: "Принять",
    color: "#16A34A",
    action: "accept",
  },
  OUTGOING_PENDING: {
    label: "Отменить",
    color: "#F59E0B",
    action: "cancel",
  },
  FRIENDS: {
    label: "Удалить из друзей",
    color: "#DC2626",
    action: "remove",
  },
};

const relationDescription = {
  NONE: "Заявок в друзья между вами пока нет.",
  INCOMING_PENDING: "У этого пользователя есть неподтверждённая заявка в друзья для вас.",
  OUTGOING_PENDING: "Вы уже отправили этому пользователю заявку в друзья.",
  FRIENDS: "Вы уже друзья.",
};

const getDisplayName = (user, fallbackId) => {
  const fullName = `${user?.firstName ?? ""} ${user?.lastName ?? ""}`.trim();
  return fullName || `Пользователь #${fallbackId}`;
};

export default function OtherUserProfileScreen() {
  const router = useRouter();
  const { id } = useLocalSearchParams();
  const [currentUserId, , isUserIdLoaded] = useUserId();
  const targetUserId = useMemo(() => Number(id), [id]);

  const [periodMonths, setPeriodMonths] = useState(3);
  const [user, setUser] = useState(null);
  const [relation, setRelation] = useState(null);
  const [progress, setProgress] = useState(null);
  const [personalization, setPersonalization] = useState(null);
  const [socialPersonalization, setSocialPersonalization] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isProcessingAction, setIsProcessingAction] = useState(false);

  const loadData = useCallback(async () => {
    if (!isUserIdLoaded || currentUserId == null || !Number.isFinite(targetUserId)) {
      return;
    }

    if (currentUserId === targetUserId) {
      router.replace("/(tabs)/(profile)");
      return;
    }

    setIsLoading(true);
    try {
      const [profileData, relationData, progressData, personalizationData, socialData] =
        await Promise.all([
          get_user_profile(targetUserId),
          getFriendRelationship(currentUserId, targetUserId),
          fetchWorkoutProgress(targetUserId, periodMonths),
          fetchPersonalizationProfile(targetUserId),
          fetchSocialPersonalization(targetUserId),
        ]);

      setUser(profileData);
      setRelation(relationData);
      setProgress(progressData);
      setPersonalization(personalizationData);
      setSocialPersonalization(socialData);
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось загрузить профиль другого пользователя.")
      );
    } finally {
      setIsLoading(false);
    }
  }, [currentUserId, isUserIdLoaded, periodMonths, router, targetUserId]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const achievementSummary = useMemo(
    () =>
      summarizeAchievements(
        combineAchievements(personalization?.achievements, socialPersonalization?.achievements)
      ),
    [personalization?.achievements, socialPersonalization?.achievements]
  );

  const handleRelationAction = async () => {
    if (isProcessingAction || currentUserId == null || targetUserId == null) {
      return;
    }

    const relationType = relation?.relationType ?? "NONE";
    const meta = relationMeta[relationType];
    if (!meta) {
      return;
    }

    setIsProcessingAction(true);
    try {
      if (meta.action === "add") {
        await sendFriendRequest(currentUserId, targetUserId);
      } else if (meta.action === "accept" && relation?.requestId != null) {
        await acceptFriendRequest(relation.requestId);
      } else if (meta.action === "cancel" && relation?.requestId != null) {
        await cancelFriendRequest(relation.requestId, currentUserId);
      } else if (meta.action === "remove" && relation?.requestId != null) {
        await removeFriend(relation.requestId, currentUserId);
      }

      await loadData();
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось выполнить действие с дружбой.")
      );
    } finally {
      setIsProcessingAction(false);
    }
  };

  if (isLoading) {
    return (
      <SafeAreaView style={styles.centeredContainer}>
        <ActivityIndicator size="large" color="#2563EB" />
      </SafeAreaView>
    );
  }

  const relationType = relation?.relationType ?? "NONE";
  const action = relationMeta[relationType];

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>
        <UserProfileCard
          user={user}
          fallbackId={targetUserId}
          subtitle={relationDescription[relationType]}
        />

        {action ? (
          <TouchableOpacity
            style={[styles.primaryButton, { backgroundColor: action.color }]}
            onPress={handleRelationAction}
            disabled={isProcessingAction}
          >
            <Text style={styles.primaryButtonText}>
              {isProcessingAction ? "Обработка..." : action.label}
            </Text>
          </TouchableOpacity>
        ) : null}

        <WellnessInsightsCard
          fitnessGoal={user?.fitnessGoal}
          recoveryScore={personalization?.recoveryScore}
          recoveryStatus={personalization?.recoveryStatus}
          muscleBalance={personalization?.muscleBalance ?? []}
        />

        {personalization?.profileMessage ? (
          <View style={styles.insightCard}>
            <Text style={styles.insightLabel}>Персональная сводка</Text>
            <Text style={styles.insightText}>{personalization.profileMessage}</Text>
          </View>
        ) : null}

        <AchievementsPreview
          achievements={achievementSummary.achievements}
          unlockedCount={achievementSummary.unlockedCount}
          totalCount={achievementSummary.totalCount}
          onPressAll={() =>
            router.push({
              pathname: "/(tabs)/(profile)/achievements",
              params: {
                userId: String(targetUserId),
                title: getDisplayName(user, targetUserId),
              },
            })
          }
        />

        <PersonalRecordsCard records={personalization?.personalRecords ?? []} />

        <View style={styles.summaryRow}>
          <View style={styles.summaryCard}>
            <Text style={styles.summaryLabel}>Тренировок</Text>
            <Text style={styles.summaryValue}>{progress?.summary?.workoutsCount ?? 0}</Text>
          </View>
          <View style={styles.summaryCard}>
            <Text style={styles.summaryLabel}>Общий объём</Text>
            <Text style={styles.summaryValue}>
              {Math.round(progress?.summary?.totalVolume ?? 0)}
            </Text>
          </View>
          <View style={styles.summaryCard}>
            <Text style={styles.summaryLabel}>Прогресс</Text>
            <Text style={styles.summaryValue}>
              {Math.round(progress?.summary?.progressPercent ?? 0)}%
            </Text>
          </View>
        </View>

        <View style={styles.periodRow}>
          {PERIODS.map((period) => (
            <TouchableOpacity
              key={period}
              style={[styles.periodChip, periodMonths === period && styles.periodChipActive]}
              onPress={() => setPeriodMonths(period)}
            >
              <Text
                style={[
                  styles.periodChipText,
                  periodMonths === period && styles.periodChipTextActive,
                ]}
              >
                {period} мес.
              </Text>
            </TouchableOpacity>
          ))}
        </View>

        <SpiderChart data={progress?.muscleGroupProgress ?? []} />
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#F4F7FB",
  },
  centeredContainer: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "#F4F7FB",
  },
  content: {
    padding: 20,
    gap: 16,
  },
  primaryButton: {
    borderRadius: 16,
    paddingVertical: 16,
    alignItems: "center",
  },
  primaryButtonText: {
    color: "#FFFFFF",
    fontSize: 16,
    fontWeight: "700",
  },
  insightCard: {
    backgroundColor: "#111827",
    borderRadius: 20,
    padding: 18,
  },
  insightLabel: {
    color: "#93C5FD",
    fontSize: 13,
    fontWeight: "700",
    textTransform: "uppercase",
    letterSpacing: 0.6,
  },
  insightText: {
    color: "#F8FAFC",
    fontSize: 15,
    lineHeight: 22,
    marginTop: 10,
  },
  summaryRow: {
    flexDirection: "row",
    gap: 10,
  },
  summaryCard: {
    flex: 1,
    backgroundColor: "#111827",
    borderRadius: 18,
    padding: 14,
  },
  summaryLabel: {
    color: "#94A3B8",
    fontSize: 12,
  },
  summaryValue: {
    color: "#F8FAFC",
    fontSize: 22,
    fontWeight: "700",
    marginTop: 8,
  },
  periodRow: {
    flexDirection: "row",
    justifyContent: "center",
    gap: 10,
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
});
