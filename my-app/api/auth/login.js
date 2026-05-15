import { apiPost } from "../client";

export const login = async (email, password) => {
  return apiPost("/api/auth/login", { email, password });
};
