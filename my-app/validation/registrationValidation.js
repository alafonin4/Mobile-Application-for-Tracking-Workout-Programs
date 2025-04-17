import * as Yup from "yup";

const emailRegExp = /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/

export const registrationValidationSchema = Yup.object().shape({
  firstName: Yup.string().required("Имя обязательно"),
  lastName: Yup.string().required("Фамилия обязательна"),
  email: Yup.string()
    .matches(emailRegExp, "Email некорректен")
    .required("Пожалуйста, введите email"),
  password: Yup.string()
    .required("Пожалуйста, введите пароль")
    .min(6, "Пароль должен содержать минимум 6 символов"),
  confirmPassword: Yup.string()
    .required("Пожалуйста, подтвердите пароль")
    .oneOf([Yup.ref("password")], "Пароли не совпадают"),
});