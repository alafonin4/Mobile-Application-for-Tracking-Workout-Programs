import React, { useState } from "react";
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
import { createWorkout } from "../../../api/workout/createWorkout";
import { useUserId } from "../../../hooks/useUserId";

const CreateWorkout = () => {
  const router = useRouter();
  const [userId, , isUserIdLoaded] = useUserId();
  const [workoutName, setWorkoutName] = useState("");
  const [exercises, setExercises] = useState([]);
  const [selectedExerciseIndex, setSelectedExerciseIndex] = useState(null);
  const [showExerciseModal, setShowExerciseModal] = useState(false);
  const [showSetModal, setShowSetModal] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleAddExercise = (exercise) => {
    setExercises((current) => [...current, { ...exercise, sets: [] }]);
    setShowExerciseModal(false);
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
      console.error("Failed to create workout:", error);
      Alert.alert("Ошибка", "Не удалось сохранить тренировку.");
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

      {exercises.map((exercise, index) => (
        <View key={`${exercise.id}-${index}`} style={styles.exerciseBlock}>
          <Text style={styles.exerciseTitle}>{exercise.name}</Text>
          <Text style={styles.exerciseGroup}>{exercise.muscleGroup}</Text>

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
    marginBottom: 10,
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
