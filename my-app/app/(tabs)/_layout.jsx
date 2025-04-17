import { Tabs } from 'expo-router';
import { Ionicons } from '@expo/vector-icons';

export default function TabsLayout() {
  return (
    <Tabs
      screenOptions={({ route }) => ({
        tabBarIcon: ({ color, size }) => {
          let iconName;

          if (route.name === '(friends)') iconName = 'people';
          else if (route.name === '(profile)') iconName = 'person';
          else if (route.name === '(workout)') iconName = 'barbell';

          return <Ionicons name={iconName} size={size} color={color} />;
        },
        tabBarActiveTintColor: '#007AFF',
        tabBarInactiveTintColor: 'gray',
      })}
    >
      <Tabs.Screen name="(friends)" options={{ title: 'Friends' }} />
      <Tabs.Screen name="(profile)" options={{ title: 'Profile' }} />
      <Tabs.Screen name="(workout)" options={{ title: 'Workout' }} />
    </Tabs>
  );
}
