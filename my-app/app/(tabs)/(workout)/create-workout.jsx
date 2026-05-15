import React, { useEffect, useMemo, useState } from "react";
import {
  Alert,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from "react-native";
import { useRouter } from "expo-router";

import AddExerciseModal from "../../../components/AddExerciseModal";
import AddSetModal from "../../../components/AddSetModal";
import { getApiErrorMessage } from "../../../api/client";
import { getAllExercises } from "../../../api/exercises/getAllExercises";
import { createWorkout } from "../../../api/workout/createWorkout";
import { fetchUserPrograms } from "../../../api/workout/fetchUserData";
import { useUserId } from "../../../hooks/useUserId";

const getExerciseLabelFromMap = (exerciseId, exercisesMap) => {
  return exercisesMap[exerciseId] ?? {
    id: exerciseId,
    name: `Упражнение #${exerciseId}`,
    muscleGroup: "Без группы",
    requiresAdditionalWeight: false,
  };
};

const CreateWorkout = () => {
  const router = useRouter();
  const [userId, , isUserIdLoaded] = useUserId();
  const [workoutName, setWorkoutName] = useState("");
  const [exercises, setExercises] = useState([]);
  const [programs, setPrograms] = useState([]);
  const [exerciseDirectory, setExerciseDirectory] = useState({});
  const [selectedExerciseIndex, setSelectedExerciseIndex] = useState(null);
  const [showExerciseModal, setShowExerciseModal] = useState(false);
  const [showSetModal, setShowSetModal] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [selectedTemplateKey, setSelectedTemplateKey] = useState(null);

  useEffect(() => {
    const loadTemplates = async () => {
      if (!isUserIdLoaded || userId == null) {
        return;
      }

      try {
        const [loadedPrograms, loadedExercises] = await Promise.all([
          fetchUserPrograms(userId),
          getAllExercises(),
        ]);

        setPrograms(loadedPrograms);
        setExerciseDirectory(
          loadedExercises.reduce((accumulator, exercise) => {
            accumulator[exercise.id] = exercise;
            return accumulator;
          }, {})
        );
      } catch (error) {
        Alert.alert(
          "Ошибка",
          getApiErrorMessage(error, "Не удалось загрузить программы и упражнения.")
        );
      }
    };

    loadTemplates();
  }, [isUserIdLoaded, userId]);

  const programDays = useMemo(
    () =>
      programs.flatMap((program) =>
        (program.trainingDays ?? []).map((day) => ({
          key: `${program.id}-${day.id ?? day.dayIdentifier}`,
          programName: program.name,
          day,
        }))
      ),
    [programs]
  );

  const appendExerciseIfMissing = (exerciseToAdd) => {
    setExercises((current) => {
      const alreadyExists = current.some((item) => item.id === exerciseToAdd.id);
      if (alreadyExists) {
        return current;
      }

      return [...current, { ...exerciseToAdd, sets: exerciseToAdd.sets ?? [] }];
    });
  };

  const handleAddExercise = (exercise) => {
    appendExerciseIfMissing({
      id: exercise.id,
      name: exercise.name,
      muscleGroup: exercise.muscleGroup,
      requiresAdditionalWeight: exercise.requiresAdditionalWeight,
      recommendedSets: null,
      recommendedReps: null,
      recommendedWeight: null,
      sets: [],
    });
    setShowExerciseModal(false);
  };

  const handleImportDay = (item) => {
    const importedExercises = (item.day.exercises ?? []).map((exercise) => {
      const exerciseMeta = getExerciseLabelFromMap(exercise.exerciseId, exerciseDirectory);
      return {
        id: exercise.exerciseId,
        name: exercise.exerciseName || exerciseMeta.name,
        muscleGroup: exerciseMeta.muscleGroup,
        requiresAdditionalWeight: exerciseMeta.requiresAdditionalWeight,
        recommendedSets: exercise.recommendedSets,
        recommendedReps: exercise.recommendedReps,
        recommendedWeight: exercise.recommendedWeight,
        sets: [],
      };
    });

    if (!importedExercises.length) {
      Alert.alert("Нет упражнений", "В выбранном тренировочном дне пока нет упражнений.");
      return;
    }

    setSelectedTemplateKey(item.key);
    setExercises((current) => {
      const existingIds = new Set(current.map((exercise) => exercise.id));
      const newExercises = importedExercises.filter((exercise) => !existingIds.has(exercise.id));

      if (!newExercises.length) {
        return current;
      }

      return [...current, ...newExercises];
    });
  };

  const handleAddSet = (setData) => {
    setExercises((current) =>
      current.map((exercise, index) =>
        index === selectedExerciseIndex
          ? { ...exercise, sets: [...exercise.sets, setData] }
          : exercise
      )
    );
    setShowSetModal(false);
  };

  const handleSubmit = async () => {
    if (!isUserIdLoaded || userId == null) {
      Alert.alert("Ошибка", "Не удалось определить пользователя.");
      return;
    }

    if (!workoutName.trim()) {
      Alert.alert("Ошибка", "Введите название тренировки.");
      return;
    }

    if (!exercises.length) {
      Alert.alert("Ошибка", "Добавьте хотя бы одно упражнение.");
      return;
    }

    setIsSubmitting(true);
    try {
      await createWorkout({
        userId,
        name: workoutName.trim(),
        workoutDate: new Date().toISOString(),
        workoutExercises: exercises.map((exercise) => ({
          exercise: { id: exercise.id },
          notes: "",
          sets: exercise.sets.map((setItem, index) => ({
            setNumber: index + 1,
            reps: setItem.reps,
            weight: setItem.weight,
          })),
        })),
      });

      Alert.alert("Успех", "Тренировка сохранена.");
      router.replace("/(tabs)/(workout)");
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось сохранить тренировку.")
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.label}>Название тренировки</Text>
      <TextInput
        style={styles.input}
        placeholder="Например, Грудь и трицепс"
        placeholderTextColor="#8a8a8a"
        value={workoutName}
        onChangeText={setWorkoutName}
      />

      <View style={styles.programSection}>
        <Text style={styles.sectionTitle}>Импорт из тренировочной программы</Text>
        <Text style={styles.sectionSubtitle}>
          Выберите день из своей программы, и упражнения перенесутся в тренировку как шаблон.
        </Text>

        {programDays.length ? (
          programDays.map((item) => (
            <TouchableOpacity
              key={item.key}
              style={[
                styles.programDayCard,
                selectedTemplateKey === item.key && styles.programDayCardActive,
              ]}
              onPress={() => handleImportDay(item)}
            >
              <Text style={styles.programDayProgram}>{item.programName}</Text>
              <Text style={styles.programDayTitle}>
                {item.day.dayIdentifier || "Тренировочный день"}
              </Text>
              <Text style={styles.programDayMeta}>
                {item.day.muscleGroup || "Без группы"} • {(item.day.exercises ?? []).length} упражнений
              </Text>
            </TouchableOpacity>
          ))
        ) : (
          <Text style={styles.emptyProgramText}>У вас пока нет тренировочных программ.</Text>
        )}
      </View>

      {exercises.map((exercise, index) => (
        <View key={`${exercise.id}-${index}`} style={styles.exerciseBlock}>
          <Text style={styles.exerciseTitle}>{exercise.name}</Text>
          <Text style={styles.exerciseGroup}>
            {exercise.muscleGroup} •{" "}
            {exercise.requiresAdditionalWeight ? "с доп. весом" : "без доп. веса"}
          </Text>

          {exercise.recommendedSets || exercise.recommendedReps ? (
            <Text style={styles.recommendationText}>
              Рекомендация из программы: {exercise.recommendedSets ?? 0} подходов ×{" "}
              {exercise.recommendedReps ?? 0} повторений, вес {exercise.recommendedWeight ?? 0} кг
            </Text>
          ) : null}

          {exercise.sets.map((setItem, setIndex) => (
            <Text key={setIndex} style={styles.setText}>
              Подход {setIndex + 1}: {setItem.reps} повт. •{" "}
              {setItem.weight === 0 ? "собственный вес" : `${setItem.weight} кг`}
            </Text>
          ))}

          <TouchableOpacity
            style={styles.addSetButton}
            onPress={() => {
              setSelectedExerciseIndex(index);
              setShowSetModal(true);
            }}
          >
            <Text style={styles.addSetText}>+ Добавить подход</Text>
          </TouchableOpacity>
        </View>
      ))}

      <TouchableOpacity
        style={styles.addExerciseButton}
        onPress={() => setShowExerciseModal(true)}
      >
        <Text style={styles.addExerciseText}>+ Добавить упражнение</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={[styles.submitButton, isSubmitting && styles.submitButtonDisabled]}
        onPress={handleSubmit}
        disabled={isSubmitting}
      >
        <Text style={styles.submitText}>
          {isSubmitting ? "Сохраняем..." : "Сохранить тренировку"}
        </Text>
      </TouchableOpacity>

      <AddExerciseModal
        visible={showExerciseModal}
        onClose={() => setShowExerciseModal(false)}
        onSelectExercise={handleAddExercise}
        userId={userId}
      />
      <AddSetModal
        visible={showSetModal}
        onClose={() => setShowSetModal(false)}
        onAddSet={handleAddSet}
      />
    </ScrollView>
  );
};

export default CreateWorkout;

const styles = StyleSheet.create({
  container: {
    padding: 20,
    paddingBottom: 80,
    backgroundColor: "#1A1A1A",
    minHeight: "100%",
  },
  label: {
    fontSize: 18,
    color: "#fff",
    marginBottom: 10,
  },
  input: {
    backgroundColor: "#2D2D2D",
    color: "#fff",
    padding: 12,
    borderRadius: 8,
    marginBottom: 20,
  },
  sectionTitle: {
    color: "#fff",
    fontSize: 18,
    fontWeight: "700",
  },
  sectionSubtitle: {
    color: "#9CA3AF",
    marginTop: 6,
    marginBottom: 12,
    lineHeight: 20,
  },
  programSection: {
    marginBottom: 20,
  },
  programDayCard: {
    backgroundColor: "#252525",
    borderRadius: 12,
    padding: 14,
    marginBottom: 10,
  },
  programDayCardActive: {
    borderWidth: 1,
    borderColor: "#2563EB",
  },
  programDayProgram: {
    color: "#60A5FA",
    fontWeight: "700",
  },
  programDayTitle: {
    color: "#fff",
    fontSize: 16,
    marginTop: 6,
    fontWeight: "600",
  },
  programDayMeta: {
    color: "#9CA3AF",
    marginTop: 4,
  },
  emptyProgramText: {
    color: "#9CA3AF",
  },
  exerciseBlock: {
    backgroundColor: "#2D2D2D",
    padding: 15,
    borderRadius: 10,
    marginBottom: 15,
  },
  exerciseTitle: {
    fontSize: 16,
    color: "#fff",
    fontWeight: "600",
  },
  exerciseGroup: {
    color: "#9d9d9d",
    marginTop: 4,
    marginBottom: 8,
  },
  recommendationText: {
    color: "#93C5FD",
    marginBottom: 10,
    lineHeight: 18,
  },
  setText: {
    color: "#ccc",
    marginBottom: 5,
  },
  addSetButton: {
    marginTop: 10,
  },
  addSetText: {
    color: "#8de969",
    fontWeight: "600",
  },
  addExerciseButton: {
    marginTop: 10,
    paddingVertical: 14,
    alignItems: "center",
  },
  addExerciseText: {
    color: "#72bcd4",
    fontSize: 16,
    fontWeight: "500",
  },
  submitButton: {
    marginTop: 30,
    backgroundColor: "#6a3cb0",
    padding: 16,
    borderRadius: 12,
    alignItems: "center",
  },
  submitButtonDisabled: {
    opacity: 0.6,
  },
  submitText: {
    color: "#fff",
    fontSize: 16,
    fontWeight: "600",
  },
});
