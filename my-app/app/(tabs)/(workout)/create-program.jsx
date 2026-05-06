import React, { useMemo, useState } from "react";
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
import { createProgram } from "../../../api/workout/createProgram";
import { useUserId } from "../../../hooks/useUserId";

const createEmptyDay = () => ({
  dayIdentifier: "",
  muscleGroup: "",
  exercises: [],
});

const CreateProgram = () => {
  const router = useRouter();
  const [userId, , isUserIdLoaded] = useUserId();
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [trainingDays, setTrainingDays] = useState([createEmptyDay()]);
  const [selectedDayIndex, setSelectedDayIndex] = useState(null);
  const [showExerciseModal, setShowExerciseModal] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const canSubmit = useMemo(
    () =>
      Boolean(name.trim()) &&
      trainingDays.every((day) => day.dayIdentifier.trim() && day.muscleGroup.trim()),
    [name, trainingDays]
  );

  const updateDayField = (index, field, value) => {
    setTrainingDays((current) =>
      current.map((day, dayIndex) =>
        dayIndex === index ? { ...day, [field]: value } : day
      )
    );
  };

  const addTrainingDay = () => {
    setTrainingDays((current) => [...current, createEmptyDay()]);
  };

  const addExerciseToDay = (exercise) => {
    setTrainingDays((current) =>
      current.map((day, index) =>
        index === selectedDayIndex
          ? {
              ...day,
              exercises: [
                ...day.exercises,
                {
                  exerciseId: exercise.id,
                  exerciseName: exercise.name,
                  recommendedSets: 3,
                  recommendedReps: 10,
                  recommendedWeight: 0,
                },
              ],
            }
          : day
      )
    );
    setShowExerciseModal(false);
  };

  const updateExerciseField = (dayIndex, exerciseIndex, field, value) => {
    setTrainingDays((current) =>
      current.map((day, currentDayIndex) => {
        if (currentDayIndex !== dayIndex) {
          return day;
        }

        return {
          ...day,
          exercises: day.exercises.map((exercise, currentExerciseIndex) =>
            currentExerciseIndex === exerciseIndex
              ? { ...exercise, [field]: value }
              : exercise
          ),
        };
      })
    );
  };

  const handleSubmit = async () => {
    if (!isUserIdLoaded || userId == null) {
      Alert.alert("Ошибка", "Не удалось определить пользователя.");
      return;
    }

    if (!canSubmit) {
      Alert.alert("Ошибка", "Заполните название программы и данные по дням.");
      return;
    }

    setIsSubmitting(true);
    try {
      await createProgram({
        userId,
        name: name.trim(),
        description: description.trim(),
        trainingDays: trainingDays.map((day) => ({
          dayIdentifier: day.dayIdentifier.trim(),
          muscleGroup: day.muscleGroup.trim(),
          exercises: day.exercises.map((exercise) => ({
            exerciseId: exercise.exerciseId,
            recommendedSets: Number(exercise.recommendedSets) || 0,
            recommendedReps: Number(exercise.recommendedReps) || 0,
            recommendedWeight: Number(exercise.recommendedWeight) || 0,
          })),
        })),
      });

      Alert.alert("Успех", "Программа сохранена.");
      router.replace("/(tabs)/(workout)");
    } catch (error) {
      console.error("Failed to create program:", error);
      Alert.alert("Ошибка", "Не удалось сохранить программу.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.title}>Новая программа</Text>

      <TextInput
        style={styles.input}
        placeholder="Название программы"
        placeholderTextColor="#8a8a8a"
        value={name}
        onChangeText={setName}
      />

      <TextInput
        style={[styles.input, styles.textArea]}
        placeholder="Описание"
        placeholderTextColor="#8a8a8a"
        value={description}
        onChangeText={setDescription}
        multiline
      />

      {trainingDays.map((day, dayIndex) => (
        <View key={dayIndex} style={styles.dayCard}>
          <Text style={styles.dayTitle}>День {dayIndex + 1}</Text>

          <TextInput
            style={styles.input}
            placeholder="Например, Понедельник"
            placeholderTextColor="#8a8a8a"
            value={day.dayIdentifier}
            onChangeText={(value) => updateDayField(dayIndex, "dayIdentifier", value)}
          />

          <TextInput
            style={styles.input}
            placeholder="Целевая группа мышц"
            placeholderTextColor="#8a8a8a"
            value={day.muscleGroup}
            onChangeText={(value) => updateDayField(dayIndex, "muscleGroup", value)}
          />

          {day.exercises.map((exercise, exerciseIndex) => (
            <View key={`${exercise.exerciseId}-${exerciseIndex}`} style={styles.exerciseCard}>
              <Text style={styles.exerciseName}>{exercise.exerciseName}</Text>

              <View style={styles.inlineInputs}>
                <TextInput
                  style={[styles.input, styles.smallInput]}
                  placeholder="Подходы"
                  placeholderTextColor="#8a8a8a"
                  keyboardType="numeric"
                  value={String(exercise.recommendedSets)}
                  onChangeText={(value) =>
                    updateExerciseField(dayIndex, exerciseIndex, "recommendedSets", value)
                  }
                />
                <TextInput
                  style={[styles.input, styles.smallInput]}
                  placeholder="Повторы"
                  placeholderTextColor="#8a8a8a"
                  keyboardType="numeric"
                  value={String(exercise.recommendedReps)}
                  onChangeText={(value) =>
                    updateExerciseField(dayIndex, exerciseIndex, "recommendedReps", value)
                  }
                />
                <TextInput
                  style={[styles.input, styles.smallInput]}
                  placeholder="Вес"
                  placeholderTextColor="#8a8a8a"
                  keyboardType="numeric"
                  value={String(exercise.recommendedWeight)}
                  onChangeText={(value) =>
                    updateExerciseField(dayIndex, exerciseIndex, "recommendedWeight", value)
                  }
                />
              </View>
            </View>
          ))}

          <TouchableOpacity
            style={styles.secondaryButton}
            onPress={() => {
              setSelectedDayIndex(dayIndex);
              setShowExerciseModal(true);
            }}
          >
            <Text style={styles.secondaryButtonText}>+ Добавить упражнение</Text>
          </TouchableOpacity>
        </View>
      ))}

      <TouchableOpacity style={styles.secondaryButton} onPress={addTrainingDay}>
        <Text style={styles.secondaryButtonText}>+ Добавить тренировочный день</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={[styles.submitButton, (!canSubmit || isSubmitting) && styles.submitDisabled]}
        onPress={handleSubmit}
        disabled={!canSubmit || isSubmitting}
      >
        <Text style={styles.submitText}>
          {isSubmitting ? "Сохраняем..." : "Сохранить программу"}
        </Text>
      </TouchableOpacity>

      <AddExerciseModal
        visible={showExerciseModal}
        onClose={() => setShowExerciseModal(false)}
        onSelectExercise={addExerciseToDay}
      />
    </ScrollView>
  );
};

export default CreateProgram;

const styles = StyleSheet.create({
  container: {
    padding: 20,
    paddingBottom: 80,
    backgroundColor: "#1A1A1A",
    minHeight: "100%",
  },
  title: {
    color: "#fff",
    fontSize: 24,
    fontWeight: "700",
    marginBottom: 20,
  },
  input: {
    backgroundColor: "#2D2D2D",
    color: "#fff",
    padding: 12,
    borderRadius: 8,
    marginBottom: 12,
  },
  textArea: {
    minHeight: 100,
    textAlignVertical: "top",
  },
  dayCard: {
    backgroundColor: "#252525",
    borderRadius: 14,
    padding: 16,
    marginBottom: 16,
  },
  dayTitle: {
    color: "#fff",
    fontSize: 18,
    fontWeight: "600",
    marginBottom: 12,
  },
  exerciseCard: {
    backgroundColor: "#303030",
    borderRadius: 10,
    padding: 12,
    marginBottom: 12,
  },
  exerciseName: {
    color: "#fff",
    fontWeight: "600",
    marginBottom: 10,
  },
  inlineInputs: {
    flexDirection: "row",
    columnGap: 8,
  },
  smallInput: {
    flex: 1,
    marginBottom: 0,
  },
  secondaryButton: {
    borderWidth: 1,
    borderColor: "#72bcd4",
    borderRadius: 10,
    paddingVertical: 12,
    alignItems: "center",
    marginBottom: 12,
  },
  secondaryButtonText: {
    color: "#72bcd4",
    fontWeight: "600",
  },
  submitButton: {
    backgroundColor: "#6a3cb0",
    borderRadius: 12,
    paddingVertical: 16,
    alignItems: "center",
  },
  submitDisabled: {
    opacity: 0.6,
  },
  submitText: {
    color: "#fff",
    fontSize: 16,
    fontWeight: "600",
  },
});
