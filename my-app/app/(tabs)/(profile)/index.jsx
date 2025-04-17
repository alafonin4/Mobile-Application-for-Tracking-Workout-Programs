import { Image, StyleSheet, Platform } from 'react-native';
import { View, Text, FlatList, TouchableOpacity, Alert, SafeAreaView } from 'react-native';
import { useRouter } from 'expo-router';
import { useEffect, useState } from 'react';


import { useUserId } from '../../../hooks/useUserId';
import { get_user_profile } from '../../../api/user/get_user_profile';

const ProfileScreen = () => {
  const router = useRouter();
  const [user, setUser] = useState(null);
  const [userId, , isLoaded] = useUserId();

useEffect(() => {
  const fetchUser = async () => {
    if (!isLoaded || userId === null) return;

    try {
      console.log("✅ userId загружен:", userId);
      const profile = await get_user_profile(userId);
      setUser(profile);
    } catch (error) {
      console.warn("Ошибка загрузки профиля:", error);
    }
  };

  fetchUser();
}, [userId, isLoaded]);

  const menuItems = [
    { id: '1', title: '✏️ Редактировать профиль', route: '/profile/edit' },
    { id: '2', title: '🔒 Изменить пароль', route: '/profile/password' },
    { id: '3', title: '👥 Список друзей', route: '(friends)' },
    { id: '4', title: '🚪 Выйти из аккаунта', action: () => Alert.alert('Выход', 'Вы вышли из аккаунта') },
    { id: '5', title: '🗑️ Удалить аккаунт', action: () => {
        Alert.alert('Удалить аккаунт', 'Вы уверены?', [
          { text: 'Отмена', style: 'cancel' },
          { text: 'Удалить', style: 'destructive', onPress: () => console.log('Аккаунт удален') }
        ])
      }, danger: true
    },
  ];

  const handleItemPress = (item) => {
    if (item.route) {
      router.push(item.route);
    } else if (item.action) {
      item.action();
    }
  };

  const renderItem = ({ item }) => (
    <TouchableOpacity onPress={() => handleItemPress(item)} style={styles.menuItem}>
      <Text style={[styles.menuText, item.danger && styles.dangerText]}>{item.title}</Text>
    </TouchableOpacity>
  );

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.profileBlock}>
        {user ? (
          <>
            <Text style={styles.name}>{user.firstName} {user.lastName}</Text>
            <Text style={styles.email}>{user.email}</Text>
          </>
        ) : (
          <Text style={styles.email}>Загрузка профиля...</Text>
        )}
      </View>
      <FlatList
        data={menuItems}
        renderItem={renderItem}
        keyExtractor={(item) => item.id}
        ItemSeparatorComponent={() => <View style={styles.separator} />}
        contentContainerStyle={styles.list}
      />
    </SafeAreaView>
  );
};

export default ProfileScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    padding: 16,
    backgroundColor: '#fff'
  },
  avatar: {
    width: 100,
    height: 100,
    borderRadius: 50,
    marginBottom: 16
  },
  name: {
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 8
  },
  email: {
    fontSize: 16,
    marginBottom: 8
  },
  bio: {
    fontSize: 14,
    color: 'gray',
    marginBottom: 8
  },
  bodyWeight: {
    fontSize: 16,
    marginBottom: 16
  },
  buttonContainer: {
    marginVertical: 5,
    width: '80%'
  }
});