import { apiGet } from "../client";

export const getFriendRelationship = async (userId, otherUserId) => {
  return apiGet(`/api/friendRequests/relationship/${userId}/${otherUserId}`);
};
