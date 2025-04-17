import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const WorkoutCard = ({ workout }) => {
  return (
    <View style={styles.card}>
      <Text style={styles.title}>{workout.name}</Text>
      <Text style={styles.date}>Дата: {new Date(workout.createdAt).toLocaleDateString()}</Text>
      <Text style={styles.details}>Упражнений: {workout.exercises?.length || 0}</Text>
    </View>
  );
};

export default WorkoutCard;

const styles = StyleSheet.create({
  card: {
    backgroundColor: '#2D2D2D',
    borderRadius: 12,
    padding: 16,
    marginBottom: 12,
  },
  title: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
  date: {
    color: '#bbb',
    fontSize: 14,
    marginTop: 4,
  },
  details: {
    color: '#ccc',
    fontSize: 14,
    marginTop: 4,
  },
});