import { apiPost } from "../client";

export const create_user_profile = async (id, firstName, lastName, email) => {
  return apiPost("/api/users/create", {
    id,
    firstName,
    lastName,
    bio: "",
    bodyWeight: 0,
    avatarUrl: null,
    email,
  });
};
