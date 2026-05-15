import React from "react";
import { StyleSheet, Text, View } from "react-native";

const formatRecordValue = (value) => {
  if (!Number.isFinite(Number(value))) {
    return "0";
  }

  const numericValue = Number(value);
  return Number.isInteger(numericValue)
    ? String(numericValue)
    : String(Math.round(numericValue * 10) / 10);
};

export default function PersonalRecordsCard({ records = [] }) {
  return (
    <View style={styles.card}>
      <Text style={styles.title}>Персональные рекорды</Text>

      {records.length ? (
        records.map((record, index) => (
          <View
            key={record.code}
            style={[styles.recordRow, index === 0 && styles.firstRecordRow]}
          >
            <View style={styles.recordMain}>
              <Text style={styles.recordTitle}>{record.title}</Text>
              <Text style={styles.recordSubtitle}>
                {record.exerciseName}
                {record.subtitle ? ` • ${record.subtitle}` : ""}
              </Text>
            </View>

            <View style={styles.recordValueWrap}>
              <Text style={styles.recordValue}>
                {formatRecordValue(record.value)} {record.unit}
              </Text>
              <Text style={styles.recordDate}>{record.date ?? "—"}</Text>
            </View>
          </View>
        ))
      ) : (
        <Text style={styles.emptyText}>Рекорды появятся после тренировок.</Text>
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
  title: {
    fontSize: 20,
    fontWeight: "700",
    color: "#111827",
    marginBottom: 12,
  },
  recordRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    gap: 12,
    paddingVertical: 12,
    borderTopWidth: StyleSheet.hairlineWidth,
    borderTopColor: "#E5E7EB",
  },
  firstRecordRow: {
    borderTopWidth: 0,
    paddingTop: 0,
  },
  recordMain: {
    flex: 1,
  },
  recordTitle: {
    color: "#111827",
    fontWeight: "700",
    fontSize: 15,
  },
  recordSubtitle: {
    marginTop: 4,
    color: "#6B7280",
    lineHeight: 18,
  },
  recordValueWrap: {
    alignItems: "flex-end",
  },
  recordValue: {
    color: "#1D4ED8",
    fontWeight: "700",
    fontSize: 16,
  },
  recordDate: {
    marginTop: 4,
    color: "#6B7280",
    fontSize: 12,
  },
  emptyText: {
    color: "#6B7280",
    lineHeight: 20,
  },
});
