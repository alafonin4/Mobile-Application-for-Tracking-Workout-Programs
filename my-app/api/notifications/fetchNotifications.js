import { apiGet } from "../client";

export const fetchNotifications = async (userId) => {
  return apiGet(`/api/notifications/user/${userId}`);
};
