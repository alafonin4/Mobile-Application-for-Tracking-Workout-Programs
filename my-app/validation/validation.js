import * as Yup from "yup";

const emailRegExp = /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/;

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

export const authorizationValidationSchema = Yup.object().shape({
  email: Yup.string()
    .matches(emailRegExp, "Email некорректен")
    .required("Пожалуйста, введите зарегистрированный email"),
  password: Yup.string()
    .required("Пожалуйста, введите пароль")
    .min(6, "Пароль должен содержать минимум 6 символов"),
});

export const editUserInfoValidationSchema = Yup.object().shape({
  firstName: Yup.string()
    .required("Имя обязательно")
    .min(2, "Имя должно содержать минимум 2 символа"),
  lastName: Yup.string()
    .required("Фамилия обязательна")
    .min(2, "Фамилия должна содержать минимум 2 символа"),
  email: Yup.string()
    .required("Email обязателен")
    .matches(emailRegExp, "Некорректный email"),
  bio: Yup.string().max(500, "Описание не должно превышать 500 символов"),
  bodyWeight: Yup.number()
    .transform((value, originalValue) => (originalValue === "" ? null : value))
    .nullable()
    .min(0, "Вес не может быть отрицательным"),
});

export const editPasswordValidationSchema = Yup.object().shape({
  currentPassword: Yup.string().required("Текущий пароль обязателен"),
  newPassword: Yup.string()
    .required("Новый пароль обязателен")
    .min(8, "Пароль должен содержать минимум 8 символов")
    .matches(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/,
      "Пароль должен содержать заглавную букву, строчную букву и цифру"
    ),
  confirmNewPassword: Yup.string()
    .required("Подтверждение пароля обязательно")
    .oneOf([Yup.ref("newPassword")], "Пароли должны совпадать"),
});
