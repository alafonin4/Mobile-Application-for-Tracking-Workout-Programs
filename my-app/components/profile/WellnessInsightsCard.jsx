import React, { useMemo } from "react";
import { StyleSheet, Text, View } from "react-native";

import { getFitnessGoalLabel } from "../../utils/profile";

const getRecoveryAccent = (score) => {
  if ((score ?? 0) >= 80) {
    return "#15803D";
  }
  if ((score ?? 0) >= 60) {
    return "#1D4ED8";
  }
  if ((score ?? 0) >= 45) {
    return "#B45309";
  }
  return "#B91C1C";
};

const buildBalanceSummary = (items = []) => {
  const sorted = [...items].sort((left, right) => (right.sharePercent ?? 0) - (left.sharePercent ?? 0));
  return {
    strongest: sorted[0] ?? null,
    weakest: sorted[sorted.length - 1] ?? null,
  };
};

export default function WellnessInsightsCard({
  fitnessGoal,
  recoveryScore,
  recoveryStatus,
  muscleBalance = [],
}) {
  const { strongest, weakest } = useMemo(() => buildBalanceSummary(muscleBalance), [muscleBalance]);
  const recoveryAccent = getRecoveryAccent(recoveryScore);

  return (
    <View style={styles.card}>
      <Text style={styles.title}>Персонализация</Text>

      <View style={styles.metricRow}>
        <View style={styles.metricCard}>
          <Text style={styles.metricLabel}>Цель</Text>
          <Text style={styles.metricValue}>{getFitnessGoalLabel(fitnessGoal)}</Text>
        </View>

        <View style={styles.metricCard}>
          <Text style={styles.metricLabel}>Recovery score</Text>
          <Text style={[styles.metricValue, { color: recoveryAccent }]}>
            {recoveryScore ?? "—"}
          </Text>
        </View>
      </View>

      {recoveryStatus ? <Text style={styles.recoveryText}>{recoveryStatus}</Text> : null}

      {strongest || weakest ? (
        <View style={styles.balanceCard}>
          <Text style={styles.balanceTitle}>Баланс нагрузки</Text>

          {strongest ? (
            <Text style={styles.balanceText}>
              Сильнее всего нагружена группа {strongest.muscleGroup}: {Math.round(strongest.sharePercent ?? 0)}%
              общего объёма.
            </Text>
          ) : null}

          {weakest ? (
            <Text style={styles.balanceText}>
              Меньше всего внимания получает {weakest.muscleGroup}: {Math.round(weakest.sharePercent ?? 0)}%.
            </Text>
          ) : null}
        </View>
      ) : null}
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: "#FFFFFF",
    borderRadius: 20,
    padding: 18,
  },
  title: {
    fontSize: 20,
    fontWeight: "700",
    color: "#111827",
  },
  metricRow: {
    flexDirection: "row",
    gap: 10,
    marginTop: 12,
  },
  metricCard: {
    flex: 1,
    borderRadius: 16,
    backgroundColor: "#F8FAFC",
    padding: 14,
  },
  metricLabel: {
    color: "#64748B",
    fontSize: 12,
  },
  metricValue: {
    color: "#0F172A",
    fontSize: 18,
    fontWeight: "700",
    marginTop: 8,
  },
  recoveryText: {
    color: "#475569",
    marginTop: 12,
    lineHeight: 20,
  },
  balanceCard: {
    marginTop: 14,
    borderRadius: 16,
    backgroundColor: "#F8FAFC",
    padding: 14,
    gap: 8,
  },
  balanceTitle: {
    color: "#0F172A",
    fontWeight: "700",
  },
  balanceText: {
    color: "#475569",
    lineHeight: 20,
  },
});
