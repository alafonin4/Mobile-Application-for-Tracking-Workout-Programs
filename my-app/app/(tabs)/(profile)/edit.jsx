import { useEffect, useState } from "react";
import {
  Alert,
  Image,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from "react-native";
import { KeyboardAwareScrollView } from "react-native-keyboard-aware-scroll-view";
import { Formik } from "formik";
import { useRouter } from "expo-router";
import * as ImagePicker from "expo-image-picker";

import { getApiErrorMessage } from "../../../api/client";
import { FormField } from "../../../components/FormField";
import { get_user_profile } from "../../../api/user/get_user_profile";
import { update_user_profile } from "../../../api/user/update_user_profile";
import { useUserId } from "../../../hooks/useUserId";
import { editUserInfoValidationSchema } from "../../../validation/validation";
import { FITNESS_GOALS } from "../../../utils/profile";

const isFormReady = (isValid, dirty, isSubmitting) => isValid && dirty && !isSubmitting;

const buildAvatarDataUri = (asset) => {
  if (!asset?.base64) {
    return null;
  }

  const mimeType = asset.mimeType || "image/jpeg";
  return `data:${mimeType};base64,${asset.base64}`;
};

const getInitials = (firstName, lastName) => {
  const parts = [firstName, lastName].filter(Boolean);
  if (!parts.length) {
    return "U";
  }

  return parts
    .map((part) => part[0]?.toUpperCase() ?? "")
    .join("")
    .slice(0, 2);
};

export default function EditProfileScreen() {
  const router = useRouter();
  const [userId, , isLoaded] = useUserId();
  const [initialValues, setInitialValues] = useState({
    firstName: "",
    lastName: "",
    email: "",
    bio: "",
    bodyWeight: "",
    avatarUrl: null,
    fitnessGoal: "GENERAL_FITNESS",
  });

  useEffect(() => {
    const loadProfile = async () => {
      if (!isLoaded || userId === null) {
        return;
      }

      try {
        const profile = await get_user_profile(userId);
        setInitialValues({
          firstName: profile.firstName ?? "",
          lastName: profile.lastName ?? "",
          email: profile.email ?? "",
          bio: profile.bio ?? "",
          bodyWeight:
            profile.bodyWeight === null || profile.bodyWeight === undefined
              ? ""
              : String(profile.bodyWeight),
          avatarUrl: profile.avatarUrl ?? null,
          fitnessGoal: profile.fitnessGoal ?? "GENERAL_FITNESS",
        });
      } catch (error) {
        Alert.alert(
          "Ошибка",
          getApiErrorMessage(error, "Не удалось загрузить данные профиля.")
        );
      }
    };

    loadProfile();
  }, [isLoaded, userId]);

  const handlePickAvatar = async (setFieldValue) => {
    try {
      const permission = await ImagePicker.requestMediaLibraryPermissionsAsync();
      if (!permission.granted) {
        Alert.alert("Нет доступа", "Нужно разрешение на доступ к галерее.");
        return;
      }

      const result = await ImagePicker.launchImageLibraryAsync({
        mediaTypes: ImagePicker.MediaTypeOptions.Images,
        allowsEditing: true,
        aspect: [1, 1],
        quality: 0.55,
        base64: true,
      });

      if (result.canceled || !result.assets?.length) {
        return;
      }

      const avatarUrl = buildAvatarDataUri(result.assets[0]);
      if (!avatarUrl) {
        Alert.alert("Ошибка", "Не удалось подготовить изображение для сохранения.");
        return;
      }

      setFieldValue("avatarUrl", avatarUrl);
    } catch (error) {
      Alert.alert("Ошибка", "Не удалось выбрать изображение из галереи.");
    }
  };

  const onSubmit = async (values, { setSubmitting }) => {
    if (userId === null) {
      setSubmitting(false);
      return;
    }

    try {
      await update_user_profile(userId, {
        firstName: values.firstName.trim(),
        lastName: values.lastName.trim(),
        email: values.email.trim(),
        bio: values.bio.trim(),
        bodyWeight: values.bodyWeight === "" ? 0 : Number(values.bodyWeight),
        avatarUrl: values.avatarUrl,
        fitnessGoal: values.fitnessGoal,
      });

      Alert.alert("Успех", "Профиль обновлён.", [
        {
          text: "OK",
          onPress: () => router.back(),
        },
      ]);
    } catch (error) {
      Alert.alert(
        "Ошибка",
        getApiErrorMessage(error, "Не удалось сохранить профиль.")
      );
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <KeyboardAwareScrollView
        contentContainerStyle={styles.content}
        keyboardShouldPersistTaps="handled"
      >
        <Formik
          enableReinitialize
          initialValues={initialValues}
          validationSchema={editUserInfoValidationSchema}
          onSubmit={onSubmit}
        >
          {({
            handleBlur,
            handleChange,
            handleSubmit,
            setFieldValue,
            values,
            errors,
            touched,
            isValid,
            dirty,
            isSubmitting,
          }) => (
            <View style={styles.card}>
              <View style={styles.avatarSection}>
                {values.avatarUrl ? (
                  <Image source={{ uri: values.avatarUrl }} style={styles.avatar} />
                ) : (
                  <View style={styles.avatarFallback}>
                    <Text style={styles.avatarFallbackText}>
                      {getInitials(values.firstName, values.lastName)}
                    </Text>
                  </View>
                )}

                <View style={styles.avatarActions}>
                  <TouchableOpacity
                    style={styles.avatarButton}
                    onPress={() => handlePickAvatar(setFieldValue)}
                  >
                    <Text style={styles.avatarButtonText}>Выбрать фото</Text>
                  </TouchableOpacity>

                  {values.avatarUrl ? (
                    <TouchableOpacity
                      style={[styles.avatarButton, styles.removeAvatarButton]}
                      onPress={() => setFieldValue("avatarUrl", null)}
                    >
                      <Text style={[styles.avatarButtonText, styles.removeAvatarButtonText]}>
                        Удалить фото
                      </Text>
                    </TouchableOpacity>
                  ) : null}
                </View>
              </View>

              <FormField
                field="firstName"
                label="Имя"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
                autoCapitalize="words"
              />
              <FormField
                field="lastName"
                label="Фамилия"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
                autoCapitalize="words"
              />
              <FormField
                field="email"
                label="Email"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
                keyboardType="email-address"
                editable={false}
              />
              <FormField
                field="bodyWeight"
                label="Вес"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
                keyboardType="numeric"
              />

              <Text style={styles.goalLabel}>Цель тренировок</Text>
              <View style={styles.goalWrap}>
                {FITNESS_GOALS.map((goal) => {
                  const isSelected = values.fitnessGoal === goal.value;
                  return (
                    <TouchableOpacity
                      key={goal.value}
                      style={[styles.goalChip, isSelected && styles.goalChipActive]}
                      onPress={() => setFieldValue("fitnessGoal", goal.value)}
                    >
                      <Text style={[styles.goalChipText, isSelected && styles.goalChipTextActive]}>
                        {goal.label}
                      </Text>
                    </TouchableOpacity>
                  );
                })}
              </View>

              <FormField
                field="bio"
                label="О себе"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
                multiline
                autoCapitalize="sentences"
              />

              <TouchableOpacity
                style={[
                  styles.button,
                  !isFormReady(isValid, dirty, isSubmitting) && styles.buttonDisabled,
                ]}
                disabled={!isFormReady(isValid, dirty, isSubmitting)}
                onPress={handleSubmit}
              >
                <Text style={styles.buttonText}>
                  {isSubmitting ? "Сохранение..." : "Сохранить изменения"}
                </Text>
              </TouchableOpacity>
            </View>
          )}
        </Formik>
      </KeyboardAwareScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#F4F7FB",
  },
  content: {
    padding: 20,
  },
  card: {
    backgroundColor: "#FFFFFF",
    borderRadius: 20,
    padding: 20,
  },
  avatarSection: {
    alignItems: "center",
    marginBottom: 20,
  },
  avatar: {
    width: 112,
    height: 112,
    borderRadius: 56,
    marginBottom: 14,
  },
  avatarFallback: {
    width: 112,
    height: 112,
    borderRadius: 56,
    marginBottom: 14,
    backgroundColor: "#DBEAFE",
    alignItems: "center",
    justifyContent: "center",
  },
  avatarFallbackText: {
    color: "#1D4ED8",
    fontSize: 34,
    fontWeight: "700",
  },
  avatarActions: {
    width: "100%",
    gap: 10,
  },
  avatarButton: {
    backgroundColor: "#E0E7FF",
    borderRadius: 14,
    paddingVertical: 14,
    alignItems: "center",
  },
  avatarButtonText: {
    color: "#1D4ED8",
    fontSize: 15,
    fontWeight: "700",
  },
  removeAvatarButton: {
    backgroundColor: "#FEE2E2",
  },
  removeAvatarButtonText: {
    color: "#B91C1C",
  },
  goalLabel: {
    fontSize: 16,
    color: "#333",
    marginBottom: 8,
  },
  goalWrap: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: 8,
    marginBottom: 16,
  },
  goalChip: {
    borderRadius: 999,
    paddingHorizontal: 12,
    paddingVertical: 10,
    backgroundColor: "#E2E8F0",
  },
  goalChipActive: {
    backgroundColor: "#2563EB",
  },
  goalChipText: {
    color: "#334155",
    fontWeight: "600",
  },
  goalChipTextActive: {
    color: "#FFFFFF",
  },
  button: {
    backgroundColor: "#2563EB",
    borderRadius: 14,
    paddingVertical: 16,
    alignItems: "center",
    marginTop: 8,
  },
  buttonDisabled: {
    opacity: 0.5,
  },
  buttonText: {
    color: "#FFFFFF",
    fontSize: 16,
    fontWeight: "700",
  },
});
