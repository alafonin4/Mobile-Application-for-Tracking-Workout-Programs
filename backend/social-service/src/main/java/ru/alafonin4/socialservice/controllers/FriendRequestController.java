package ru.alafonin4.socialservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alafonin4.socialservice.dto.FriendRelationshipResponse;
import ru.alafonin4.socialservice.dto.FriendRequestDTO;
import ru.alafonin4.socialservice.entities.FriendRequest;
import ru.alafonin4.socialservice.services.FriendRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/friendRequests")
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/")
    public ResponseEntity<FriendRequest> sendFriendRequest(@RequestBody FriendRequestDTO request) {
        FriendRequest savedRequest = friendRequestService.sendFriendRequest(request);
        return ResponseEntity.ok(savedRequest);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<FriendRequest> acceptFriendRequest(@PathVariable("id") Long requestId) {
        FriendRequest acceptedRequest = friendRequestService.acceptFriendRequest(requestId);
        return ResponseEntity.ok(acceptedRequest);
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<FriendRequest> rejectFriendRequest(@PathVariable("id") Long requestId) {
        FriendRequest rejectedRequest = friendRequestService.rejectFriendRequest(requestId);
        return ResponseEntity.ok(rejectedRequest);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<FriendRequest> cancelFriendRequest(
            @PathVariable("id") Long requestId,
            @RequestParam("currentUserId") Long currentUserId
    ) {
        FriendRequest canceledRequest = friendRequestService.cancelFriendRequest(requestId, currentUserId);
        return ResponseEntity.ok(canceledRequest);
    }

    @PutMapping("/{id}/remove")
    public ResponseEntity<FriendRequest> removeFriend(
            @PathVariable("id") Long requestId,
            @RequestParam("currentUserId") Long currentUserId
    ) {
        FriendRequest updatedRequest = friendRequestService.removeFriend(requestId, currentUserId);
        return ResponseEntity.ok(updatedRequest);
    }

    @GetMapping("/relationship/{userId}/{otherUserId}")
    public ResponseEntity<FriendRelationshipResponse> getRelationship(
            @PathVariable("userId") Long userId,
            @PathVariable("otherUserId") Long otherUserId
    ) {
        return ResponseEntity.ok(friendRequestService.getRelationship(userId, otherUserId));
    }

    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<FriendRequest>> getReceivedFriendRequests(@PathVariable("receiverId") Long receiverId) {
        List<FriendRequest> requests = friendRequestService.getReceivedFriendRequests(receiverId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<FriendRequest>> getSentFriendRequests(@PathVariable("senderId") Long senderId) {
        List<FriendRequest> requests = friendRequestService.getSentFriendRequests(senderId);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/approved/{userId}")
    public ResponseEntity<List<FriendRequest>> getApprovedFriendRequests(@PathVariable("userId") Long userId) {
        List<FriendRequest> friends = friendRequestService.getApprovedFriendRequests(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/sent/pending/{senderId}")
    public ResponseEntity<List<FriendRequest>> getPendingSentFriendRequests(@PathVariable("senderId") Long senderId) {
        List<FriendRequest> pending = friendRequestService.getPendingSentRequests(senderId);
        return ResponseEntity.ok(pending);
    }

    @GetMapping("/received/pending/{receiverId}")
    public ResponseEntity<List<FriendRequest>> getPendingReceivedFriendRequests(@PathVariable("receiverId") Long receiverId) {
        List<FriendRequest> pending = friendRequestService.getPendingReceivedRequests(receiverId);
        return ResponseEntity.ok(pending);
    }
}
