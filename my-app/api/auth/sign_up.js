import { apiPost } from "../client";

export const sign_up = async (firstName, lastName, email, password) => {
  return apiPost("/api/auth/register", {
    firstName,
    lastName,
    email,
    password,
  });
};
