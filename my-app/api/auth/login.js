import axios from "axios";
import { API_URL } from "../../constants/api_url";

export const login = async (email, password) => {
  try {
    const response = await axios.post(`http://10.110.84.11:8083/api/auth/login`, { email, password });
    return response.data;
  } catch (error) {
    console.error("Login failed:", error.response?.status, error);
    throw error;
  }
};
