import React, { useState, useEffect } from 'react';
import { View, Text, FlatList, TouchableOpacity, StyleSheet, RefreshControl } from 'react-native';
import { useRouter } from 'expo-router';
import FloatingAction from '../../components/FloatingAction';
import { getFriends } from '../../api/friends/getFriends';

const FriendsScreen = () => {
  const router = useRouter();
  const [friends, setFriends] = useState([]);
  const [refreshing, setRefreshing] = useState(false);

  const fetchFriends = async () => {
    try {
      const data = await getFriends();
      setFriends(data);
    } catch (error) {
      console.error('Ошибка загрузки друзей:', error);
    }
  };

  useEffect(() => {
    fetchFriends();
  }, []);

  const onRefresh = async () => {
    setRefreshing(true);
    await fetchFriends();
    setRefreshing(false);
  };

  const handleFriendPress = (friendId) => {
    router.push("/tabs/friends/${friendId}");
  };

  const renderFriend = ({ item }) => (
    <TouchableOpacity style={styles.card} onPress={() => handleFriendPress(item.id)}>
      <Text style={styles.name}>{item.firstName} {item.lastName}</Text>
      <Text style={styles.email}>{item.email}</Text>
    </TouchableOpacity>
  );

  return (
    <View style={styles.container}>
      <FlatList
        data={friends}
        keyExtractor={(item) => item.id.toString()}
        renderItem={renderFriend}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} />}
        ListEmptyComponent={<Text style={styles.empty}>У вас пока нет друзей</Text>}
        contentContainerStyle={friends.length === 0 && styles.emptyContainer}
      />

      <FloatingAction onPress={() => router.push('/tabs/friends/search')} />
    </View>
  );
};

export default FriendsScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#2D1F13',
    padding: 16,
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
  empty: {
    color: '#ccc',
    fontSize: 16,
    textAlign: 'center',
    marginTop: 32,
  },
  emptyContainer: {
    flexGrow: 1,
    justifyContent: 'center',
  },
});