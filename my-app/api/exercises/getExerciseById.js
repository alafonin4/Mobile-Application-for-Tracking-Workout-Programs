import { API_URL } from "../../constants/api_url";

export const getExerciseById = async (id) => {
  try {
    const response = await fetch(`http://10.110.84.11:8083/api/exercises/${id}`);
    if (!response.ok) {
      throw new Error("Failed to load exercise");
    }
    return await response.json();
  } catch (error) {
    console.error(`Failed to load exercise ${id}:`, error);
    return null;
  }
};
