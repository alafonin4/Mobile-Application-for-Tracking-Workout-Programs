import React, { useCallback, useEffect, useMemo, useState } from "react";
import {
  ActivityIndicator,
  Alert,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { useRouter } from "expo-router";
import { Feather } from "@expo/vector-icons";

import { getApiErrorMessage } from "../../../api/client";
import { fetchExerciseProgress, fetchWorkoutProgress } from "../../../api/workout/fetchProgress";
import BarChart from "../../../components/charts/BarChart";
import LineChart from "../../../components/charts/LineChart";
import SpiderChart from "../../../components/charts/SpiderChart";
import { useUserId } from "../../../hooks/useUserId";

const PERIODS = [1, 3, 6];
const DEFAULT_MUSCLE_GROUPS = [
  "Грудь",
  "Спина",
  "Ноги",
  "Плечи",
  "Бицепс",
  "Трицепс",
  "Пресс",
  "Other",
];
const WEEKDAY_LABELS = ["Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"];

const formatShortDate = (value) => {
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return value;
  }
  return `${date.getDate()}.${date.getMonth() + 1}`;
};

const getWeekdayIndex = (value) => {
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return null;
  }

  const day = date.getDay();
  return day === 0 ? 6 : day - 1;
};

const buildMuscleGroupData = (items = []) => {
  const byGroup = new Map(
    (items ?? []).map((item) => [
      item.muscleGroup,
      {
        ...item,
        normalizedScore: Number(item.normalizedScore) || 0,
      },
    ])
  );

  const allGroups = [
    ...DEFAULT_MUSCLE_GROUPS,
    ...(items ?? [])
      .map((item) => item.muscleGroup)
      .filter((group) => group && !DEFAULT_MUSCLE_GROUPS.includes(group)),
  ];

  return allGroups.map((muscleGroup) => {
    const existing = byGroup.get(muscleGroup);
    return (
      existing ?? {
        muscleGroup,
        totalVolume: 0,
        totalSets: 0,
        totalReps: 0,
        progressPercent: 0,
        normalizedScore: 0,
        compositeScore: 0,
        averageIntensity: 0,
        peakEstimatedOneRepMax: 0,
      }
    );
  });
};

const buildWeekdayActivityData = (timeline = []) => {
  const totals = Array(7).fill(0);

  (timeline ?? []).forEach((point) => {
    const weekdayIndex = getWeekdayIndex(point.date);
    if (weekdayIndex == null) {
      return;
    }

    totals[weekdayIndex] += Number(point.overallVolume) || 0;
  });

  return WEEKDAY_LABELS.map((label, index) => ({
    label,
    value: Math.round(totals[index] * 100) / 100,
  }));
};

