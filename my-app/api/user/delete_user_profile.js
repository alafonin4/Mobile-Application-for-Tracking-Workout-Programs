import { apiDelete } from "../client";

export const delete_user_profile = async (userId) => {
  await apiDelete(`/api/users/${userId}`);
};
