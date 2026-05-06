import { Tabs } from "expo-router";
import { Ionicons } from "@expo/vector-icons";

export default function TabsLayout() {
  return (
    <Tabs
      screenOptions={({ route }) => ({
        headerShown: false,
        tabBarActiveTintColor: "#007AFF",
        tabBarInactiveTintColor: "gray",
        tabBarIcon: ({ color, size }) => {
          let iconName = "ellipse";

          if (route.name === "CompetitionScreen") iconName = "trophy";
          else if (route.name === "(friends)") iconName = "people";
          else if (route.name === "(workout)") iconName = "barbell";
          else if (route.name === "(exercises)") iconName = "fitness";
          else if (route.name === "(profile)") iconName = "person";

          return <Ionicons name={iconName} size={size} color={color} />;
        },
      })}
    >
      <Tabs.Screen name="CompetitionScreen" options={{ title: "Соревнования" }} />
      <Tabs.Screen name="(friends)" options={{ title: "Друзья" }} />
      <Tabs.Screen name="(workout)" options={{ title: "Тренировки" }} />
      <Tabs.Screen name="(exercises)" options={{ title: "Упражнения" }} />
      <Tabs.Screen name="(profile)" options={{ title: "Профиль" }} />
      <Tabs.Screen name="explore" options={{ href: null }} />
    </Tabs>
  );
}
