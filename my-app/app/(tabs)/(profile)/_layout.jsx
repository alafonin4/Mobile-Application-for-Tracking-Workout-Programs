import { Stack } from "expo-router";

export default function ProfileLayout() {
  return (
    <Stack
      screenOptions={{
        headerTitleAlign: "center",
      }}
    >
      <Stack.Screen name="index" options={{ headerShown: false }} />
      <Stack.Screen name="edit" options={{ title: "Изменение профиля" }} />
      <Stack.Screen name="password" options={{ title: "Изменение пароля" }} />
    </Stack>
  );
}
