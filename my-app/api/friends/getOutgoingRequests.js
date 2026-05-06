import axios from 'axios';

export const getOutgoingRequests = async (userId) => {
  try {
    const response = await axios.get(`http://10.110.84.11:8083/api/friendRequests/sent/pending/${userId}`);
    return response.data;
  } catch (error) {
    console.error('Ошибка при получении исходящих заявок:', error);
    return []; // Возвращаем пустой массив вместо выброса ошибки
  }
};
