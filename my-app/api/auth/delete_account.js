import { apiDelete } from "../client";

export const delete_account = async (userId) => {
  await apiDelete(`/api/auth/${userId}`);
};
