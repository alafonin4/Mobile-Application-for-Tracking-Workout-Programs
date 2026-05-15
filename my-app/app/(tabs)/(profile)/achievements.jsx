import { useEffect, useMemo, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from "react-native";
import { useLocalSearchParams } from "expo-router";

import { getApiErrorMessage } from "../../../api/client";
import { fetchSocialPersonalization } from "../../../api/social/fetchSocialPersonalization";
import { fetchPersonalizationProfile } from "../../../api/workout/fetchPersonalization";
import PersonalRecordsCard from "../../../components/profile/PersonalRecordsCard";
import { useUserId } from "../../../hooks/useUserId";
import { combineAchievements, summarizeAchievements } from "../../../utils/personalization";

const sortAchievements = (items = []) =>
  [...items].sort((left, right) => {
    if (left.unlocked !== right.unlocked) {
      return left.unlocked ? -1 : 1;
    }

    return (right.progressPercent ?? 0) - (left.progressPercent ?? 0);
  });

const formatMetric = (value) => {
  if (!Number.isFinite(Number(value))) {
    return 0;
  }

  const numericValue = Number(value);
  return Number.isInteger(numericValue) ? numericValue : Math.round(numericValue * 10) / 10;
};

export default function AchievementsScreen() {
  const params = useLocalSearchParams();
  const [currentUserId, , isUserIdLoaded] = useUserId();

  const targetUserId = useMemo(() => {
    const raw = params.userId ?? currentUserId;
    const parsed = Number(raw);
    return Number.isFinite(parsed) ? parsed : null;
  }, [currentUserId, params.userId]);

  const profileTitle =
    typeof params.title === "string" && params.title.trim().length
      ? params.title
      : "Ваш прогресс";

  const [data, setData] = useState(null);
  const [socialData, setSocialData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      if (!isUserIdLoaded || targetUserId == null) {
        if (isUserIdLoaded) {
          setIsLoading(false);
        }
        return;
      }

      setIsLoading(true);
      try {
        const [workoutData, socialResponse] = await Promise.all([
          fetchPersonalizationProfile(targetUserId),
          fetchSocialPersonalization(targetUserId),
        ]);
        setData(workoutData);
        setSocialData(socialResponse);
      } catch (error) {
        Alert.alert(
          "Ошибка",
          getApiErrorMessage(error, "Не удалось загрузить достижения.")
        );
      } finally {
        setIsLoading(false);
      }
    };

    load();
  }, [isUserIdLoaded, targetUserId]);

  if (isLoading) {
    return (
      <SafeAreaView style={styles.centered}>
        <ActivityIndicator size="large" color="#2563EB" />
      </SafeAreaView>
    );
  }

  const achievementSummary = summarizeAchievements(
    combineAchievements(data?.achievements, socialData?.achievements)
  );
  const achievements = sortAchievements(achievementSummary.achievements);

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>
        <View style={styles.headerCard}>
          <Text style={styles.title}>Все достижения</Text>
          <Text style={styles.owner}>{profileTitle}</Text>
          <Text style={styles.subtitle}>
            Открыто {achievementSummary.unlockedCount} из {achievementSummary.totalCount}
          </Text>
          <Text style={styles.message}>
            {data?.profileMessage ?? "Прогресс и достижения появляются по мере тренировок."}
          </Text>
        </View>

        <PersonalRecordsCard records={data?.personalRecords ?? []} />

        <View style={styles.card}>
          <Text style={styles.sectionTitle}>Лента достижений</Text>

          {achievements.length ? (
            achievements.map((achievement) => (
              <View
                key={achievement.code}
                style={[
                  styles.achievementRow,
                  achievement.unlocked ? styles.achievementUnlocked : styles.achievementLocked,
                ]}
              >
                <View style={styles.achievementMain}>
                  <Text style={styles.achievementTitle}>{achievement.title}</Text>
                  <Text style={styles.achievementCategory}>{achievement.category}</Text>
                  <Text style={styles.achievementDescription}>{achievement.description}</Text>
                </View>

                <View style={styles.achievementMeta}>
                  <Text style={styles.achievementProgress}>
                    {achievement.unlocked
                      ? "Открыто"
                      : `${Math.round(achievement.progressPercent ?? 0)}%`}
                  </Text>
                  <Text style={styles.achievementTarget}>
                    {formatMetric(achievement.currentValue)} / {formatMetric(achievement.targetValue)}{" "}
                    {achievement.unit}
                  </Text>
                  {achievement.awardedAt ? (
                    <Text style={styles.achievementDate}>{achievement.awardedAt}</Text>
                  ) : null}
                </View>
              </View>
            ))
          ) : (
            <Text style={styles.emptyText}>Пока нет достижений для отображения.</Text>
          )}
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#F4F7FB",
  },
  centered: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "#F4F7FB",
  },
  content: {
    padding: 20,
    gap: 16,
  },
  headerCard: {
    backgroundColor: "#111827",
    borderRadius: 20,
    padding: 20,
  },
  title: {
    color: "#FFFFFF",
    fontSize: 24,
    fontWeight: "700",
  },
  owner: {
    color: "#E5E7EB",
    marginTop: 8,
    fontSize: 15,
  },
  subtitle: {
    color: "#93C5FD",
    marginTop: 8,
    fontWeight: "600",
  },
  message: {
    color: "#E5E7EB",
    marginTop: 10,
    lineHeight: 20,
  },
  card: {
    backgroundColor: "#FFFFFF",
    borderRadius: 20,
    padding: 18,
  },
  sectionTitle: {
    fontSize: 20,
    fontWeight: "700",
    color: "#111827",
    marginBottom: 12,
  },
  achievementRow: {
    borderRadius: 16,
    padding: 14,
    marginTop: 10,
    flexDirection: "row",
    justifyContent: "space-between",
    gap: 12,
  },
  achievementUnlocked: {
    backgroundColor: "#ECFDF5",
  },
  achievementLocked: {
    backgroundColor: "#F3F4F6",
  },
  achievementMain: {
    flex: 1,
  },
  achievementTitle: {
    color: "#111827",
    fontWeight: "700",
    fontSize: 15,
  },
  achievementCategory: {
    marginTop: 4,
    color: "#2563EB",
    fontWeight: "600",
    fontSize: 12,
  },
  achievementDescription: {
    marginTop: 6,
    color: "#4B5563",
    lineHeight: 18,
  },
  achievementMeta: {
    alignItems: "flex-end",
  },
  achievementProgress: {
    color: "#047857",
    fontWeight: "700",
  },
  achievementTarget: {
    marginTop: 4,
    color: "#6B7280",
    fontSize: 12,
  },
  achievementDate: {
    marginTop: 4,
    color: "#6B7280",
    fontSize: 12,
  },
  emptyText: {
    color: "#6B7280",
    lineHeight: 20,
  },
});
