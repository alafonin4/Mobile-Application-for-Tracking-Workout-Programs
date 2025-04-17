import {Stack, useRouter} from "expo-router";
import {TouchableOpacity} from "react-native";
import {globalStyles, HEADER_BACKGROUND} from "../../styles/globalStyles";
import Ionicons from "@expo/vector-icons/Ionicons";
import React from "react";

const AuthLayout = () => {
  const router = useRouter();

  return (
    <Stack screenOptions={{
      headerStyle: {backgroundColor: HEADER_BACKGROUND},
      headerTitleStyle: globalStyles.headerText,
      headerTitleAlign: "center"
    }}>
      <Stack.Screen name={"index"} options={{title: "Вход"}}/>
      <Stack.Screen
        name={"sign_up"}
        options={{
          title: "Регистрация",
          headerLeft: () => (
            <TouchableOpacity style={globalStyles.backButton} onPress={() => router.back()}>
              <Ionicons name="chevron-back" size={24} color="black"/>
            </TouchableOpacity>
          )
        }}
      />
    </Stack>
  )
}

export default AuthLayout;