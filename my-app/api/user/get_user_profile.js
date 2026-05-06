import axios from "axios";
import { API_URL } from "../../constants/api_url";

export const get_user_profile = async (userId) => {
  try {
    const response = await axios.get(`http://10.110.84.11:8083/api/users/${userId}`);
    return response.data;
  } catch (error) {
    console.error("Ошибка при получении профиля пользователя:", error);
    throw error;
  }
};
