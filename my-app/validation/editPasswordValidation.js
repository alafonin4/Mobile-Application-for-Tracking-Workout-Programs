import * as Yup from "yup";

export const editPasswordValidationSchema = Yup.object().shape({
  newPassword: Yup.string()
    .required('Новый пароль обязателен')
    .min(8, 'Пароль должен содержать минимум 8 символов')
    .matches(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/,
      'Пароль должен содержать хотя бы одну заглавную букву, одну строчную букву и одну цифру'
    ),
  confirmNewPassword: Yup.string()
    .required('Подтверждение пароля обязательно')
    .oneOf([Yup.ref('newPassword')], 'Пароли должны совпадать'),
});