export default function WorkoutProgressScreen() {
  const router = useRouter();
  const [userId, , isUserIdLoaded] = useUserId();
  const [periodMonths, setPeriodMonths] = useState(1);
  const [overview, setOverview] = useState(null);
  const [exerciseProgress, setExerciseProgress] = useState(null);
  const [selectedView, setSelectedView] = useState("overall");
  const [selectedMuscleGroup, setSelectedMuscleGroup] = useState(null);
  const [selectedExerciseId, setSelectedExerciseId] = useState(null);
  const [isLoadingOverview, setIsLoadingOverview] = useState(true);
  const [isLoadingExercise, setIsLoadingExercise] = useState(false);

  const muscleGroupData = useMemo(
    () => buildMuscleGroupData(overview?.muscleGroupProgress ?? []),
    [overview?.muscleGroupProgress]
  );

  const weekdayActivityData = useMemo(
    () => buildWeekdayActivityData(overview?.timeline ?? []),
    [overview?.timeline]
  );

  const loadOverview = useCallback(async () => {
    if (!isUserIdLoaded || userId == null) {
      return;
    }

    setIsLoadingOverview(true);
    try {
      const data = await fetchWorkoutProgress(userId, periodMonths);
      setOverview(data);

      const nextMuscleGroups = buildMuscleGroupData(data.muscleGroupProgress ?? []);

      if (
        !selectedMuscleGroup ||
        !nextMuscleGroups.some((item) => item.muscleGroup === selectedMuscleGroup)
      ) {
        setSelectedMuscleGroup(nextMuscleGroups[0]?.muscleGroup ?? null);
      }

      if ((!selectedExerciseId || !data.exercises?.some((item) => item.exerciseId === selectedExerciseId))
        && data.exercises?.length) {
        setSelectedExerciseId(data.exercises[0].exerciseId);
      }
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось загрузить прогресс тренировок.")
      );
    } finally {
      setIsLoadingOverview(false);
    }
  }, [isUserIdLoaded, periodMonths, selectedExerciseId, selectedMuscleGroup, userId]);

  useEffect(() => {
    loadOverview();
  }, [loadOverview]);

  useEffect(() => {
    const loadExercise = async () => {
      if (!isUserIdLoaded || userId == null || selectedExerciseId == null) {
        return;
      }

      setIsLoadingExercise(true);
      try {
        const data = await fetchExerciseProgress(userId, selectedExerciseId, periodMonths);
        setExerciseProgress(data);
      } catch (error) {
        Alert.alert(
          "Ошибка",
          getApiErrorMessage(error, "Не удалось загрузить прогресс по упражнению.")
        );
      } finally {
        setIsLoadingExercise(false);
      }
    };

    loadExercise();
  }, [isUserIdLoaded, periodMonths, selectedExerciseId, userId]);

  const overviewSeries = useMemo(() => {
    if (!overview?.timeline?.length) {
      return [];
    }

    return overview.timeline.map((point) => ({
      label: formatShortDate(point.date),
      value:
        selectedView === "overall"
          ? point.overallVolume
          : point.muscleGroupVolumes?.[selectedMuscleGroup] ?? 0,
    }));
  }, [overview, selectedMuscleGroup, selectedView]);

  const exerciseSeries = useMemo(() => {
    if (!exerciseProgress?.timeline?.length) {
      return [];
    }

    return exerciseProgress.timeline.map((point) => ({
      label: formatShortDate(point.date),
      value: point.maxWeight,
    }));
  }, [exerciseProgress]);

  if (isLoadingOverview) {
    return (
      <View style={styles.centered}>
        <ActivityIndicator size="large" color="#38BDF8" />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <View style={styles.topBar}>
        <TouchableOpacity style={styles.backButton} onPress={() => router.back()}>
          <Feather name="chevron-left" size={20} color="#fff" />
        </TouchableOpacity>
        <Text style={styles.screenTitle}>Прогресс</Text>
        <View style={styles.backButtonPlaceholder} />
      </View>

      <ScrollView contentContainerStyle={styles.content}>
        <View style={styles.periodRow}>
          {PERIODS.map((period) => (
            <TouchableOpacity
              key={period}
              style={[styles.periodChip, periodMonths === period && styles.periodChipActive]}
              onPress={() => setPeriodMonths(period)}
            >
              <Text
                style={[
                  styles.periodChipText,
                  periodMonths === period && styles.periodChipTextActive,
                ]}
              >
                {period} мес.
              </Text>
            </TouchableOpacity>
          ))}
        </View>

        <View style={styles.summaryRow}>
          <View style={styles.summaryCard}>
            <Text style={styles.summaryLabel}>Тренировок</Text>
            <Text style={styles.summaryValue}>{overview?.summary?.workoutsCount ?? 0}</Text>
          </View>
          <View style={styles.summaryCard}>
            <Text style={styles.summaryLabel}>Общий объем</Text>
            <Text style={styles.summaryValue}>
              {Math.round(overview?.summary?.totalVolume ?? 0)}
            </Text>
          </View>
          <View style={styles.summaryCard}>
            <Text style={styles.summaryLabel}>Прогресс</Text>
            <Text style={styles.summaryValue}>
              {Math.round(overview?.summary?.progressPercent ?? 0)}%
            </Text>
          </View>
        </View>

        <SpiderChart data={muscleGroupData} />

        <BarChart
          title="Активность по дням недели"
          data={weekdayActivityData}
          color="#A855F7"
        />

        <View style={styles.sectionCard}>
          <Text style={styles.sectionTitle}>Динамика за период</Text>
          <View style={styles.toggleRow}>
            <TouchableOpacity
              style={[styles.toggleChip, selectedView === "overall" && styles.toggleChipActive]}
              onPress={() => setSelectedView("overall")}
            >
              <Text style={styles.toggleText}>Общий прогресс</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[styles.toggleChip, selectedView === "muscle" && styles.toggleChipActive]}
              onPress={() => setSelectedView("muscle")}
            >
              <Text style={styles.toggleText}>По мышечной группе</Text>
            </TouchableOpacity>
          </View>

          {selectedView === "muscle" ? (
            <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.chipsRow}>
              {muscleGroupData.map((item) => (
                <TouchableOpacity
                  key={item.muscleGroup}
                  style={[
                    styles.muscleChip,
                    selectedMuscleGroup === item.muscleGroup && styles.muscleChipActive,
                  ]}
                  onPress={() => setSelectedMuscleGroup(item.muscleGroup)}
                >
                  <Text style={styles.muscleChipText}>{item.muscleGroup}</Text>
                </TouchableOpacity>
              ))}
            </ScrollView>
          ) : null}
        </View>

        <LineChart
          title={
            selectedView === "overall"
              ? "Общий прогресс по тренировочному объему"
              : `Прогресс по группе ${selectedMuscleGroup ?? ""}`
          }
          data={overviewSeries}
          color={selectedView === "overall" ? "#38BDF8" : "#22C55E"}
        />

        <View style={styles.sectionCard}>
          <Text style={styles.sectionTitle}>Прогресс по конкретному упражнению</Text>
          <ScrollView horizontal showsHorizontalScrollIndicator={false} contentContainerStyle={styles.chipsRow}>
            {(overview?.exercises ?? []).map((exercise) => (
              <TouchableOpacity
                key={exercise.exerciseId}
                style={[
                  styles.exerciseChip,
                  selectedExerciseId === exercise.exerciseId && styles.exerciseChipActive,
                ]}
                onPress={() => setSelectedExerciseId(exercise.exerciseId)}
              >
                <Text style={styles.exerciseChipTitle}>{exercise.exerciseName}</Text>
                <Text style={styles.exerciseChipSubtitle}>{exercise.muscleGroup}</Text>
              </TouchableOpacity>
            ))}
          </ScrollView>
        </View>

        {isLoadingExercise ? (
          <View style={styles.exerciseLoading}>
            <ActivityIndicator size="small" color="#38BDF8" />
          </View>
        ) : (
          <>
            <LineChart
              title={
                exerciseProgress?.exerciseName
                  ? `${exerciseProgress.exerciseName}: пик веса`
                  : "График упражнения"
              }
              data={exerciseSeries}
              color="#F97316"
              suffix=" кг"
            />

            <View style={styles.exerciseStats}>
              <Text style={styles.exerciseStatText}>
                Прогресс: {Math.round(exerciseProgress?.progressPercent ?? 0)}%
              </Text>
              <Text style={styles.exerciseStatText}>
                Макс. вес: {Math.round(exerciseProgress?.maxWeight ?? 0)} кг
              </Text>
              <Text style={styles.exerciseStatText}>
                Повторения: {exerciseProgress?.totalReps ?? 0}
              </Text>
            </View>
          </>
        )}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#020617",
    paddingTop: 40,
  },
  centered: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "#020617",
  },
  content: {
    padding: 16,
    gap: 16,
    paddingBottom: 40,
  },
  topBar: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    paddingHorizontal: 16,
    marginBottom: 12,
  },
  backButton: {
    width: 36,
    height: 36,
    borderRadius: 18,
    backgroundColor: "#111827",
    alignItems: "center",
    justifyContent: "center",
  },
  backButtonPlaceholder: {
    width: 36,
    height: 36,
  },
  screenTitle: {
    color: "#F8FAFC",
    fontSize: 22,
    fontWeight: "700",
  },
  periodRow: {
    flexDirection: "row",
    justifyContent: "center",
    gap: 10,
  },
  periodChip: {
    backgroundColor: "#111827",
    borderRadius: 999,
    paddingHorizontal: 14,
    paddingVertical: 10,
  },
  periodChipActive: {
    backgroundColor: "#2563EB",
  },
  periodChipText: {
    color: "#CBD5E1",
    fontWeight: "600",
  },
  periodChipTextActive: {
    color: "#FFFFFF",
  },
  summaryRow: {
    flexDirection: "row",
    gap: 10,
  },
  summaryCard: {
    flex: 1,
    backgroundColor: "#111827",
    borderRadius: 18,
    padding: 14,
  },
  summaryLabel: {
    color: "#94A3B8",
    fontSize: 12,
  },
  summaryValue: {
    color: "#F8FAFC",
    fontSize: 22,
    fontWeight: "700",
    marginTop: 8,
  },
  sectionCard: {
    backgroundColor: "#111827",
    borderRadius: 20,
    padding: 16,
  },
  sectionTitle: {
    color: "#F8FAFC",
    fontSize: 18,
    fontWeight: "700",
    marginBottom: 12,
  },
  toggleRow: {
    flexDirection: "row",
    gap: 10,
  },
  toggleChip: {
    flex: 1,
    borderRadius: 14,
    backgroundColor: "#1E293B",
    paddingVertical: 12,
    paddingHorizontal: 10,
    alignItems: "center",
  },
  toggleChipActive: {
    backgroundColor: "#2563EB",
  },
  toggleText: {
    color: "#E2E8F0",
    fontSize: 13,
    fontWeight: "600",
    textAlign: "center",
  },
  chipsRow: {
    gap: 10,
    paddingTop: 12,
    paddingRight: 8,
  },
  muscleChip: {
    backgroundColor: "#1E293B",
    borderRadius: 999,
    paddingHorizontal: 14,
    paddingVertical: 10,
  },
  muscleChipActive: {
    backgroundColor: "#16A34A",
  },
  muscleChipText: {
    color: "#E2E8F0",
    fontWeight: "600",
  },
  exerciseChip: {
    width: 150,
    backgroundColor: "#1E293B",
    borderRadius: 16,
    padding: 12,
  },
  exerciseChipActive: {
    backgroundColor: "#EA580C",
  },
  exerciseChipTitle: {
    color: "#F8FAFC",
    fontWeight: "700",
    marginBottom: 6,
  },
  exerciseChipSubtitle: {
    color: "#CBD5E1",
    fontSize: 12,
  },
  exerciseLoading: {
    backgroundColor: "#111827",
    borderRadius: 20,
    padding: 20,
    alignItems: "center",
  },
  exerciseStats: {
    backgroundColor: "#111827",
    borderRadius: 20,
    padding: 16,
    gap: 8,
  },
  exerciseStatText: {
    color: "#E2E8F0",
    fontSize: 14,
  },
});
