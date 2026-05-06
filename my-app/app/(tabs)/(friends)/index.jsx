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
import { getFriends } from "../../../api/friends/getFriends";
import { getIncomingRequests } from "../../../api/friends/getIncomingRequests";
import { getOutgoingRequests } from "../../../api/friends/getOutgoingRequests";
import { get_user_profile } from "../../../api/user/get_user_profile";
import { useUserId } from "../../../hooks/useUserId";

const tabLabels = {
  friends: "Друзья",
  incoming: "Входящие",
  outgoing: "Исходящие",
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
      console.warn("Ошибка при загрузке данных друзей:", error);
    } finally {
      setRefreshing(false);
    }
  }, [isLoaded, userId]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const handleAccept = async (requestId) => {
    if (processingRequestId != null) {
      return;
    }

    setProcessingRequestId(requestId);
    try {
      await acceptFriendRequest(requestId);
      await loadData();
    } catch (error) {
      console.warn("Не удалось принять заявку:", error);
      Alert.alert("Ошибка", "Не удалось принять заявку в друзья.");
    } finally {
      setProcessingRequestId(null);
    }
  };

  const currentData =
    activeTab === "friends" ? friends : activeTab === "incoming" ? incoming : outgoing;

  return (
    <SafeAreaView style={styles.container} edges={["top"]}>
      <View style={styles.header}>
        <Text style={styles.title}>Друзья</Text>
        <Text style={styles.subtitle}>Список друзей и заявок</Text>
      </View>

      <View style={styles.tabContainer}>
        {Object.entries(tabLabels).map(([key, label]) => (
          <TouchableOpacity
            key={key}
            onPress={() => setActiveTab(key)}
            style={[styles.tab, activeTab === key && styles.activeTab]}
          >
            <Text style={styles.tabText}>{label}</Text>
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
              <Text style={styles.name}>{item.displayName}</Text>
              <Text style={styles.email}>{item.subtitle}</Text>

              {activeTab === "incoming" ? (
                <TouchableOpacity
                  style={[
                    styles.acceptButton,
                    processingRequestId === item.id && styles.acceptButtonDisabled,
                  ]}
                  onPress={() => handleAccept(item.id)}
                  disabled={processingRequestId === item.id}
                >
                  <Text style={styles.acceptButtonText}>
                    {processingRequestId === item.id ? "Обработка..." : "Принять"}
                  </Text>
                </TouchableOpacity>
              ) : null}
            </View>
          ))
        ) : (
          <Text style={styles.emptyText}>
            У вас пока нет{" "}
            {activeTab === "friends"
              ? "друзей"
              : activeTab === "incoming"
              ? "входящих заявок"
              : "исходящих заявок"}
            .
          </Text>
        )}
      </ScrollView>

      <TouchableOpacity
        style={styles.fab}
        onPress={() => Alert.alert("Недоступно", "Экран поиска друзей пока не добавлен.")}
      >
        <Feather name="user-plus" size={28} color="#fff" />
      </TouchableOpacity>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#1A1A1A",
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
  },
  tab: {
    paddingVertical: 10,
    paddingHorizontal: 20,
    marginHorizontal: 5,
    borderRadius: 20,
    backgroundColor: "#333",
  },
  activeTab: {
    backgroundColor: "#6200EA",
  },
  tabText: {
    color: "#fff",
    fontSize: 16,
  },
  scrollView: {
    paddingHorizontal: 20,
    paddingBottom: 100,
  },
  card: {
    backgroundColor: "#3B2F2F",
    padding: 16,
    borderRadius: 12,
    marginBottom: 12,
  },
  name: {
    fontSize: 18,
    color: "#fff",
    fontWeight: "600",
  },
  email: {
    fontSize: 14,
    color: "#bbb",
    marginTop: 4,
  },
  emptyText: {
    color: "#aaa",
    textAlign: "center",
    marginTop: 40,
    fontSize: 16,
  },
  acceptButton: {
    alignSelf: "flex-start",
    marginTop: 12,
    backgroundColor: "#16A34A",
    borderRadius: 10,
    paddingHorizontal: 14,
    paddingVertical: 10,
  },
  acceptButtonDisabled: {
    opacity: 0.6,
  },
  acceptButtonText: {
    color: "#fff",
    fontWeight: "700",
  },
  fab: {
    position: "absolute",
    right: 20,
    bottom: 30,
    backgroundColor: "#6200EA",
    padding: 16,
    borderRadius: 50,
    elevation: 5,
  },
});
