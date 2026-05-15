import React, { useMemo, useState } from "react";
import { StyleSheet, Text, View } from "react-native";

const SegmentLine = ({ x1, y1, x2, y2, color }) => {
  const length = Math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2);
  const angle = Math.atan2(y2 - y1, x2 - x1);
  const left = (x1 + x2) / 2 - length / 2;
  const top = (y1 + y2) / 2 - 1;

  return (
    <View
      style={[
        styles.segment,
        {
          width: length,
          left,
          top,
          backgroundColor: color,
          transform: [{ rotate: `${angle}rad` }],
        },
      ]}
    />
  );
};

export default function LineChart({
  title,
  data = [],
  color = "#38BDF8",
  suffix = "",
}) {
  const [chartWidth, setChartWidth] = useState(0);
  const chartHeight = 180;

  const values = useMemo(() => data.map((item) => Number(item.value) || 0), [data]);
  const maxValue = Math.max(...values, 1);
  const labelStep = data.length > 6 ? Math.ceil(data.length / 6) : 1;

  const points = useMemo(() => {
    if (!chartWidth || !data.length) {
      return [];
    }

    const usableWidth = Math.max(chartWidth - 12, 1);
    const stepX = data.length === 1 ? 0 : usableWidth / (data.length - 1);

    return data.map((item, index) => ({
      ...item,
      x: 6 + stepX * index,
      y: 12 + (chartHeight - 24) * (1 - (Number(item.value) || 0) / maxValue),
    }));
  }, [chartWidth, data, maxValue]);

  const onLayout = (event) => {
    setChartWidth(event.nativeEvent.layout.width);
  };

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

          <View style={styles.chartArea} onLayout={onLayout}>
            <View style={styles.gridTop} />
            <View style={styles.gridMiddle} />
            <View style={styles.gridBottom} />

            {points.map((point, index) => {
              const nextPoint = points[index + 1];
              if (!nextPoint) {
                return null;
              }

              return (
                <SegmentLine
                  key={`segment-${index}`}
                  x1={point.x}
                  y1={point.y}
                  x2={nextPoint.x}
                  y2={nextPoint.y}
                  color={color}
                />
              );
            })}

            {points.map((point, index) => (
              <View
                key={`point-${index}`}
                style={[
                  styles.point,
                  {
                    left: point.x - 4,
                    top: point.y - 4,
                    backgroundColor: color,
                  },
                ]}
              />
            ))}
          </View>

          <View style={styles.labelsRow}>
            {data.map((item, index) => (
              <Text key={`${item.label}-${index}`} style={styles.labelText}>
                {index % labelStep === 0 ? item.label : ""}
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
  segment: {
    position: "absolute",
    height: 2,
  },
  point: {
    position: "absolute",
    width: 8,
    height: 8,
    borderRadius: 4,
  },
  labelsRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginLeft: 34,
    marginTop: 8,
  },
  labelText: {
    flex: 1,
    color: "#94A3B8",
    fontSize: 10,
    textAlign: "center",
  },
});
