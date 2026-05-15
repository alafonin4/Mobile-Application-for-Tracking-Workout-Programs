import { apiPut } from "../client";

export const updateProgram = async (programId, programData) => {
  return apiPut(`/api/training-programs/${programId}`, programData);
};
