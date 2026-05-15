import { apiPut } from "../client";

export const change_password = async (userId, currentPassword, newPassword) => {
  return apiPut(`/api/auth/password/${userId}`, {
    currentPassword,
    newPassword,
  });
};
