import { Alert, SafeAreaView, Text, TouchableOpacity, View } from "react-native";
import { KeyboardAwareScrollView } from "react-native-keyboard-aware-scroll-view";
import { Formik } from "formik";
import { router } from "expo-router";

import { sign_up } from "../../api/auth/sign_up";
import { getApiErrorMessage, getApiErrorStatus } from "../../api/client";
import { create_user_profile } from "../../api/user/create_user_profile";
import { FormField } from "../../components/FormField";
import { useUserId } from "../../hooks/useUserId";
import { globalStyles } from "../../styles/globalStyles";
import { registrationValidationSchema } from "../../validation/registrationValidation";

const isFormValid = (isValid, touched) => {
  return isValid && Object.keys(touched).length !== 0;
};

const SignUp = () => {
  const [, setUserId] = useUserId();

  const onSubmitHandler = async (values, { setSubmitting }) => {
    try {
      const authUser = await sign_up(
        values.firstName,
        values.lastName,
        values.email,
        values.password
      );

      const user = await create_user_profile(
        authUser.id,
        values.firstName,
        values.lastName,
        values.email
      );

      if (!user || !user.id) {
        throw new Error("Не удалось получить пользователя из user-service.");
      }

      await setUserId(user.id);

      Alert.alert("Успех", "Регистрация прошла успешно!", [
        { text: "OK", onPress: () => router.push("/(tabs)/(profile)") },
      ]);
    } catch (error) {
      const status = getApiErrorStatus(error);
      const fallback =
        status === 409
          ? "Пользователь с таким email уже существует."
          : "Не удалось зарегистрироваться. Попробуйте снова.";

      Alert.alert("Ошибка", getApiErrorMessage(error, fallback));
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <SafeAreaView style={globalStyles.mainSafeArea}>
      <KeyboardAwareScrollView
        style={globalStyles.content}
        showsVerticalScrollIndicator={false}
        keyboardShouldPersistTaps="handled"
        extraScrollHeight={150}
      >
        <Formik
          initialValues={{
            firstName: "",
            lastName: "",
            email: "",
            password: "",
            confirmPassword: "",
          }}
          onSubmit={onSubmitHandler}
          validationSchema={registrationValidationSchema}
        >
          {({
            handleChange,
            handleBlur,
            handleSubmit,
            values,
            errors,
            touched,
            isValid,
            isSubmitting,
          }) => (
            <>
              <FormField
                field="firstName"
                label="Имя"
                autoCapitalize="words"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
              />
              <FormField
                field="lastName"
                label="Фамилия"
                autoCapitalize="words"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
              />
              <FormField
                field="email"
                label="email"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
              />
              <FormField
                field="password"
                label="Пароль"
                secureTextEntry
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
              />
              <FormField
                field="confirmPassword"
                label="Подтвердите пароль"
                secureTextEntry
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
              />

              <TouchableOpacity onPress={handleSubmit} disabled={isSubmitting}>
                <View
                  style={[
                    globalStyles.submitButton,
                    {
                      opacity: isFormValid(isValid, touched) && !isSubmitting ? 1 : 0.4,
                    },
                  ]}
                >
                  <Text style={globalStyles.buttonText}>
                    {isSubmitting ? "Регистрируется..." : "Зарегистрироваться"}
                  </Text>
                </View>
              </TouchableOpacity>
            </>
          )}
        </Formik>
      </KeyboardAwareScrollView>
    </SafeAreaView>
  );
};

export default SignUp;
