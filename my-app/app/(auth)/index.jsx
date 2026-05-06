import { SafeAreaView, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import { KeyboardAwareScrollView } from "react-native-keyboard-aware-scroll-view";
import { Formik } from "formik";
import { Link, router } from "expo-router";
import { globalStyles } from "../../styles/globalStyles";
import { authorizationValidationSchema } from "../../validation/authorizationValidation";
import { FormField } from "../../components/FormField";
import { useSession } from "../../context/ctx";
import { login } from "../../api/auth/login";
import { useState } from "react";
import { useUserId } from "../../hooks/useUserId";
import { useStorageState } from "./../../hooks/useStorageState";


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
      console.log(data);
      if (!data || !data.id) {
        throw new Error("Не удалось получить ID из ответа.");
      }
      console.log("user от user-сервиса:", data);
      await setUserId(data.id);

      signIn("Student");
      router.push("/(tabs)/(profile)");
    } catch (error) {
      console.error("Login failed:", error);

      console.log(error.response?.status)
      if (error.response?.status === 404) {
        setLoginError("Неправильный email или пароль."); 
      } else {
        setLoginError("Ошибка сервера. Попробуйте позже.");
      }
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
                label="Электронная почта"
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
              />

              <FormField
                field="password"
                label="Пароль"
                secureTextEntry={true}
                values={values}
                touched={touched}
                errors={errors}
                handleChange={handleChange}
                handleBlur={handleBlur}
              />

              {loginError && <Text style={styles.errorText}>{loginError}</Text>}

              <TouchableOpacity onPress={handleSubmit}>
                <View
                  style={[
                    globalStyles.signInButton,
                    { opacity: isFormValid(isValid, touched) ? 1 : 0.4 }
                  ]}
                >
                  <Text style={globalStyles.buttonText}>Войти</Text>
                </View>
              </TouchableOpacity>

              <View style={styles.linkContainer}>
                <Text style={styles.footerText}>Нет аккаунта?</Text>
                <Link href={"/sign_up"} style={styles.link}>Регистрация</Link>
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
    marginTop: 10
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
    fontSize: 14
  }
});

export default Index;
