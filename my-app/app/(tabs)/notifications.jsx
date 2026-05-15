import { useCallback, useEffect, useMemo, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  RefreshControl,
  SafeAreaView,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { useRouter } from "expo-router";
import { useIsFocused } from "@react-navigation/native";

import { acceptCompetitionInvite } from "../../api/competition/acceptCompetitionInvite";
import { declineCompetitionInvite } from "../../api/competition/declineCompetitionInvite";
import { acceptFriendRequest } from "../../api/friends/acceptFriendRequest";
import { getApiErrorMessage } from "../../api/client";
import { fetchNotifications } from "../../api/notifications/fetchNotifications";
import { useUserId } from "../../hooks/useUserId";

const typeMeta = {
  FRIEND_REQUEST: { backgroundColor: "#DBEAFE", accent: "#1D4ED8" },
  FRIEND_ACCEPTED: { backgroundColor: "#DCFCE7", accent: "#15803D" },
  COMPETITION_INVITE: { backgroundColor: "#FEF3C7", accent: "#B45309" },
  SMART_REMINDER: { backgroundColor: "#F3F4F6", accent: "#334155" },
  ACHIEVEMENT: { backgroundColor: "#FCE7F3", accent: "#BE185D" },
};

const formatDateLabel = (value) => {
  if (!value) {
    return "";
  }

  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return value;
  }

  return date.toLocaleDateString("ru-RU", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });
};

