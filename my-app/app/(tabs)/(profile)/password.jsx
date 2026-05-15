import { Alert, SafeAreaView, StyleSheet, Text, TouchableOpacity, View } from "react-native";
import { KeyboardAwareScrollView } from "react-native-keyboard-aware-scroll-view";
import { Formik } from "formik";
import { useRouter } from "expo-router";

import { change_password } from "../../../api/auth/change_password";
import { getApiErrorMessage, getApiErrorStatus } from "../../../api/client";
import { FormField } from "../../../components/FormField";
import { useUserId } from "../../../hooks/useUserId";
import { editPasswordValidationSchema } from "../../../validation/editPasswordValidation";

const isFormReady = (isValid, dirty, isSubmitting) => isValid && dirty && !isSubmitting;

export default function ChangePasswordScreen() {
  const router = useRouter();
  const [userId] = useUserId();

  const onSubmit = async (values, { setSubmitting, resetForm }) => {
    if (userId === null) {
      setSubmitting(false);
      return;
    }

    try {
      await change_password(userId, values.currentPassword, values.newPassword);
      resetForm();
      Alert.alert("Успех", "Пароль изменен.", [
        {
          text: "OK",
          onPress: () => router.back(),
        },
      ]);
    } catch (error) {
      const status = getApiErrorStatus(error);
      const fallback =
        status === 400
          ? "Текущий пароль указан неверно."
          : "Не удалось изменить пароль.";

      Alert.alert("Ошибка", getApiErrorMessage(error, fallback));
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
          initialValues={{
            currentPassword: "",
            newPassword: "",
            confirmNewPassword: "",
          }}
          validationSchema={editPasswordValidationSchema}
          onSubmit={onSubmit}
        >
          {({
            handleBlur,
            handleChange,
            handleSubmit,
            values,
            errors,
            touched,
            isValid,
            dirty,
            isSubmitting,
          }) => (
            <View style={styles.card}>
              <FormField
                field="currentPassword"
                label="Текущий пароль"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
                secureTextEntry
              />
              <FormField
                field="newPassword"
                label="Новый пароль"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
                secureTextEntry
              />
              <FormField
                field="confirmNewPassword"
                label="Подтвердите новый пароль"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
                secureTextEntry
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
                  {isSubmitting ? "Изменение..." : "Изменить пароль"}
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
