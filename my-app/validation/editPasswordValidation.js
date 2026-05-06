import * as Yup from "yup";

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
