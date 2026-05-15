import { apiPost } from "../client";

export const createProgram = async (programData) => {
  return apiPost("/api/training-programs", programData);
};
