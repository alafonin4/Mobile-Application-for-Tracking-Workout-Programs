import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const ProgramCard = ({ program }) => {
  return (
    <View style={styles.card}>
      <Text style={styles.title}>{program.name}</Text>
      <Text style={styles.duration}>Длительность: {program.duration} недель</Text>
      <Text style={styles.workouts}>Тренировок: {program.workouts?.length || 0}</Text>
    </View>
  );
};

export default ProgramCard;

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
  duration: {
    color: '#bbb',
    fontSize: 14,
    marginTop: 4,
  },
  workouts: {
    color: '#ccc',
    fontSize: 14,
    marginTop: 4,
  },
});