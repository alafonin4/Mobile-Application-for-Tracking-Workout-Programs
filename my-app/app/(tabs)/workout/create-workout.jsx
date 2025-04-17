import React, { useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, StyleSheet, Modal, Alert } from 'react-native';
import { useRouter } from 'expo-router';
import { TextInput } from 'react-native-gesture-handler';
import AddExerciseModal from '../../components/AddExerciseModal';
import AddSetModal from '../../components/AddSetModal';
import { createWorkout } from '../../api/workout/createWorkout';
import { useUserId } from '../../hooks/useUserId';

const CreateWorkout = () => {
  const router = useRouter();
  const userId = useUserId();

  const [workoutName, setWorkoutName] = useState('');
  const [exercises, setExercises] = useState([]);
  const [selectedExerciseIndex, setSelectedExerciseIndex] = useState(null);
  const [showExerciseModal, setShowExerciseModal] = useState(false);
  const [showSetModal, setShowSetModal] = useState(false);

  const handleAddExercise = (exercise) => {
    setExercises([...exercises, { ...exercise, sets: [] }]);
    setShowExerciseModal(false);
  };

  const handleAddSet = (setData) => {
    const updated = [...exercises];
    updated[selectedExerciseIndex].sets.push(setData);
    setExercises(updated);
    setShowSetModal(false);
  };

  const handleSubmit = async () => {
    if (!workoutName.trim()) {
      Alert.alert('Ошибка', 'Введите название тренировки');
      return;
    }

    if (!exercises.length) {
      Alert.alert('Ошибка', 'Добавьте хотя бы одно упражнение');
      return;
    }

    try {
      await createWorkout({
        userId,
        name: workoutName,
        exercises: exercises.map((ex) => ({
          id: ex.id,
          sets: ex.sets.map((set) => ({
            repetitions: set.repetitions,
            weight: set.weight
          }))
        }))
      });

      Alert.alert('Успех', 'Тренировка сохранена');
      router.replace('/(tabs)/workout');
    } catch (err) {
      console.error(err);
      Alert.alert('Ошибка', 'Не удалось сохранить тренировку');
    }
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.label}>Название тренировки</Text>
      <TextInput
        style={styles.input}
        placeholder="Введите название"
        value={workoutName}
        onChangeText={setWorkoutName}
      />

      {exercises.map((exercise, index) => (
        <View key={index} style={styles.exerciseBlock}>
          <Text style={styles.exerciseTitle}>{exercise.name}</Text>
          {exercise.sets.map((set, setIndex) => (
            <Text key={setIndex} style={styles.setText}>
              Подход {setIndex + 1}: {set.repetitions} повт. — {set.weight === 0 ? 'Собственный вес' : "${set.weight} кг"}
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

      <TouchableOpacity style={styles.addExerciseButton} onPress={() => setShowExerciseModal(true)}>
        <Text style={styles.addExerciseText}>+ Добавить упражнение</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.submitButton} onPress={handleSubmit}>
        <Text style={styles.submitText}>Закончить тренировку</Text>
      </TouchableOpacity>

      <AddExerciseModal visible={showExerciseModal} onClose={() => setShowExerciseModal(false)} onSelect={handleAddExercise} />
      <AddSetModal visible={showSetModal} onClose={() => setShowSetModal(false)} onAdd={handleAddSet} />
    </ScrollView>
  );
};

export default CreateWorkout;

const styles = StyleSheet.create({
  container: {
    padding: 20,
    paddingBottom: 80,},
    label: {
      fontSize: 18,
      color: '#fff',
      marginBottom: 10,
    },
    input: {
      backgroundColor: '#444',
      color: '#fff',
      padding: 12,
      borderRadius: 8,
      marginBottom: 20,
    },
    exerciseBlock: {
      backgroundColor: '#333',
      padding: 15,
      borderRadius: 10,
      marginBottom: 15,
    },
    exerciseTitle: {
      fontSize: 16,
      color: '#fff',
      fontWeight: '600',
      marginBottom: 10,
    },
    setText: {
      color: '#ccc',
      marginBottom: 5,
    },
    addSetButton: {
      marginTop: 10,
    },
    addSetText: {
      color: '#8de969',
      fontWeight: '600',
    },
    addExerciseButton: {
      marginTop: 10,
      paddingVertical: 14,
      alignItems: 'center',
    },
    addExerciseText: {
      color: '#72bcd4',
      fontSize: 16,
      fontWeight: '500',
    },
    submitButton: {
      marginTop: 30,
      backgroundColor: '#6a3cb0',
      padding: 16,
      borderRadius: 12,
      alignItems: 'center',
    },
    submitText: {
      color: '#fff',
      fontSize: 16,
      fontWeight: '600',
    },
  });