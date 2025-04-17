import axios from "axios";

export const create_user_profile = async (firstName, lastName, email) => {
  try {
    const newUser = {
      firstName,
      lastName,
      bio: "",
      bodyWeight: 0,
      email
    };

    const response = await axios.post("http://192.168.116.232:8083/api/users/create", newUser);
    return response.data;
  } catch (error) {
    console.error("Ошибка при создании пользователя в user-сервисе:", error);
    throw error;
  }
};
