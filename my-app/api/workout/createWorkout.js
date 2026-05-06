import axios from "axios";
import { API_URL } from "../../constants/api_url";

export const createWorkout = async (workoutData) => {
  const response = await axios.post(`http://10.110.84.11:8083/api/workouts`, workoutData);
  return response.data;
};
