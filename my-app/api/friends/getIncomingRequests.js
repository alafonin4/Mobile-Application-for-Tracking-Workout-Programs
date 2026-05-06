import axios from 'axios';

export const getIncomingRequests = async (userId) => {
  try {
    const response = await axios.get(`http://10.110.84.11:8083/api/friendRequests/received/pending/${userId}`);
    return response.data;
  } catch (error) {
    console.error('Ошибка при получении входящих заявок:', error);
    return []; // Возвращаем пустой массив вместо выброса ошибки
  }
};
