import axios from "axios";
import { API_URL } from "../../constants/api_url";

export const fetchUserWorkouts = async (userId) => {
  const response = await axios.get(`http://10.110.84.11:8083/api/workouts/user/${userId}`);
  return response.data;
};

export const fetchUserPrograms = async (userId) => {
  const response = await axios.get(`http://10.110.84.11:8083/api/training-programs/user/${userId}`);
  return response.data;
};
