export const getAllExercises = async () => {
    try {
      const response = await fetch('https://your-api.com/api/exercises'); // Заменить на реальный URL
      if (!response.ok) {
        throw new Error('Ошибка загрузки упражнений');
      }
      return await response.json();
    } catch (error) {
      console.error('Ошибка при получении упражнений:', error);
      return [];
    }
  };
  