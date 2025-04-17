export const getExerciseById = async (id) => {
    try {
      const response = await fetch(`https://your-api.com/api/exercises/${id}`); // Замени на свой реальный URL
      if (!response.ok) {
        throw new Error('Не удалось получить упражнение');
      }
      return await response.json();
    } catch (error) {
      console.error(`Ошибка при получении упражнения с ID ${id}:`, error);
      return null;
    }
  };
  