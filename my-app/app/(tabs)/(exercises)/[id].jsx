import React, { useEffect, useState } from 'react';
import { View, Text, StyleSheet, Linking, ScrollView, ActivityIndicator } from 'react-native';
import { useLocalSearchParams } from 'expo-router';
import { getExerciseById } from '../../../api/exercises/getExerciseById';

const ExerciseDetail = () => {
  const { id } = useLocalSearchParams();
  const [exercise, setExercise] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchExercise = async () => {
      const data = await getExerciseById(id);
      setExercise(data);
      setLoading(false);
    };
    fetchExercise();
  }, [id]);

  if (loading) {
    return <ActivityIndicator size="large" color="#6a3cb0" style={{ marginTop: 40 }} />;
  }

  if (!exercise) {
    return <Text style={{ color: '#fff', padding: 20 }}>Упражнение не найдено</Text>;
  }

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.title}>{exercise.name}</Text>
      <Text style={styles.group}>{exercise.muscleGroup}</Text>
      <Text style={styles.description}>{exercise.description}</Text>
      {exercise.techniqueUrl && (
        <Text style={styles.link} onPress={() => Linking.openURL(exercise.techniqueUrl)}>
          Смотреть технику выполнения
        </Text>
      )}
    </ScrollView>
  );
};

export default ExerciseDetail;

const styles = StyleSheet.create({
  container: {
    padding: 20,
    backgroundColor: '#111',
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
    marginBottom: 10,
  },
  group: {
    fontSize: 16,
    color: '#aaa',
    marginBottom: 20,
  },
  description: {
    fontSize: 15,
    color: '#ccc',
    lineHeight: 22,
    marginBottom: 20,
  },
  link: {
    color: '#72bcd4',
    fontSize: 16,
    fontWeight: '600',
  },
});
