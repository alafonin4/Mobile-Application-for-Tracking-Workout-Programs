// api/friends/getFriends.js
import axios from 'axios';
import { API_URL } from '../../constants/api_url';

export const getFriends = async (userId) => {
  try {
    const url = `http://10.110.84.11:8083/api/friendRequests/approved/${userId}`;
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    if (error.response?.status === 404) {
      console.warn('Список друзей пуст (404)');
      return []; // Возвращаем пустой массив, если друзей нет
    }

    console.error('Ошибка при получении списка друзей:', error);
    throw error;
  }
};
