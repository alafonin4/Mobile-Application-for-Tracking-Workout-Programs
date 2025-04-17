import axios from 'axios';

export const getOutgoingRequests = async (userId) => {
  try {
    const response = await axios.get(`http://192.168.116.232:8083/api/friendRequests/sent/pending/${userId}`);
    return response.data;
  } catch (error) {
    console.error('Ошибка при получении исходящих заявок:', error);
    return []; // Возвращаем пустой массив вместо выброса ошибки
  }
};
