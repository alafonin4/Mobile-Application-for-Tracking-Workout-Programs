import React, { useMemo } from "react";
import { ScrollView, StyleSheet, Text, View } from "react-native";
import { useLocalSearchParams } from "expo-router";

const formatDate = (value) => {
  if (!value) {
    return "Без даты";
  }

  const date = new Date(value);
  return Number.isNaN(date.getTime()) ? value : date.toLocaleDateString();
};

export default function WorkoutDetailsScreen() {
  const params = useLocalSearchParams();

  const workout = useMemo(() => {
    try {
      return params.workout ? JSON.parse(params.workout) : null;
    } catch (error) {
      return null;
    }
  }, [params.workout]);

  if (!workout) {
    return (
      <View style={styles.emptyContainer}>
        <Text style={styles.emptyText}>Не удалось открыть тренировку.</Text>
      </View>
    );
  }

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <View style={styles.headerCard}>
        <Text style={styles.title}>{workout.name || `Тренировка #${workout.id}`}</Text>
        <Text style={styles.meta}>Дата: {formatDate(workout.workoutDate)}</Text>
        <Text style={styles.meta}>Упражнений: {workout.workoutExercises?.length ?? 0}</Text>
      </View>

      {(workout.workoutExercises ?? []).map((item, index) => (
        <View key={item.id ?? `${item.exercise?.id}-${index}`} style={styles.block}>
          <Text style={styles.exerciseTitle}>{item.exercise?.name ?? "Упражнение"}</Text>
          <Text style={styles.exerciseSubtitle}>
            {item.exercise?.muscleGroup ?? "Мышечная группа не указана"}
          </Text>

          {(item.sets ?? []).map((setItem, setIndex) => (
            <Text key={setItem.id ?? setIndex} style={styles.setText}>
              Подход {setItem.setNumber ?? setIndex + 1}: {setItem.reps ?? 0} повторений,{" "}
              {setItem.weight ?? 0} кг
            </Text>
          ))}
        </View>
      ))}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 20,
    backgroundColor: "#111827",
    minHeight: "100%",
  },
  emptyContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#111827",
  },
  emptyText: {
    color: "#CBD5E1",
    fontSize: 16,
  },
  headerCard: {
    backgroundColor: "#1F2937",
    borderRadius: 18,
    padding: 18,
    marginBottom: 16,
  },
  title: {
    color: "#fff",
    fontSize: 24,
    fontWeight: "700",
  },
  meta: {
    color: "#94A3B8",
    marginTop: 8,
  },
  block: {
    backgroundColor: "#1F2937",
    borderRadius: 16,
    padding: 16,
    marginBottom: 12,
  },
  exerciseTitle: {
    color: "#fff",
    fontSize: 18,
    fontWeight: "700",
  },
  exerciseSubtitle: {
    color: "#60A5FA",
    marginTop: 4,
    marginBottom: 10,
  },
  setText: {
    color: "#E5E7EB",
    marginBottom: 6,
  },
});
