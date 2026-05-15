import { useEffect, useMemo, useState } from "react";
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
import { useRouter } from "expo-router";
import { useIsFocused } from "@react-navigation/native";

import { delete_account } from "../../../api/auth/delete_account";
import { getApiErrorMessage } from "../../../api/client";
import { fetchSocialPersonalization } from "../../../api/social/fetchSocialPersonalization";
import { delete_user_profile } from "../../../api/user/delete_user_profile";
import { get_user_profile } from "../../../api/user/get_user_profile";
import { fetchPersonalizationProfile } from "../../../api/workout/fetchPersonalization";
import AchievementsPreview from "../../../components/profile/AchievementsPreview";
import PersonalRecordsCard from "../../../components/profile/PersonalRecordsCard";
import UserProfileCard from "../../../components/profile/UserProfileCard";
import WellnessInsightsCard from "../../../components/profile/WellnessInsightsCard";
import { useSession } from "../../../context/ctx";
import { useUserId } from "../../../hooks/useUserId";
import { combineAchievements, summarizeAchievements } from "../../../utils/personalization";

const MENU_ITEMS = [
  { key: "edit", title: "Изменить профиль", route: "/(tabs)/(profile)/edit" },
  { key: "password", title: "Изменить пароль", route: "/(tabs)/(profile)/password" },
  { key: "achievements", title: "Все достижения", route: "/(tabs)/(profile)/achievements" },
  { key: "notifications", title: "Открыть уведомления", route: "/(tabs)/notifications" },
  { key: "friends", title: "Открыть друзей", route: "/(tabs)/(friends)" },
];

export default function ProfileScreen() {
  const router = useRouter();
  const isFocused = useIsFocused();
  const { signOut } = useSession();
  const [userId, setUserId, isLoaded] = useUserId();
  const [user, setUser] = useState(null);
  const [personalization, setPersonalization] = useState(null);
  const [socialPersonalization, setSocialPersonalization] = useState(null);
  const [isFetching, setIsFetching] = useState(true);
  const [isDeleting, setIsDeleting] = useState(false);

  useEffect(() => {
    let isMounted = true;

    const loadProfile = async () => {
      if (!isFocused || !isLoaded) {
        return;
      }

      if (userId === null) {
        signOut();
        await setUserId(null);
        router.replace("/(auth)/sign_up");
        return;
      }

      setIsFetching(true);
      try {
        const [profile, personalizationData, socialData] = await Promise.all([
          get_user_profile(userId),
          fetchPersonalizationProfile(userId),
          fetchSocialPersonalization(userId),
        ]);

        if (isMounted) {
          setUser(profile);
          setPersonalization(personalizationData);
          setSocialPersonalization(socialData);
        }
      } catch (error) {
        if (isMounted) {
          Alert.alert(
            "Ошибка",
            getApiErrorMessage(error, "Не удалось загрузить профиль.")
          );
        }
      } finally {
        if (isMounted) {
          setIsFetching(false);
        }
      }
    };

    loadProfile();

    return () => {
      isMounted = false;
    };
  }, [isFocused, isLoaded, router, setUserId, signOut, userId]);

  const achievementSummary = useMemo(
    () =>
      summarizeAchievements(
        combineAchievements(personalization?.achievements, socialPersonalization?.achievements)
      ),
    [personalization?.achievements, socialPersonalization?.achievements]
  );

  const navigateToRegistration = async () => {
    signOut();
    await setUserId(null);
    router.replace("/(auth)/sign_up");
  };

  const handleLogout = () => {
    Alert.alert("Выход", "После выхода откроется экран регистрации.", [
      { text: "Отмена", style: "cancel" },
      { text: "Выйти", onPress: () => navigateToRegistration() },
    ]);
  };

  const handleDelete = () => {
    Alert.alert("Удаление аккаунта", "Аккаунт будет удалён без возможности восстановления.", [
      { text: "Отмена", style: "cancel" },
      {
        text: "Удалить",
        style: "destructive",
        onPress: async () => {
          if (userId === null || isDeleting) {
            return;
          }

          setIsDeleting(true);
          try {
            await delete_user_profile(userId);
            await delete_account(userId);
            await navigateToRegistration();
          } catch (error) {
            Alert.alert(
              "Ошибка",
              getApiErrorMessage(error, "Не удалось удалить аккаунт.")
            );
          } finally {
            setIsDeleting(false);
          }
        },
      },
    ]);
  };

  if (isFetching) {
    return (
      <SafeAreaView style={styles.centeredContainer}>
        <ActivityIndicator size="large" color="#007AFF" />
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>
        <UserProfileCard user={user} fallbackId={userId} />

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
          onPressAll={() => router.push("/(tabs)/(profile)/achievements")}
        />

        <PersonalRecordsCard records={personalization?.personalRecords ?? []} />

        <View style={styles.menuCard}>
          {MENU_ITEMS.map((item) => (
            <TouchableOpacity
              key={item.key}
              style={styles.menuItem}
              onPress={() => router.push(item.route)}
            >
              <Text style={styles.menuText}>{item.title}</Text>
            </TouchableOpacity>
          ))}
        </View>

        <TouchableOpacity style={styles.secondaryButton} onPress={handleLogout}>
          <Text style={styles.secondaryButtonText}>Выйти из аккаунта</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={[styles.secondaryButton, styles.deleteButton]}
          onPress={handleDelete}
          disabled={isDeleting}
        >
          <Text style={[styles.secondaryButtonText, styles.deleteButtonText]}>
            {isDeleting ? "Удаление..." : "Удалить аккаунт"}
          </Text>
        </TouchableOpacity>
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
  menuCard: {
    backgroundColor: "#FFFFFF",
    borderRadius: 20,
    overflow: "hidden",
  },
  menuItem: {
    paddingHorizontal: 18,
    paddingVertical: 18,
    borderBottomWidth: StyleSheet.hairlineWidth,
    borderBottomColor: "#E5E7EB",
  },
  menuText: {
    fontSize: 16,
    color: "#111827",
    fontWeight: "600",
  },
  secondaryButton: {
    backgroundColor: "#111827",
    borderRadius: 16,
    paddingVertical: 16,
    alignItems: "center",
  },
  secondaryButtonText: {
    color: "#FFFFFF",
    fontSize: 16,
    fontWeight: "700",
  },
  deleteButton: {
    backgroundColor: "#FEE2E2",
  },
  deleteButtonText: {
    color: "#B91C1C",
  },
});
