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
import { useRouter } from "expo-router";
import { Feather } from "@expo/vector-icons";
import { getApiErrorMessage } from "../../../api/client";
import WorkoutCard from "./workout_card";
import ProgramCard from "./program_card";
import { fetchUserPrograms, fetchUserWorkouts } from "../../../api/workout/fetchUserData";
import { useUserId } from "../../../hooks/useUserId";

const WorkoutScreen = () => {
  const router = useRouter();
  const [activeTab, setActiveTab] = useState("workouts");
  const [workouts, setWorkouts] = useState([]);
  const [programs, setPrograms] = useState([]);
  const [refreshing, setRefreshing] = useState(false);
  const [userId, , isUserIdLoaded] = useUserId();

  const loadData = useCallback(async () => {
    if (!isUserIdLoaded || userId == null) {
      return;
    }

    setRefreshing(true);
    try {
      const [userWorkouts, userPrograms] = await Promise.all([
        fetchUserWorkouts(userId),
        fetchUserPrograms(userId),
      ]);
      setWorkouts(userWorkouts);
      setPrograms(userPrograms);
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось загрузить тренировки и программы.")
      );
    } finally {
      setRefreshing(false);
    }
  }, [isUserIdLoaded, userId]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const renderEmptyMessage = () => (
    <Text style={styles.emptyText}>
      Пока что у вас нет {activeTab === "workouts" ? "тренировок" : "программ"}.
    </Text>
  );

  const renderContent = () => {
    const data = activeTab === "workouts" ? workouts : programs;

    if (!data.length) {
      return renderEmptyMessage();
    }

    return data.map((item) =>
      activeTab === "workouts" ? (
        <WorkoutCard key={item.id} workout={item} />
      ) : (
        <ProgramCard key={item.id} program={item} />
      )
    );
  };

  const handleCreate = () => {
    router.push(
      activeTab === "workouts"
        ? "/(tabs)/(workout)/create-workout"
        : "/(tabs)/(workout)/create-program"
    );
  };

  return (
    <View style={styles.container}>
      <View style={styles.headerRow}>
        <View>
          <Text style={styles.heading}>Тренировки</Text>
          <Text style={styles.subheading}>Список занятий и программ</Text>
        </View>
        <TouchableOpacity
          style={styles.progressButton}
          onPress={() => router.push("/(tabs)/(workout)/progress")}
        >
          <Feather name="bar-chart-2" size={18} color="#fff" />
          <Text style={styles.progressButtonText}>Прогресс</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.tabContainer}>
        <TouchableOpacity
          onPress={() => setActiveTab("workouts")}
          style={[styles.tab, activeTab === "workouts" && styles.activeTab]}
        >
          <Text style={styles.tabText}>Тренировки</Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() => setActiveTab("programs")}
          style={[styles.tab, activeTab === "programs" && styles.activeTab]}
        >
          <Text style={styles.tabText}>Программы</Text>
        </TouchableOpacity>
      </View>

      <ScrollView
        contentContainerStyle={styles.scrollView}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={loadData} />}
      >
        {renderContent()}
      </ScrollView>

      <TouchableOpacity style={styles.fab} onPress={handleCreate}>
        <Feather name="plus" size={28} color="#fff" />
      </TouchableOpacity>
    </View>
  );
};

export default WorkoutScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#1A1A1A",
    paddingTop: 40,
  },
  headerRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    paddingHorizontal: 20,
    marginBottom: 16,
  },
  heading: {
    color: "#fff",
    fontSize: 26,
    fontWeight: "700",
  },
  subheading: {
    color: "#94A3B8",
    fontSize: 14,
    marginTop: 4,
  },
  progressButton: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#2563EB",
    borderRadius: 14,
    paddingHorizontal: 14,
    paddingVertical: 10,
    gap: 8,
  },
  progressButtonText: {
    color: "#fff",
    fontWeight: "600",
  },
  tabContainer: {
    flexDirection: "row",
    justifyContent: "center",
    marginBottom: 12,
  },
  tab: {
    paddingVertical: 10,
    paddingHorizontal: 20,
    marginHorizontal: 10,
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
  emptyText: {
    color: "#aaa",
    textAlign: "center",
    marginTop: 40,
    fontSize: 16,
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
