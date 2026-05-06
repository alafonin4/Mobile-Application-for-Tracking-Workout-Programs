import { Stack } from "expo-router";

export default function WorkoutLayout() {
  return (
    <Stack screenOptions={{ headerTitleAlign: "center" }}>
      <Stack.Screen name="index" options={{ headerShown: false }} />
      <Stack.Screen name="create-workout" options={{ title: "Новая тренировка" }} />
      <Stack.Screen name="create-program" options={{ title: "Новая программа" }} />
      <Stack.Screen name="progress" options={{ headerShown: false }} />
      <Stack.Screen name="workout-details" options={{ title: "Тренировка" }} />
      <Stack.Screen name="program-details" options={{ title: "Программа" }} />
    </Stack>
  );
}
