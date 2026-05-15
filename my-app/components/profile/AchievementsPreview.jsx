import React, { useMemo } from "react";
import { StyleSheet, Text, TouchableOpacity, View } from "react-native";

const pickPreviewItems = (items = []) => {
  const unlocked = items.filter((item) => item.unlocked);
  if (unlocked.length) {
    return unlocked.slice(0, 3);
  }

  return [...items]
    .sort((left, right) => (right.progressPercent ?? 0) - (left.progressPercent ?? 0))
    .slice(0, 3);
};

export default function AchievementsPreview({
  achievements = [],
  unlockedCount = 0,
  totalCount = 0,
  onPressAll,
  title = "Достижения",
}) {
  const previewItems = useMemo(() => pickPreviewItems(achievements), [achievements]);

  return (
    <View style={styles.card}>
      <View style={styles.headerRow}>
        <View>
          <Text style={styles.title}>{title}</Text>
          <Text style={styles.subtitle}>
            Открыто {unlockedCount} из {totalCount}
          </Text>
        </View>

        {onPressAll ? (
          <TouchableOpacity onPress={onPressAll}>
            <Text style={styles.link}>Все</Text>
          </TouchableOpacity>
        ) : null}
      </View>

      {previewItems.length ? (
        previewItems.map((item) => (
          <View
            key={item.code}
            style={[styles.badge, item.unlocked ? styles.badgeUnlocked : styles.badgeLocked]}
          >
            <View style={styles.badgeMain}>
              <Text style={styles.badgeTitle}>{item.title}</Text>
              <Text style={styles.badgeDescription}>{item.description}</Text>
            </View>
            <Text style={[styles.badgeMeta, item.unlocked ? styles.badgeMetaUnlocked : styles.badgeMetaLocked]}>
              {item.unlocked ? "Открыто" : `${Math.round(item.progressPercent ?? 0)}%`}
            </Text>
          </View>
        ))
      ) : (
        <Text style={styles.emptyText}>
          Достижения появятся после первых тренировок.
        </Text>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: "#FFFFFF",
    borderRadius: 20,
    padding: 18,
  },
  headerRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "flex-start",
    marginBottom: 12,
  },
  title: {
    fontSize: 20,
    fontWeight: "700",
    color: "#111827",
  },
  subtitle: {
    marginTop: 4,
    color: "#6B7280",
  },
  link: {
    color: "#2563EB",
    fontWeight: "700",
  },
  badge: {
    borderRadius: 16,
    padding: 14,
    marginTop: 10,
    flexDirection: "row",
    justifyContent: "space-between",
    gap: 12,
  },
  badgeUnlocked: {
    backgroundColor: "#ECFDF5",
  },
  badgeLocked: {
    backgroundColor: "#F3F4F6",
  },
  badgeMain: {
    flex: 1,
  },
  badgeTitle: {
    color: "#111827",
    fontWeight: "700",
    fontSize: 15,
  },
  badgeDescription: {
    marginTop: 4,
    color: "#4B5563",
    lineHeight: 18,
  },
  badgeMeta: {
    fontWeight: "700",
    alignSelf: "center",
  },
  badgeMetaUnlocked: {
    color: "#047857",
  },
  badgeMetaLocked: {
    color: "#6B7280",
  },
  emptyText: {
    color: "#6B7280",
    lineHeight: 20,
  },
});
