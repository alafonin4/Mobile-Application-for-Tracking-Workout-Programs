package ru.alafonin4.socialservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.alafonin4.socialservice.dto.FriendRelationshipResponse;
import ru.alafonin4.socialservice.dto.FriendRequestDTO;
import ru.alafonin4.socialservice.entities.FriendRequest;
import ru.alafonin4.socialservice.enums.FriendRequestStatus;
import ru.alafonin4.socialservice.repositories.FriendRequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    public FriendRequest sendFriendRequest(FriendRequestDTO req) {
        validateDifferentUsers(req.getSenderId(), req.getReceiverId());

        List<FriendRequest> existingRelations = getActiveRelations(req.getSenderId(), req.getReceiverId());
        if (!existingRelations.isEmpty()) {
            FriendRequest relation = existingRelations.get(0);
            if (relation.getStatus() == FriendRequestStatus.ACCEPTED) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Users are already friends.");
            }
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A pending friend request already exists.");
        }

        FriendRequest request = new FriendRequest();
        request.setReceiverId(req.getReceiverId());
        request.setSenderId(req.getSenderId());
        request.setStatus(FriendRequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        return friendRequestRepository.save(request);
    }

    public FriendRequest acceptFriendRequest(Long requestId) {
        FriendRequest request = getRequest(requestId);
        request.setStatus(FriendRequestStatus.ACCEPTED);
        request.setRespondedAt(LocalDateTime.now());
        return friendRequestRepository.save(request);
    }

    public FriendRequest rejectFriendRequest(Long requestId) {
        FriendRequest request = getRequest(requestId);
        request.setStatus(FriendRequestStatus.REJECTED);
        request.setRespondedAt(LocalDateTime.now());
        return friendRequestRepository.save(request);
    }

    public FriendRequest cancelFriendRequest(Long requestId, Long currentUserId) {
        FriendRequest request = getRequest(requestId);
        if (!request.getSenderId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the sender can cancel a pending friend request.");
        }
        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only pending friend requests can be canceled.");
        }

        request.setStatus(FriendRequestStatus.CANCELED);
        request.setRespondedAt(LocalDateTime.now());
        return friendRequestRepository.save(request);
    }

    public FriendRequest removeFriend(Long requestId, Long currentUserId) {
        FriendRequest request = getRequest(requestId);
        if (request.getStatus() != FriendRequestStatus.ACCEPTED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only accepted friend requests can be reverted to pending.");
        }
        if (!request.getSenderId().equals(currentUserId) && !request.getReceiverId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not related to the selected friend request.");
        }

        Long otherUserId = request.getSenderId().equals(currentUserId)
                ? request.getReceiverId()
                : request.getSenderId();

        request.setSenderId(otherUserId);
        request.setReceiverId(currentUserId);
        request.setStatus(FriendRequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        request.setRespondedAt(null);
        return friendRequestRepository.save(request);
    }

    public FriendRelationshipResponse getRelationship(Long userId, Long otherUserId) {
        FriendRelationshipResponse response = new FriendRelationshipResponse();
        if (userId.equals(otherUserId)) {
            response.setRelationType("SELF");
            return response;
        }

        List<FriendRequest> relations = getActiveRelations(userId, otherUserId);
        if (relations.isEmpty()) {
            response.setRelationType("NONE");
            return response;
        }

        FriendRequest relation = relations.get(0);
        response.setRequestId(relation.getId());
        response.setSenderId(relation.getSenderId());
        response.setReceiverId(relation.getReceiverId());
        response.setStatus(relation.getStatus());

        if (relation.getStatus() == FriendRequestStatus.ACCEPTED) {
            response.setRelationType("FRIENDS");
        } else if (relation.getSenderId().equals(userId)) {
            response.setRelationType("OUTGOING_PENDING");
        } else {
            response.setRelationType("INCOMING_PENDING");
        }

        return response;
    }

    public List<FriendRequest> getReceivedFriendRequests(Long receiverId) {
        return friendRequestRepository.findByReceiverIdAndStatus(receiverId, FriendRequestStatus.PENDING);
    }

    public List<FriendRequest> getSentFriendRequests(Long senderId) {
        return friendRequestRepository.findBySenderIdAndStatus(senderId, FriendRequestStatus.PENDING);
    }

    public List<FriendRequest> getApprovedFriendRequests(Long userId) {
        return friendRequestRepository.findBySenderIdOrReceiverIdAndStatus(userId, userId, FriendRequestStatus.ACCEPTED);
    }

    public List<FriendRequest> getPendingSentRequests(Long senderId) {
        return friendRequestRepository.findBySenderIdAndStatus(senderId, FriendRequestStatus.PENDING);
    }

    public List<FriendRequest> getPendingReceivedRequests(Long receiverId) {
        return friendRequestRepository.findByReceiverIdAndStatus(receiverId, FriendRequestStatus.PENDING);
    }

    private FriendRequest getRequest(Long requestId) {
        return friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Friend request not found with id " + requestId));
    }

    private List<FriendRequest> getActiveRelations(Long userId, Long otherUserId) {
        return friendRequestRepository.findRelationsBetweenUsers(
                userId,
                otherUserId,
                List.of(FriendRequestStatus.PENDING, FriendRequestStatus.ACCEPTED)
        );
    }

    private void validateDifferentUsers(Long userId, Long otherUserId) {
        if (userId == null || otherUserId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender and receiver must be provided.");
        }
        if (userId.equals(otherUserId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot create a friend request to themselves.");
        }
    }
}
