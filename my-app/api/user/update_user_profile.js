import { apiPut } from "../client";

export const update_user_profile = async (userId, profile) => {
  return apiPut(`/api/users/${userId}`, profile);
};
