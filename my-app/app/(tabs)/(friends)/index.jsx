import React, { useEffect, useState, useCallback } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  RefreshControl,
  TouchableOpacity,
} from 'react-native';
import { Feather } from '@expo/vector-icons';
import { useRouter } from 'expo-router';
import { useUserId } from '../../../hooks/useUserId';
import { getFriends } from '../../../api/friends/getFriends';
import { getIncomingRequests } from '../../../api/friends/getIncomingRequests';
import { getOutgoingRequests } from '../../../api/friends/getOutgoingRequests';
import { SafeAreaView } from 'react-native-safe-area-context';

const FriendsScreen = () => {
  const router = useRouter();
  const [userId, , isLoaded] = useUserId(); // ← добавлено isLoaded

  const [activeTab, setActiveTab] = useState('friends');
  const [friends, setFriends] = useState([]);
  const [incoming, setIncoming] = useState([]);
  const [outgoing, setOutgoing] = useState([]);
  const [refreshing, setRefreshing] = useState(false);

  const loadData = useCallback(async () => {
    if (!isLoaded || userId === null) return; // ← проверка здесь тоже

    setRefreshing(true);
    try {
      const [friendsRes, incomingRes, outgoingRes] = await Promise.all([
        getFriends(userId),
        getIncomingRequests(userId),
        getOutgoingRequests(userId),
      ]);
      setFriends(friendsRes);
      setIncoming(incomingRes);
      setOutgoing(outgoingRes);
    } catch (error) {
      console.warn('Ошибка при загрузке данных друзей:', error);
    } finally {
      setRefreshing(false);
    }
  }, [userId, isLoaded]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const renderList = () => {
    let data;
    switch (activeTab) {
      case 'friends':
        data = friends;
        break;
      case 'incoming':
        data = incoming;
        break;
      case 'outgoing':
        data = outgoing;
        break;
      default:
        data = [];
    }

    if (!data || data.length === 0) {
      const label =
        activeTab === 'friends'
          ? 'друзей'
          : activeTab === 'incoming'
          ? 'входящих заявок'
          : 'исходящих заявок';

      return <Text style={styles.emptyText}>У вас пока нет {label}.</Text>;
    }

    return data.map((item) => (
      <TouchableOpacity key={item.id} style={styles.card}>
        <Text style={styles.name}>
          {item.firstName} {item.lastName}
        </Text>
        <Text style={styles.email}>{item.email}</Text>
      </TouchableOpacity>
    ));
  };

  const tabLabels = {
    friends: 'Друзья',
    incoming: 'Входящие',
    outgoing: 'Исходящие',
  };

  const handleAddFriend = () => {
    router.push('/tabs/friends/search');
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.tabContainer}>
        {Object.entries(tabLabels).map(([key, label]) => (
          <TouchableOpacity
            key={key}
            onPress={() => setActiveTab(key)}
            style={[styles.tab, activeTab === key && styles.activeTab]}
          >
            <Text style={styles.tabText}>{label}</Text>
          </TouchableOpacity>
        ))}
      </View>

      <ScrollView
        contentContainerStyle={styles.scrollView}
        refreshControl={
          <RefreshControl refreshing={refreshing} onRefresh={loadData} />
        }
      >
        {renderList()}
      </ScrollView>

      <TouchableOpacity style={styles.fab} onPress={handleAddFriend}>
        <Feather name="user-plus" size={28} color="#fff" />
      </TouchableOpacity>
    </SafeAreaView>
  );
};

export default FriendsScreen;

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
    marginHorizontal: 5,
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
  card: {
    backgroundColor: '#3B2F2F',
    padding: 16,
    borderRadius: 12,
    marginBottom: 12,
  },
  name: {
    fontSize: 18,
    color: '#fff',
    fontWeight: '600',
  },
  email: {
    fontSize: 14,
    color: '#bbb',
    marginTop: 4,
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
