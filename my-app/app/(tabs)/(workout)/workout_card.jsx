import React from "react";
import { useRouter } from "expo-router";
import { StyleSheet, Text, TouchableOpacity } from "react-native";

export default function WorkoutCard({ workout }) {
  const router = useRouter();
  const formattedDate = workout.workoutDate
    ? new Date(workout.workoutDate).toLocaleDateString()
    : "Без даты";

  return (
    <TouchableOpacity
      style={styles.card}
      onPress={() =>
        router.push({
          pathname: "/(tabs)/(workout)/workout-details",
          params: { workout: JSON.stringify(workout) },
        })
      }
    >
      <Text style={styles.title}>{workout.name || `Тренировка #${workout.id}`}</Text>
      <Text style={styles.date}>Дата: {formattedDate}</Text>
      <Text style={styles.details}>
        Упражнений: {workout.workoutExercises?.length || 0}
      </Text>
      <Text style={styles.link}>Открыть тренировку</Text>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: "#2D2D2D",
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
  },
  title: {
    color: "#fff",
    fontSize: 18,
    fontWeight: "bold",
  },
  date: {
    color: "#bbb",
    fontSize: 14,
    marginTop: 4,
  },
  details: {
    color: "#ccc",
    fontSize: 14,
    marginTop: 4,
  },
  link: {
    color: "#60A5FA",
    marginTop: 10,
    fontWeight: "600",
  },
});
