import axios from "axios";
import { API_URL } from "../../constants/api_url";

export const create_user_profile = async (id, firstName, lastName, email) => {
  try {
    const newUser = {
      id,
      firstName,
      lastName,
      bio: "",
      bodyWeight: 0,
      email
    };

    const response = await axios.post(`http://10.110.84.11:8083/api/users/create`, newUser);
    return response.data;
  } catch (error) {
    console.error("Ошибка при создании пользователя в user-сервисе:", error);
    throw error;
  }
};
