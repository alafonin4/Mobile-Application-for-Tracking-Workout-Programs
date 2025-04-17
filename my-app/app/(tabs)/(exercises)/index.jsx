import React, { useState, useEffect } from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet, ActivityIndicator } from 'react-native';
import { useRouter } from 'expo-router';
import { getAllExercises } from '../../../api/exercises/getAllExercises';

const muscleGroups = ['Все', 'Грудь', 'Спина', 'Ноги', 'Плечи', 'Бицепс', 'Трицепс', 'Пресс'];

const AllExercisesScreen = () => {
  const router = useRouter();
  const [selectedGroup, setSelectedGroup] = useState('Все');
  const [allExercises, setAllExercises] = useState([]);
  const [filteredExercises, setFilteredExercises] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadExercises = async () => {
      const data = await getAllExercises();
      setAllExercises(data);
      setFilteredExercises(data);
      setLoading(false);
    };
    loadExercises();
  }, []);

  useEffect(() => {
    if (selectedGroup === 'Все') {
      setFilteredExercises(allExercises);
    } else {
      setFilteredExercises(allExercises.filter(ex => ex.muscleGroup === selectedGroup));
    }
  }, [selectedGroup, allExercises]);

  if (loading) {
    return <ActivityIndicator size="large" color="#6a3cb0" style={{ marginTop: 40 }} />;
  }

  return (
    <View style={styles.container}>
      <FlatList
        horizontal
        data={muscleGroups}
        keyExtractor={(item) => item}
        contentContainerStyle={styles.filterContainer}
        showsHorizontalScrollIndicator={false}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={[styles.filterButton, selectedGroup === item && styles.selectedButton]}
            onPress={() => setSelectedGroup(item)}
          >
            <Text style={[styles.filterText, selectedGroup === item && styles.selectedText]}>
              {item}
            </Text>
          </TouchableOpacity>
        )}
      />

      <FlatList
        data={filteredExercises}
        keyExtractor={(item) => item.id.toString()}
        contentContainerStyle={styles.exerciseList}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.exerciseItem}
            onPress={() => router.push(`/exercise/${item.id}`)}
          >
            <Text style={styles.exerciseName}>{item.name}</Text>
            <Text style={styles.exerciseGroup}>{item.muscleGroup}</Text>
          </TouchableOpacity>
        )}
      />
    </View>
  );
};

export default AllExercisesScreen;
