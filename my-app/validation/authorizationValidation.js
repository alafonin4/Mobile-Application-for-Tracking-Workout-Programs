import * as Yup from "yup";

const emailRegExp = /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/

export const authorizationValidationSchema = Yup.object().shape({
  email: Yup.string()
    .matches(emailRegExp, "Email некорректен")
    .required("Пожалуйста, введите зарегистрированный email"),
  password: Yup.string()
    .required("Пожалуйста, введите пароль")
    .min(6, "Пароль должен содержать минимум 6 символов"),
});