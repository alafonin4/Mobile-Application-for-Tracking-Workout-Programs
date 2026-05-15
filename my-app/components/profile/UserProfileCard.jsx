import React from "react";
import { Image, StyleSheet, Text, View } from "react-native";

import { getFitnessGoalLabel } from "../../utils/profile";

export default function UserProfileCard({ user, fallbackId, subtitle }) {
  const fullName = `${user?.firstName ?? ""} ${user?.lastName ?? ""}`.trim();
  const title = fullName || `Пользователь #${fallbackId}`;
  const initials = fullName
    ? fullName
        .split(" ")
        .filter(Boolean)
        .slice(0, 2)
        .map((part) => part[0]?.toUpperCase() ?? "")
        .join("")
    : "U";

  return (
    <View style={styles.headerCard}>
      {user?.avatarUrl ? (
        <Image source={{ uri: user.avatarUrl }} style={styles.avatar} />
      ) : (
        <View style={styles.avatarFallback}>
          <Text style={styles.avatarFallbackText}>{initials}</Text>
        </View>
      )}

      <Text style={styles.name}>{title}</Text>
      <Text style={styles.email}>{user?.email ?? "Email не указан"}</Text>

      <View style={styles.metaRow}>
        <Text style={styles.meta}>Вес: {user?.bodyWeight ?? 0} кг</Text>
        <Text style={styles.goalChip}>{getFitnessGoalLabel(user?.fitnessGoal)}</Text>
      </View>

      <Text style={styles.bio}>{user?.bio || "Пользователь пока не добавил описание."}</Text>
      {subtitle ? <Text style={styles.subtitle}>{subtitle}</Text> : null}
    </View>
  );
}

const styles = StyleSheet.create({
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
  avatar: {
    width: 88,
    height: 88,
    borderRadius: 44,
    marginBottom: 16,
    alignSelf: "center",
  },
  avatarFallback: {
    width: 88,
    height: 88,
    borderRadius: 44,
    marginBottom: 16,
    alignSelf: "center",
    backgroundColor: "#DBEAFE",
    alignItems: "center",
    justifyContent: "center",
  },
  avatarFallbackText: {
    fontSize: 30,
    fontWeight: "700",
    color: "#1D4ED8",
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
  metaRow: {
    flexDirection: "row",
    flexWrap: "wrap",
    alignItems: "center",
    gap: 10,
    marginBottom: 12,
  },
  meta: {
    fontSize: 15,
    color: "#1D4ED8",
  },
  goalChip: {
    backgroundColor: "#EEF2FF",
    color: "#3730A3",
    fontSize: 13,
    fontWeight: "700",
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 999,
    overflow: "hidden",
  },
  bio: {
    fontSize: 15,
    lineHeight: 22,
    color: "#374151",
  },
  subtitle: {
    fontSize: 13,
    lineHeight: 20,
    color: "#6B7280",
    marginTop: 12,
  },
});
