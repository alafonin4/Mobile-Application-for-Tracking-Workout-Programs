import axios from 'axios';
import { API_URL } from '../../constants/api_url';

export const createWorkout = async (workoutData) => {
  const response = await axios.post("http://192.168.116.232:8083/workouts", workoutData);
  return response.data;
};