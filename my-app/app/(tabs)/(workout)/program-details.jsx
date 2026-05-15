import React, { useEffect, useMemo, useState } from "react";
import {
  ActivityIndicator,
  ScrollView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { useLocalSearchParams, useRouter } from "expo-router";

import { getApiErrorMessage } from "../../../api/client";
import { fetchProgramAdaptation } from "../../../api/workout/fetchPersonalization";
import { useUserId } from "../../../hooks/useUserId";

const recommendationMeta = {
  INCREASE_WEIGHT: {
    color: "#DCFCE7",
    accent: "#15803D",
  },
  INCREASE_REPS: {
    color: "#DBEAFE",
    accent: "#1D4ED8",
  },
  REDUCE_LOAD: {
    color: "#FEE2E2",
    accent: "#B91C1C",
  },
  DELOAD_RETURN: {
    color: "#FEF3C7",
    accent: "#B45309",
  },
};

const formatNumber = (value) => {
  if (!Number.isFinite(Number(value))) {
    return "0";
  }

  const numericValue = Number(value);
  return Number.isInteger(numericValue)
    ? String(numericValue)
    : String(Math.round(numericValue * 10) / 10);
};

const renderSuggestionChange = (label, currentValue, nextValue, unit = "") => {
  if (currentValue == null && nextValue == null) {
    return null;
  }

  return `${label}: ${formatNumber(currentValue)} -> ${formatNumber(nextValue)}${unit ? ` ${unit}` : ""}`;
};

export default function ProgramDetailsScreen() {
  const router = useRouter();
  const params = useLocalSearchParams();
  const [userId, , isUserIdLoaded] = useUserId();
  const [adaptation, setAdaptation] = useState(null);
  const [adaptationError, setAdaptationError] = useState("");
  const [isAdaptationLoading, setIsAdaptationLoading] = useState(true);

  const program = useMemo(() => {
    try {
      return params.program ? JSON.parse(params.program) : null;
    } catch (error) {
      return null;
    }
  }, [params.program]);

  useEffect(() => {
    let isMounted = true;

    const loadAdaptation = async () => {
      if (!program?.id || !isUserIdLoaded) {
        if (isUserIdLoaded) {
          setIsAdaptationLoading(false);
        }
        return;
      }

      if (userId == null) {
        if (isMounted) {
          setAdaptation(null);
          setAdaptationError("Не удалось определить пользователя для персонализации.");
          setIsAdaptationLoading(false);
        }
        return;
      }

      setIsAdaptationLoading(true);
      setAdaptationError("");

      try {
        const response = await fetchProgramAdaptation(userId, program.id);
        if (isMounted) {
          setAdaptation(response);
        }
      } catch (error) {
        if (isMounted) {
          setAdaptation(null);
          setAdaptationError(
            getApiErrorMessage(error, "Не удалось загрузить рекомендации по программе.")
          );
        }
      } finally {
        if (isMounted) {
          setIsAdaptationLoading(false);
        }
      }
    };

    loadAdaptation();

    return () => {
      isMounted = false;
    };
  }, [isUserIdLoaded, program?.id, userId]);

  if (!program) {
    return (
      <View style={styles.emptyContainer}>
        <Text style={styles.emptyText}>Не удалось открыть программу.</Text>
      </View>
    );
  }

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <View style={styles.headerCard}>
        <Text style={styles.title}>{program.name}</Text>
        <Text style={styles.description}>
          {program.description || "Описание пока не добавлено."}
        </Text>
        <Text style={styles.meta}>
          Тренировочных дней: {program.trainingDays?.length ?? 0}
        </Text>

        <TouchableOpacity
          style={styles.editButton}
          onPress={() =>
            router.push({
              pathname: "/(tabs)/(workout)/edit-program",
              params: { program: JSON.stringify(program) },
            })
          }
        >
          <Text style={styles.editButtonText}>Редактировать программу</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.adaptationCard}>
        <Text style={styles.adaptationTitle}>Интеллектуальная адаптация</Text>

        {isAdaptationLoading ? (
          <View style={styles.loaderRow}>
            <ActivityIndicator size="small" color="#60A5FA" />
            <Text style={styles.loaderText}>Анализируем историю тренировок...</Text>
          </View>
        ) : adaptationError ? (
          <Text style={styles.errorText}>{adaptationError}</Text>
        ) : (
          <>
            <Text style={styles.readinessText}>
              {adaptation?.readinessMessage ??
                "После первых тренировок здесь появятся персональные рекомендации."}
            </Text>

            {adaptation?.daysSinceLastWorkout != null ? (
              <Text style={styles.daysText}>
                Дней с последней тренировки: {adaptation.daysSinceLastWorkout}
              </Text>
            ) : null}

            {adaptation?.suggestions?.length ? (
              adaptation.suggestions.map((suggestion, index) => {
                const meta =
                  recommendationMeta[suggestion.recommendationType] ??
                  recommendationMeta.INCREASE_REPS;

                return (
                  <View
                    key={`${suggestion.exerciseId ?? index}-${suggestion.recommendationType}`}
                    style={[styles.suggestionCard, { backgroundColor: meta.color }]}
                  >
                    <Text style={[styles.suggestionTitle, { color: meta.accent }]}>
                      {suggestion.title}
                    </Text>
                    <Text style={styles.suggestionExercise}>
                      {suggestion.dayIdentifier} • {suggestion.exerciseName}
                    </Text>
                    <Text style={styles.suggestionReason}>{suggestion.reason}</Text>

                    <View style={styles.suggestionMetrics}>
                      {renderSuggestionChange(
                        "Вес",
                        suggestion.currentRecommendedWeight,
                        suggestion.suggestedWeight,
                        "кг"
                      ) ? (
                        <Text style={styles.suggestionMetric}>
                          {renderSuggestionChange(
                            "Вес",
                            suggestion.currentRecommendedWeight,
                            suggestion.suggestedWeight,
                            "кг"
                          )}
                        </Text>
                      ) : null}

                      {renderSuggestionChange(
                        "Подходы",
                        suggestion.currentRecommendedSets,
                        suggestion.suggestedSets
                      ) ? (
                        <Text style={styles.suggestionMetric}>
                          {renderSuggestionChange(
                            "Подходы",
                            suggestion.currentRecommendedSets,
                            suggestion.suggestedSets
                          )}
                        </Text>
                      ) : null}

                      {renderSuggestionChange(
                        "Повторения",
                        suggestion.currentRecommendedReps,
                        suggestion.suggestedReps
                      ) ? (
                        <Text style={styles.suggestionMetric}>
                          {renderSuggestionChange(
                            "Повторения",
                            suggestion.currentRecommendedReps,
                            suggestion.suggestedReps
                          )}
                        </Text>
                      ) : null}
                    </View>
                  </View>
                );
              })
            ) : (
              <Text style={styles.emptySuggestionText}>
                Программа выглядит сбалансированной. Пока можно продолжать без изменений.
              </Text>
            )}
          </>
        )}
      </View>

      {(program.trainingDays ?? []).map((day, dayIndex) => (
        <View key={day.id ?? dayIndex} style={styles.dayCard}>
          <Text style={styles.dayTitle}>{day.dayIdentifier || `День ${dayIndex + 1}`}</Text>
          <Text style={styles.daySubtitle}>
            {day.muscleGroup || "Мышечная группа не указана"}
          </Text>

          {(day.exercises ?? []).map((exercise, exerciseIndex) => (
            <View key={exercise.id ?? `${exercise.exerciseId}-${exerciseIndex}`} style={styles.exerciseRow}>
              <Text style={styles.exerciseName}>
                {exercise.exerciseName || `Упражнение #${exercise.exerciseId}`}
              </Text>
              <Text style={styles.exerciseMeta}>
                Подходы: {exercise.recommendedSets ?? 0}, повторы: {exercise.recommendedReps ?? 0},
                вес: {exercise.recommendedWeight ?? 0} кг
              </Text>
            </View>
          ))}
        </View>
      ))}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 20,
    backgroundColor: "#111827",
    minHeight: "100%",
  },
  emptyContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#111827",
  },
  emptyText: {
    color: "#CBD5E1",
    fontSize: 16,
  },
  headerCard: {
    backgroundColor: "#1F2937",
    borderRadius: 18,
    padding: 18,
    marginBottom: 16,
  },
  title: {
    color: "#fff",
    fontSize: 24,
    fontWeight: "700",
  },
  description: {
    color: "#D1D5DB",
    marginTop: 10,
    lineHeight: 20,
  },
  meta: {
    color: "#94A3B8",
    marginTop: 10,
  },
  editButton: {
    marginTop: 16,
    backgroundColor: "#2563EB",
    borderRadius: 14,
    paddingVertical: 12,
    alignItems: "center",
  },
  editButtonText: {
    color: "#fff",
    fontWeight: "700",
  },
  adaptationCard: {
    backgroundColor: "#1F2937",
    borderRadius: 18,
    padding: 18,
    marginBottom: 16,
  },
  adaptationTitle: {
    color: "#F8FAFC",
    fontSize: 20,
    fontWeight: "700",
  },
  loaderRow: {
    flexDirection: "row",
    alignItems: "center",
    gap: 10,
    marginTop: 14,
  },
  loaderText: {
    color: "#CBD5E1",
  },
  errorText: {
    color: "#FCA5A5",
    marginTop: 14,
    lineHeight: 20,
  },
  readinessText: {
    color: "#E5E7EB",
    marginTop: 12,
    lineHeight: 20,
  },
  daysText: {
    color: "#93C5FD",
    marginTop: 10,
    fontWeight: "600",
  },
  suggestionCard: {
    borderRadius: 16,
    padding: 14,
    marginTop: 12,
  },
  suggestionTitle: {
    fontSize: 16,
    fontWeight: "700",
  },
  suggestionExercise: {
    color: "#1F2937",
    marginTop: 6,
    fontWeight: "600",
  },
  suggestionReason: {
    color: "#374151",
    marginTop: 8,
    lineHeight: 19,
  },
  suggestionMetrics: {
    marginTop: 10,
    gap: 4,
  },
  suggestionMetric: {
    color: "#111827",
    fontWeight: "600",
  },
  emptySuggestionText: {
    color: "#CBD5E1",
    marginTop: 14,
    lineHeight: 20,
  },
  dayCard: {
    backgroundColor: "#1F2937",
    borderRadius: 16,
    padding: 16,
    marginBottom: 12,
  },
  dayTitle: {
    color: "#fff",
    fontSize: 18,
    fontWeight: "700",
  },
  daySubtitle: {
    color: "#60A5FA",
    marginTop: 4,
    marginBottom: 10,
  },
  exerciseRow: {
    backgroundColor: "#111827",
    borderRadius: 12,
    padding: 12,
    marginTop: 8,
  },
  exerciseName: {
    color: "#F9FAFB",
    fontWeight: "700",
  },
  exerciseMeta: {
    color: "#CBD5E1",
    marginTop: 4,
  },
});
