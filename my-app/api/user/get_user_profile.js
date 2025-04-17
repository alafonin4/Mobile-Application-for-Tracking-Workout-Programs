import axios from "axios";

export const get_user_profile = async (userId) => {
  try {
    const response = await axios.get(`http://192.168.116.232:8083/api/users/${userId}`);
    return response.data;
  } catch (error) {
    console.error("Ошибка при получении профиля пользователя:", error);
    throw error;
  }
};