export default function NotificationsScreen() {
  const router = useRouter();
  const isFocused = useIsFocused();
  const [userId, , isUserIdLoaded] = useUserId();
  const [notifications, setNotifications] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isRefreshing, setIsRefreshing] = useState(false);
  const [processingId, setProcessingId] = useState(null);

  const loadNotifications = useCallback(async (mode = "load") => {
    if (!isUserIdLoaded || userId == null) {
      return;
    }

    if (mode === "refresh") {
      setIsRefreshing(true);
    } else {
      setIsLoading(true);
    }

    try {
      const response = await fetchNotifications(userId);
      setNotifications(response?.notifications ?? []);
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось загрузить уведомления.")
      );
    } finally {
      setIsLoading(false);
      setIsRefreshing(false);
    }
  }, [isUserIdLoaded, userId]);

  useEffect(() => {
    if (isFocused) {
      loadNotifications();
    }
  }, [isFocused, loadNotifications]);

  const stats = useMemo(
    () => ({
      total: notifications.length,
      actionable: notifications.filter(
        (item) => item.type === "FRIEND_REQUEST" || item.type === "COMPETITION_INVITE"
      ).length,
    }),
    [notifications]
  );

  const openNotificationTarget = (item) => {
    if (item.relatedUserId) {
      router.push(`/(tabs)/(profile)/${item.relatedUserId}`);
      return;
    }

    if (item.competitionId) {
      router.push("/(tabs)/CompetitionScreen");
    }
  };

  const handleAction = async (item, action) => {
    if (processingId || userId == null) {
      return;
    }

    setProcessingId(item.id);
    try {
      if (item.type === "FRIEND_REQUEST" && item.requestId && action === "accept") {
        await acceptFriendRequest(item.requestId);
      } else if (item.type === "COMPETITION_INVITE" && item.competitionId) {
        if (action === "accept") {
          await acceptCompetitionInvite(item.competitionId, userId);
        } else if (action === "decline") {
          await declineCompetitionInvite(item.competitionId, userId);
        }
      }

      await loadNotifications("refresh");
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось обработать уведомление.")
      );
    } finally {
      setProcessingId(null);
    }
  };

  if (isLoading) {
    return (
      <SafeAreaView style={styles.centered}>
        <ActivityIndicator size="large" color="#2563EB" />
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView
        contentContainerStyle={styles.content}
        refreshControl={
          <RefreshControl refreshing={isRefreshing} onRefresh={() => loadNotifications("refresh")} />
        }
      >
        <View style={styles.headerCard}>
          <Text style={styles.title}>Уведомления</Text>
          <Text style={styles.subtitle}>
            Здесь собраны события по дружбе, соревнованиям, достижениям и умным напоминаниям.
          </Text>

          <View style={styles.summaryRow}>
            <View style={styles.summaryCard}>
              <Text style={styles.summaryLabel}>Всего</Text>
              <Text style={styles.summaryValue}>{stats.total}</Text>
            </View>
            <View style={styles.summaryCard}>
              <Text style={styles.summaryLabel}>Требуют действия</Text>
              <Text style={styles.summaryValue}>{stats.actionable}</Text>
            </View>
          </View>
        </View>

        {notifications.length ? (
          notifications.map((item) => {
            const meta = typeMeta[item.type] ?? typeMeta.SMART_REMINDER;
            return (
              <TouchableOpacity
                key={item.id}
                activeOpacity={0.88}
                style={[styles.card, { backgroundColor: meta.backgroundColor }]}
                onPress={() => openNotificationTarget(item)}
              >
                <View style={styles.cardHeader}>
                  <Text style={[styles.cardTitle, { color: meta.accent }]}>{item.title}</Text>
                  <Text style={styles.cardDate}>{formatDateLabel(item.createdAt)}</Text>
                </View>

                <Text style={styles.cardMessage}>{item.message}</Text>

                {item.type === "FRIEND_REQUEST" ? (
                  <TouchableOpacity
                    style={[styles.actionButton, { backgroundColor: meta.accent }]}
                    onPress={() => handleAction(item, "accept")}
                    disabled={processingId === item.id}
                  >
                    <Text style={styles.actionButtonText}>
                      {processingId === item.id ? "Обработка..." : "Принять заявку"}
                    </Text>
                  </TouchableOpacity>
                ) : null}

                {item.type === "COMPETITION_INVITE" ? (
                  <View style={styles.inlineActions}>
                    <TouchableOpacity
                      style={[styles.inlineButton, { backgroundColor: "#15803D" }]}
                      onPress={() => handleAction(item, "accept")}
                      disabled={processingId === item.id}
                    >
                      <Text style={styles.inlineButtonText}>
                        {processingId === item.id ? "..." : "Принять"}
                      </Text>
                    </TouchableOpacity>
                    <TouchableOpacity
                      style={[styles.inlineButton, { backgroundColor: "#B91C1C" }]}
                      onPress={() => handleAction(item, "decline")}
                      disabled={processingId === item.id}
                    >
                      <Text style={styles.inlineButtonText}>Отклонить</Text>
                    </TouchableOpacity>
                  </View>
                ) : null}
              </TouchableOpacity>
            );
          })
        ) : (
          <View style={styles.emptyCard}>
            <Text style={styles.emptyText}>Пока уведомлений нет.</Text>
          </View>
        )}
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
    gap: 14,
  },
  headerCard: {
    backgroundColor: "#111827",
    borderRadius: 22,
    padding: 20,
  },
  title: {
    color: "#FFFFFF",
    fontSize: 28,
    fontWeight: "700",
  },
  subtitle: {
    color: "#CBD5E1",
    lineHeight: 20,
    marginTop: 8,
  },
  summaryRow: {
    flexDirection: "row",
    gap: 10,
    marginTop: 16,
  },
  summaryCard: {
    flex: 1,
    backgroundColor: "#1F2937",
    borderRadius: 16,
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
  card: {
    borderRadius: 20,
    padding: 16,
  },
  cardHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    gap: 10,
  },
  cardTitle: {
    flex: 1,
    fontSize: 17,
    fontWeight: "700",
  },
  cardDate: {
    color: "#64748B",
    fontSize: 12,
  },
  cardMessage: {
    color: "#334155",
    lineHeight: 20,
    marginTop: 10,
  },
  actionButton: {
    marginTop: 14,
    borderRadius: 14,
    paddingVertical: 14,
    alignItems: "center",
  },
  actionButtonText: {
    color: "#FFFFFF",
    fontWeight: "700",
  },
  inlineActions: {
    flexDirection: "row",
    gap: 10,
    marginTop: 14,
  },
  inlineButton: {
    flex: 1,
    borderRadius: 14,
    paddingVertical: 14,
    alignItems: "center",
  },
  inlineButtonText: {
    color: "#FFFFFF",
    fontWeight: "700",
  },
  emptyCard: {
    backgroundColor: "#FFFFFF",
    borderRadius: 20,
    padding: 24,
    alignItems: "center",
  },
  emptyText: {
    color: "#64748B",
    fontSize: 16,
  },
});
