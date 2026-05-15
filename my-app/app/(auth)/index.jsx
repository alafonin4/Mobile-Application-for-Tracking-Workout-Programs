import { SafeAreaView, StyleSheet, Text, TouchableOpacity, View } from "react-native";
import { KeyboardAwareScrollView } from "react-native-keyboard-aware-scroll-view";
import { Formik } from "formik";
import { Link, router } from "expo-router";
import { useState } from "react";

import { login } from "../../api/auth/login";
import { getApiErrorMessage, getApiErrorStatus } from "../../api/client";
import { FormField } from "../../components/FormField";
import { useSession } from "../../context/ctx";
import { useUserId } from "../../hooks/useUserId";
import { globalStyles } from "../../styles/globalStyles";
import { authorizationValidationSchema } from "../../validation/authorizationValidation";

const isFormValid = (isValid, touched) => {
  return isValid && Object.keys(touched).length !== 0;
};

const Index = () => {
  const { signIn } = useSession();
  const [, setUserId] = useUserId();
  const [loginError, setLoginError] = useState(null);

  const onSignInHandler = async (email, password) => {
    try {
      setLoginError(null);
      const data = await login(email, password);

      if (!data || !data.id) {
        throw new Error("Не удалось получить ID пользователя из ответа сервера.");
      }

      await setUserId(data.id);
      signIn("Student");
      router.push("/(tabs)/(profile)");
    } catch (error) {
      const status = getApiErrorStatus(error);
      const fallback =
        status === 401 || status === 404
          ? "Неправильный email или пароль."
          : "Не удалось войти. Попробуйте позже.";

      setLoginError(getApiErrorMessage(error, fallback));
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
          initialValues={{ email: "", password: "" }}
          onSubmit={(values) => onSignInHandler(values.email, values.password)}
          validationSchema={authorizationValidationSchema}
        >
          {({
            handleChange,
            handleBlur,
            handleSubmit,
            values,
            errors,
            touched,
            isValid,
          }) => (
            <>
              <FormField
                field="email"
                label="Email"
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

              {loginError ? <Text style={styles.errorText}>{loginError}</Text> : null}

              <TouchableOpacity onPress={handleSubmit}>
                <View
                  style={[
                    globalStyles.signInButton,
                    { opacity: isFormValid(isValid, touched) ? 1 : 0.4 },
                  ]}
                >
                  <Text style={globalStyles.buttonText}>Вход</Text>
                </View>
              </TouchableOpacity>

              <View style={styles.linkContainer}>
                <Text style={styles.footerText}>Нет аккаунта?</Text>
                <Link href="/sign_up" style={styles.link}>
                  Зарегистрироваться
                </Link>
              </View>
            </>
          )}
        </Formik>
      </KeyboardAwareScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  errorText: {
    color: "red",
    textAlign: "center",
    marginVertical: 10,
    fontSize: 14,
  },
  linkContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    flexDirection: "row",
    marginTop: 10,
  },
  link: {
    color: "#d903e4",
    fontFamily: "os-bold",
    fontSize: 14,
    textDecorationLine: "underline",
  },
  footerText: {
    fontFamily: "os-regular",
    marginRight: 5,
    fontSize: 14,
  },
});

export default Index;
