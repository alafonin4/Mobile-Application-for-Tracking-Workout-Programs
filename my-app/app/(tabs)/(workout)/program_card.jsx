import React from "react";
import { useRouter } from "expo-router";
import { StyleSheet, Text, TouchableOpacity } from "react-native";

export default function ProgramCard({ program }) {
  const router = useRouter();

  return (
    <TouchableOpacity
      style={styles.card}
      onPress={() =>
        router.push({
          pathname: "/(tabs)/(workout)/program-details",
          params: { program: JSON.stringify(program) },
        })
      }
    >
      <Text style={styles.title}>{program.name}</Text>
      <Text style={styles.days}>Дней в программе: {program.trainingDays?.length || 0}</Text>
      <Text style={styles.description}>
        {program.description || "Описание пока не добавлено."}
      </Text>
      <Text style={styles.link}>Открыть программу</Text>
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
  days: {
    color: "#bbb",
    fontSize: 14,
    marginTop: 4,
  },
  description: {
    color: "#ccc",
    fontSize: 14,
    marginTop: 8,
  },
  link: {
    color: "#60A5FA",
    marginTop: 10,
    fontWeight: "600",
  },
});
