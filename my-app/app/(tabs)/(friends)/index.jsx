import React, { useCallback, useEffect, useState } from "react";
import {
  Alert,
  RefreshControl,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { Feather } from "@expo/vector-icons";
import { useRouter } from "expo-router";
import { SafeAreaView } from "react-native-safe-area-context";

import { acceptFriendRequest } from "../../../api/friends/acceptFriendRequest";
import { getApiErrorMessage } from "../../../api/client";
import { cancelFriendRequest } from "../../../api/friends/cancelFriendRequest";
import { getFriends } from "../../../api/friends/getFriends";
import { getIncomingRequests } from "../../../api/friends/getIncomingRequests";
import { getOutgoingRequests } from "../../../api/friends/getOutgoingRequests";
import { removeFriend } from "../../../api/friends/removeFriend";
import { get_user_profile } from "../../../api/user/get_user_profile";
import { useUserId } from "../../../hooks/useUserId";

const tabLabels = {
  friends: "Друзья",
  incoming: "Входящие",
  outgoing: "Исходящие",
};

const actionMap = {
  friends: {
    label: "Удалить из друзей",
    color: "#DC2626",
    action: "remove",
  },
  incoming: {
    label: "Принять",
    color: "#16A34A",
    action: "accept",
  },
  outgoing: {
    label: "Отменить",
    color: "#F59E0B",
    action: "cancel",
  },
};

const buildDisplayName = (profile, userId) => {
  const fullName = `${profile?.firstName ?? ""} ${profile?.lastName ?? ""}`.trim();
  return fullName || `Пользователь #${userId}`;
};

const enrichRequests = async (items, currentUserId) =>
  Promise.all(
    (items ?? []).map(async (item) => {
      const relatedUserId =
        item.senderId === currentUserId ? item.receiverId : item.senderId;

      try {
        const profile = await get_user_profile(relatedUserId);
        return {
          ...item,
          relatedUserId,
          displayName: buildDisplayName(profile, relatedUserId),
          subtitle: profile?.email ?? `ID: ${relatedUserId}`,
        };
      } catch (error) {
        return {
          ...item,
          relatedUserId,
          displayName: `Пользователь #${relatedUserId}`,
          subtitle: `ID: ${relatedUserId}`,
        };
      }
    })
  );

export default function FriendsScreen() {
  const router = useRouter();
  const [userId, , isLoaded] = useUserId();
  const [activeTab, setActiveTab] = useState("friends");
  const [friends, setFriends] = useState([]);
  const [incoming, setIncoming] = useState([]);
  const [outgoing, setOutgoing] = useState([]);
  const [refreshing, setRefreshing] = useState(false);
  const [processingRequestId, setProcessingRequestId] = useState(null);

  const loadData = useCallback(async () => {
    if (!isLoaded || userId === null) {
      return;
    }

    setRefreshing(true);
    try {
      const [friendsRes, incomingRes, outgoingRes] = await Promise.all([
        getFriends(userId),
        getIncomingRequests(userId),
        getOutgoingRequests(userId),
      ]);

      const [friendsWithProfiles, incomingWithProfiles, outgoingWithProfiles] =
        await Promise.all([
          enrichRequests(friendsRes, userId),
          enrichRequests(incomingRes, userId),
          enrichRequests(outgoingRes, userId),
        ]);

      setFriends(friendsWithProfiles);
      setIncoming(incomingWithProfiles);
      setOutgoing(outgoingWithProfiles);
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось загрузить список друзей и заявок.")
      );
    } finally {
      setRefreshing(false);
    }
  }, [isLoaded, userId]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const handleRequestAction = async (action, item) => {
    if (processingRequestId != null || userId == null) {
      return;
    }

    setProcessingRequestId(item.id);
    try {
      if (action === "accept") {
        await acceptFriendRequest(item.id);
      } else if (action === "cancel") {
        await cancelFriendRequest(item.id, userId);
      } else if (action === "remove") {
        await removeFriend(item.id, userId);
      }

      await loadData();
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось выполнить действие с заявкой в друзья.")
      );
    } finally {
      setProcessingRequestId(null);
    }
  };

  const openUserProfile = (targetUserId) => {
    router.push(`/(tabs)/(profile)/${targetUserId}`);
  };

  const currentData =
    activeTab === "friends" ? friends : activeTab === "incoming" ? incoming : outgoing;
  const actionConfig = actionMap[activeTab];

  return (
    <SafeAreaView style={styles.container} edges={["top"]}>
      <View style={styles.header}>
        <Text style={styles.title}>Друзья</Text>
        <Text style={styles.subtitle}>Список друзей и заявок в друзья</Text>
      </View>

      <View style={styles.tabContainer}>
        {Object.entries(tabLabels).map(([key, label]) => (
          <TouchableOpacity
            key={key}
            onPress={() => setActiveTab(key)}
            style={[styles.tab, activeTab === key && styles.activeTab]}
          >
            <Text style={[styles.tabText, activeTab === key && styles.activeTabText]}>
              {label}
            </Text>
          </TouchableOpacity>
        ))}
      </View>

      <ScrollView
        contentContainerStyle={styles.scrollView}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={loadData} />}
      >
        {currentData.length ? (
          currentData.map((item) => (
            <View key={item.id} style={styles.card}>
              <TouchableOpacity
                activeOpacity={0.82}
                style={styles.cardContent}
                onPress={() => openUserProfile(item.relatedUserId)}
              >
                <Text style={styles.name}>{item.displayName}</Text>
                <Text style={styles.email}>{item.subtitle}</Text>
              </TouchableOpacity>

              <TouchableOpacity
                style={[
                  styles.actionButton,
                  { backgroundColor: actionConfig.color },
                  processingRequestId === item.id && styles.actionButtonDisabled,
                ]}
                onPress={() => handleRequestAction(actionConfig.action, item)}
                disabled={processingRequestId === item.id}
              >
                <Text style={styles.actionButtonText}>
                  {processingRequestId === item.id ? "Обработка..." : actionConfig.label}
                </Text>
              </TouchableOpacity>
            </View>
          ))
        ) : (
          <Text style={styles.emptyText}>
            {activeTab === "friends" && "У вас пока нет друзей."}
            {activeTab === "incoming" && "У вас пока нет входящих заявок."}
            {activeTab === "outgoing" && "У вас пока нет исходящих заявок."}
          </Text>
        )}
      </ScrollView>

      <TouchableOpacity
        style={styles.fab}
        onPress={() =>
          Alert.alert(
            "Недоступно",
            "Экран поиска друзей пока не добавлен. Переход в чужие профили уже работает из текущих списков."
          )
        }
      >
        <Feather name="user-plus" size={28} color="#fff" />
      </TouchableOpacity>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#0F172A",
  },
  header: {
    paddingHorizontal: 20,
    paddingTop: 8,
    marginBottom: 12,
  },
  title: {
    color: "#fff",
    fontSize: 28,
    fontWeight: "700",
  },
  subtitle: {
    color: "#94A3B8",
    marginTop: 4,
  },
  tabContainer: {
    flexDirection: "row",
    justifyContent: "center",
    marginBottom: 12,
    paddingHorizontal: 16,
    gap: 8,
  },
  tab: {
    flex: 1,
    paddingVertical: 12,
    borderRadius: 16,
    backgroundColor: "#1E293B",
    alignItems: "center",
  },
  activeTab: {
    backgroundColor: "#38BDF8",
  },
  tabText: {
    color: "#CBD5E1",
    fontSize: 15,
    fontWeight: "600",
  },
  activeTabText: {
    color: "#0F172A",
  },
  scrollView: {
    paddingHorizontal: 20,
    paddingBottom: 110,
  },
  card: {
    backgroundColor: "#111827",
    padding: 16,
    borderRadius: 16,
    marginBottom: 12,
    gap: 12,
  },
  cardContent: {
    gap: 4,
  },
  name: {
    fontSize: 18,
    color: "#fff",
    fontWeight: "600",
  },
  email: {
    fontSize: 14,
    color: "#94A3B8",
  },
  actionButton: {
    alignSelf: "flex-start",
    borderRadius: 12,
    paddingHorizontal: 14,
    paddingVertical: 10,
  },
  actionButtonDisabled: {
    opacity: 0.65,
  },
  actionButtonText: {
    color: "#fff",
    fontWeight: "700",
  },
  emptyText: {
    color: "#94A3B8",
    textAlign: "center",
    marginTop: 40,
    fontSize: 16,
  },
  fab: {
    position: "absolute",
    right: 20,
    bottom: 30,
    backgroundColor: "#2563EB",
    padding: 16,
    borderRadius: 50,
    elevation: 5,
  },
});
