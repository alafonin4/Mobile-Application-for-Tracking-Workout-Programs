// frontend/api/workout/fetchUserData.js
import axios from 'axios';
import { API_URL } from '../../constants/api_url';

export const fetchUserWorkouts = async (userId) => {
  try {
    // Формируем URL запроса. Предположим, что на сервере endpoint выглядит так: /api/workouts/userinfo/{userId}
    const url = "$http://192.168.116.232:8083/api/workouts/user/${userId}";
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error('Ошибка при получении данных пользователя:', error);
    throw error;
  }
};

export const fetchUserPrograms = async (userId) => {
  try {
    // Формируем URL запроса. Предположим, что на сервере endpoint выглядит так: /api/workouts/userinfo/{userId}
    const url = "$http://192.168.116.232:8083/api/training-programs/user/${userId}";
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    console.error('Ошибка при получении данных пользователя:', error);
    throw error;
  }
};