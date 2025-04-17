import React, { useEffect, useState, useCallback } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  RefreshControl,
  TouchableOpacity,
} from 'react-native';
import { useRouter } from 'expo-router';
import { Feather } from '@expo/vector-icons';
import WorkoutCard from '../../components/WorkoutCard';
import ProgramCard from '../../components/ProgramCard';
import { fetchUserWorkouts, fetchUserPrograms } from '../../api/workout/fetchUserData';
import { useUserId } from '../../hooks/useUserId';

const WorkoutScreen = () => {
  const router = useRouter();
  const [activeTab, setActiveTab] = useState('workouts');
  const [workouts, setWorkouts] = useState([]);
  const [programs, setPrograms] = useState([]);
  const [refreshing, setRefreshing] = useState(false);
  const userId = useUserId();

  const loadData = useCallback(async () => {
    if (!userId) return;
    setRefreshing(true);
    try {
      const [userWorkouts, userPrograms] = await Promise.all([
        fetchUserWorkouts(userId),
        fetchUserPrograms(userId),
      ]);
      setWorkouts(userWorkouts);
      setPrograms(userPrograms);
    } catch (error) {
      console.warn('Ошибка при загрузке данных:', error);
    } finally {
      setRefreshing(false);
    }
  }, [userId]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const renderEmptyMessage = () => (
    <Text style={styles.emptyText}>
      Пока что у вас нет {activeTab === 'workouts' ? 'тренировок' : 'тренировочных программ'}.
    </Text>
  );

  const renderContent = () => {
    const data = activeTab === 'workouts' ? workouts : programs;

    if (!data || data.length === 0) {
      return renderEmptyMessage();
    }

    return data.map((item) =>
      activeTab === 'workouts' ? (
        <WorkoutCard key={item.id} workout={item} />
      ) : (
        <ProgramCard key={item.id} program={item} />
      )
    );
  };

  const handleCreate = () => {
    router.push({
      pathname: '/workout/create-choice',
    });
  };

  return (
    <View style={styles.container}>
      <View style={styles.tabContainer}>
        <TouchableOpacity
          onPress={() => setActiveTab('workouts')}
          style={[styles.tab, activeTab === 'workouts' && styles.activeTab]}
        >
          <Text style={styles.tabText}>Тренировки</Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() => setActiveTab('programs')}
          style={[styles.tab, activeTab === 'programs' && styles.activeTab]}
        >
          <Text style={styles.tabText}>Программы</Text>
        </TouchableOpacity>
      </View>

      <ScrollView
        contentContainerStyle={styles.scrollView}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={loadData} />
        }
      >
        {renderContent()}
      </ScrollView>

      <TouchableOpacity style={styles.fab} onPress={handleCreate}>
        <Feather name="plus" size={28} color="#fff" />
      </TouchableOpacity>
    </View>
  );
};

export default WorkoutScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#1A1A1A',
    paddingTop: 40,
  },
  tabContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginBottom: 12,
  },
  tab: {
    paddingVertical: 10,
    paddingHorizontal: 20,
    marginHorizontal: 10,
    borderRadius: 20,
    backgroundColor: '#333',
  },
  activeTab: {
    backgroundColor: '#6200EA',
  },
  tabText: {
    color: '#fff',
    fontSize: 16,
  },
  scrollView: {
    paddingHorizontal: 20,
    paddingBottom: 100,
  },
  emptyText: {
    color: '#aaa',
    textAlign: 'center',
    marginTop: 40,
    fontSize: 16,
  },
  fab: {
    position: 'absolute',
    right: 20,
    bottom: 30,
    backgroundColor: '#6200EA',
    padding: 16,
    borderRadius: 50,
    elevation: 5,
  },
});