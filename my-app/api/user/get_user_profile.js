import { apiGet } from "../client";

export const get_user_profile = async (userId) => {
  return apiGet(`/api/users/${userId}`);
};
