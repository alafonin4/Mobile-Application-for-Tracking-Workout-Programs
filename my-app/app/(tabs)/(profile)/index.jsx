import { useEffect, useState } from "react";
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
import { delete_user_profile } from "../../../api/user/delete_user_profile";
import { get_user_profile } from "../../../api/user/get_user_profile";
import { useSession } from "../../../context/ctx";
import { useUserId } from "../../../hooks/useUserId";

const MENU_ITEMS = [
  { key: "edit", title: "Изменить профиль", route: "/(tabs)/(profile)/edit" },
  { key: "password", title: "Изменить пароль", route: "/(tabs)/(profile)/password" },
  { key: "friends", title: "Открыть друзей", route: "/(tabs)/(friends)" },
];

export default function ProfileScreen() {
  const router = useRouter();
  const isFocused = useIsFocused();
  const { signOut } = useSession();
  const [userId, setUserId, isLoaded] = useUserId();
  const [user, setUser] = useState(null);
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
        const profile = await get_user_profile(userId);
        if (isMounted) {
          setUser(profile);
        }
      } catch (error) {
        console.warn("Не удалось загрузить профиль:", error);
        if (isMounted) {
          Alert.alert("Ошибка", "Не удалось загрузить профиль.");
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
  }, [isFocused, isLoaded, userId]);

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
    Alert.alert("Удаление аккаунта", "Аккаунт будет удален без возможности восстановления.", [
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
            console.warn("Не удалось удалить аккаунт:", error);
            Alert.alert("Ошибка", "Не удалось удалить аккаунт.");
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
        <View style={styles.headerCard}>
          <Text style={styles.name}>
            {user?.firstName} {user?.lastName}
          </Text>
          <Text style={styles.email}>{user?.email ?? "Email не указан"}</Text>
          <Text style={styles.meta}>Вес: {user?.bodyWeight ?? 0} кг</Text>
          <Text style={styles.bio}>{user?.bio || "Добавьте описание о себе."}</Text>
        </View>

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
  headerCard: {
    backgroundColor: "#FFFFFF",
    borderRadius: 20,
    padding: 20,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.08,
    shadowRadius: 16,
    elevation: 3,
  },
  name: {
    fontSize: 26,
    fontWeight: "700",
    color: "#111827",
    marginBottom: 8,
  },
  email: {
    fontSize: 16,
    color: "#4B5563",
    marginBottom: 10,
  },
  meta: {
    fontSize: 15,
    color: "#1D4ED8",
    marginBottom: 12,
  },
  bio: {
    fontSize: 15,
    lineHeight: 22,
    color: "#374151",
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
