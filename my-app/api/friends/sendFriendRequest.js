import { apiPost } from "../client";

export const sendFriendRequest = async (senderId, receiverId) => {
  return apiPost("/api/friendRequests/", {
    senderId,
    receiverId,
  });
};
