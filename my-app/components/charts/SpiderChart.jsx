import React from "react";
import { StyleSheet, Text, View } from "react-native";

const clamp = (value, min, max) => Math.min(Math.max(value, min), max);

const toPoint = (index, total, radius, center, valueRadius) => {
  const angle = -Math.PI / 2 + (Math.PI * 2 * index) / total;
  return {
    angle,
    axisX: center + Math.cos(angle) * radius,
    axisY: center + Math.sin(angle) * radius,
    valueX: center + Math.cos(angle) * valueRadius,
    valueY: center + Math.sin(angle) * valueRadius,
  };
};

const SegmentLine = ({ x1, y1, x2, y2, color, thickness = 2 }) => {
  const length = Math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2);
  const angle = Math.atan2(y2 - y1, x2 - x1);
  const left = (x1 + x2) / 2 - length / 2;
  const top = (y1 + y2) / 2 - thickness / 2;

  return (
    <View
      style={[
        styles.segment,
        {
          width: length,
          height: thickness,
          backgroundColor: color,
          left,
          top,
          transform: [{ rotate: `${angle}rad` }],
        },
      ]}
    />
  );
};

export default function SpiderChart({ data = [] }) {
  const size = 280;
  const center = size / 2;
  const radius = 90;

  if (!data.length) {
    return (
      <View style={[styles.container, styles.emptyContainer]}>
        <Text style={styles.emptyText}>Нет данных по мышечным группам.</Text>
      </View>
    );
  }

  const points = data.map((item, index) => {
    const valueRadius = (clamp(item.normalizedScore ?? 0, 0, 100) / 100) * radius;
    return {
      ...item,
      ...toPoint(index, data.length, radius, center, valueRadius),
    };
  });

  return (
    <View style={styles.wrapper}>
      <Text style={styles.title}>Spider Diagram по мышечным группам</Text>
      <View style={[styles.container, { width: size, height: size }]}>
        {points.map((point) => (
          <SegmentLine
            key={`axis-${point.muscleGroup}`}
            x1={center}
            y1={center}
            x2={point.axisX}
            y2={point.axisY}
            color="#334155"
            thickness={1}
          />
        ))}

        {points.map((point, index) => {
          const nextPoint = points[(index + 1) % points.length];
          return (
            <SegmentLine
              key={`shape-${point.muscleGroup}`}
              x1={point.valueX}
              y1={point.valueY}
              x2={nextPoint.valueX}
              y2={nextPoint.valueY}
              color="#38BDF8"
              thickness={3}
            />
          );
        })}

        {points.map((point) => (
          <View
            key={`dot-${point.muscleGroup}`}
            style={[
              styles.dot,
              {
                left: point.valueX - 5,
                top: point.valueY - 5,
              },
            ]}
          />
        ))}

        <View style={styles.centerDot} />

        {points.map((point) => (
          <View
            key={`label-${point.muscleGroup}`}
            style={[
              styles.labelWrap,
              {
                left: clamp(point.axisX - 42, 0, size - 84),
                top: clamp(point.axisY - 12, 0, size - 24),
              },
            ]}
          >
            <Text style={styles.labelText}>{point.muscleGroup}</Text>
          </View>
        ))}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  wrapper: {
    backgroundColor: "#111827",
    borderRadius: 20,
    padding: 16,
    alignItems: "center",
  },
  title: {
    color: "#F8FAFC",
    fontSize: 18,
    fontWeight: "700",
    marginBottom: 12,
  },
  container: {
    position: "relative",
    justifyContent: "center",
    alignItems: "center",
  },
  emptyContainer: {
    borderRadius: 20,
    backgroundColor: "#111827",
    minHeight: 160,
  },
  emptyText: {
    color: "#94A3B8",
    fontSize: 14,
  },
  segment: {
    position: "absolute",
  },
  dot: {
    position: "absolute",
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: "#38BDF8",
  },
  centerDot: {
    position: "absolute",
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: "#F8FAFC",
    left: 135,
    top: 135,
  },
  labelWrap: {
    position: "absolute",
    width: 84,
    alignItems: "center",
  },
  labelText: {
    color: "#CBD5E1",
    fontSize: 11,
    textAlign: "center",
  },
});
