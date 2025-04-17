import { Image, StyleSheet, Platform } from 'react-native';
import { View, Text, FlatList, TouchableOpacity, Alert } from 'react-native';
import { useRouter } from 'expo-router';
import { useEffect, useState } from 'react';


import { useUserId } from '../../../hooks/useUserId';
import { get_user_profile } from '../../../api/user/get_user_profile';

const ProfileScreen = () => {
  const router = useRouter();
  const [user, setUser] = useState(null);
  const [userId] = useUserId();

  useEffect(() => {
    const fetchUser = async () => {
      try {
        if (!userId) return;
        const profile = await get_user_profile(userId);
        setUser(profile);
      } catch (error) {
        console.warn("Не удалось загрузить профиль:", error);
      }
    };

    fetchUser();
  }, [userId]);

  const menuItems = [
    { id: '1', title: '✏️ Редактировать профиль', route: '/profile/edit' },
    { id: '2', title: '🔒 Изменить пароль', route: '/profile/password' },
    { id: '3', title: '👥 Список друзей', route: '/profile/friends' },
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
    <View style={styles.container}>
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
    </View>
  );
};

export default ProfileScreen;
