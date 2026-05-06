import { API_URL } from "../../constants/api_url";

export const getAllExercises = async () => {
  try {
    const response = await fetch(`http://10.110.84.11:8083/api/exercises`);
    if (!response.ok) {
      throw new Error("Failed to load exercises");
    }
    return await response.json();
  } catch (error) {
    console.error("Failed to load exercises:", error);
    return [];
  }
};
