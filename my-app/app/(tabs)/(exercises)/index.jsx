import React, { useEffect, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  FlatList,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { useRouter } from "expo-router";
import { getApiErrorMessage } from "../../../api/client";
import { getAllExercises } from "../../../api/exercises/getAllExercises";

const muscleGroups = ["Все", "Грудь", "Спина", "Ноги", "Плечи", "Бицепс", "Трицепс", "Пресс"];

export default function AllExercisesScreen() {
  const router = useRouter();
  const [selectedGroup, setSelectedGroup] = useState("Все");
  const [allExercises, setAllExercises] = useState([]);
  const [filteredExercises, setFilteredExercises] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadExercises = async () => {
      try {
        const data = await getAllExercises();
        setAllExercises(data);
        setFilteredExercises(data);
      } catch (error) {
        Alert.alert(
          "Ошибка",
          getApiErrorMessage(error, "Не удалось загрузить список упражнений.")
        );
      } finally {
        setLoading(false);
      }
    };

    loadExercises();
  }, []);

  useEffect(() => {
    if (selectedGroup === "Все") {
      setFilteredExercises(allExercises);
      return;
    }

    setFilteredExercises(
      allExercises.filter((exercise) => exercise.muscleGroup === selectedGroup)
    );
  }, [selectedGroup, allExercises]);

  if (loading) {
    return (
      <SafeAreaView style={styles.loadingContainer} edges={["top"]}>
        <ActivityIndicator size="large" color="#6a3cb0" />
      </SafeAreaView>
    );
  }

  return (
    <SafeAreaView style={styles.container} edges={["top"]}>
      <View style={styles.header}>
        <Text style={styles.title}>Упражнения</Text>
        <Text style={styles.subtitle}>Фильтр по мышечным группам</Text>
      </View>

      <FlatList
        horizontal
        data={muscleGroups}
        keyExtractor={(item) => item}
        contentContainerStyle={styles.filterContainer}
        showsHorizontalScrollIndicator={false}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={[styles.filterButton, selectedGroup === item && styles.selectedButton]}
            onPress={() => setSelectedGroup(item)}
          >
            <Text style={[styles.filterText, selectedGroup === item && styles.selectedText]}>
              {item}
            </Text>
          </TouchableOpacity>
        )}
      />

      <FlatList
        data={filteredExercises}
        keyExtractor={(item) => item.id.toString()}
        contentContainerStyle={styles.exerciseList}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.exerciseItem}
            onPress={() => router.push(`/(tabs)/(exercises)/${item.id}`)}
          >
            <Text style={styles.exerciseName}>{item.name}</Text>
            <Text style={styles.exerciseGroup}>{item.muscleGroup}</Text>
          </TouchableOpacity>
        )}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#121212",
  },
  loadingContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#121212",
  },
  header: {
    paddingHorizontal: 16,
    paddingTop: 8,
  },
  title: {
    color: "#fff",
    fontSize: 28,
    fontWeight: "700",
  },
  subtitle: {
    color: "#9c9c9c",
    marginTop: 4,
    marginBottom: 8,
  },
  filterContainer: {
    paddingHorizontal: 12,
    paddingTop: 10,
    height: "20%",
  },
  filterButton: {
    backgroundColor: "#242424",
    borderRadius: 999,
    paddingHorizontal: 14,
    paddingVertical: 10,
    marginRight: 8,
  },
  selectedButton: {
    backgroundColor: "#6a3cb0",
  },
  filterText: {
    color: "#d8d8d8",
  },
  selectedText: {
    color: "#fff",
  },
  exerciseList: {
    paddingHorizontal: 16,
    paddingBottom: 24,
  },
  exerciseItem: {
    backgroundColor: "#1f1f1f",
    borderRadius: 14,
    padding: 16,
    marginBottom: 12,
  },
  exerciseName: {
    color: "#fff",
    fontSize: 17,
    fontWeight: "600",
  },
  exerciseGroup: {
    color: "#9c9c9c",
    marginTop: 4,
  },
});
