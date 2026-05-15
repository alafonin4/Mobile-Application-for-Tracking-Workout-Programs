import React, { useMemo } from "react";
import { StyleSheet, Text, View } from "react-native";

export default function BarChart({
  title,
  data = [],
  color = "#38BDF8",
  suffix = "",
}) {
  const values = useMemo(() => data.map((item) => Number(item.value) || 0), [data]);
  const maxValue = Math.max(...values, 1);

  return (
    <View style={styles.card}>
      <Text style={styles.title}>{title}</Text>
      {!data.length ? (
        <Text style={styles.emptyText}>Нет данных за выбранный период.</Text>
      ) : (
        <>
          <View style={styles.scaleRow}>
            <Text style={styles.scaleText}>{Math.round(maxValue)}{suffix}</Text>
            <Text style={styles.scaleText}>{Math.round(maxValue / 2)}{suffix}</Text>
            <Text style={styles.scaleText}>0{suffix}</Text>
          </View>

          <View style={styles.chartArea}>
            <View style={styles.gridTop} />
            <View style={styles.gridMiddle} />
            <View style={styles.gridBottom} />

            <View style={styles.barsRow}>
              {data.map((item, index) => {
                const value = Number(item.value) || 0;
                const heightPercent = maxValue === 0 ? 0 : (value / maxValue) * 100;

                return (
                  <View key={`${item.label}-${index}`} style={styles.barGroup}>
                    <View style={styles.barTrack}>
                      <View
                        style={[
                          styles.bar,
                          {
                            backgroundColor: color,
                            height: `${heightPercent}%`,
                          },
                        ]}
                      />
                    </View>
                    <Text style={styles.valueText}>{Math.round(value)}</Text>
                  </View>
                );
              })}
            </View>
          </View>

          <View style={styles.labelsRow}>
            {data.map((item, index) => (
              <Text key={`${item.label}-${index}`} style={styles.labelText}>
                {item.label}
              </Text>
            ))}
          </View>
        </>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    backgroundColor: "#111827",
    borderRadius: 20,
    padding: 16,
  },
  title: {
    color: "#F8FAFC",
    fontSize: 18,
    fontWeight: "700",
    marginBottom: 12,
  },
  emptyText: {
    color: "#94A3B8",
    fontSize: 14,
  },
  scaleRow: {
    height: 180,
    position: "absolute",
    left: 12,
    top: 44,
    justifyContent: "space-between",
    zIndex: 2,
  },
  scaleText: {
    color: "#64748B",
    fontSize: 11,
  },
  chartArea: {
    height: 180,
    marginLeft: 34,
    position: "relative",
  },
  gridTop: {
    position: "absolute",
    left: 0,
    right: 0,
    top: 12,
    height: 1,
    backgroundColor: "#1E293B",
  },
  gridMiddle: {
    position: "absolute",
    left: 0,
    right: 0,
    top: 90,
    height: 1,
    backgroundColor: "#1E293B",
  },
  gridBottom: {
    position: "absolute",
    left: 0,
    right: 0,
    bottom: 12,
    height: 1,
    backgroundColor: "#1E293B",
  },
  barsRow: {
    flexDirection: "row",
    alignItems: "flex-end",
    justifyContent: "space-between",
    height: 156,
    paddingTop: 12,
    paddingBottom: 12,
    gap: 8,
  },
  barGroup: {
    flex: 1,
    alignItems: "center",
    justifyContent: "flex-end",
  },
  barTrack: {
    width: "100%",
    maxWidth: 28,
    height: 120,
    borderRadius: 999,
    backgroundColor: "#1E293B",
    overflow: "hidden",
    justifyContent: "flex-end",
  },
  bar: {
    width: "100%",
    minHeight: 0,
    borderRadius: 999,
  },
  valueText: {
    color: "#CBD5E1",
    fontSize: 10,
    marginTop: 8,
  },
  labelsRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginLeft: 34,
    marginTop: 8,
    gap: 8,
  },
  labelText: {
    flex: 1,
    color: "#94A3B8",
    fontSize: 10,
    textAlign: "center",
  },
});
