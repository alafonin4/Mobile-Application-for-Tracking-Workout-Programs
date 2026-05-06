package ru.alafonin4.socialservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        FriendRequest request = new FriendRequest();
        request.setReceiverId(req.getReceiverId());
        request.setSenderId(req.getSenderId());
        request.setStatus(FriendRequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        return friendRequestRepository.save(request);
    }

    public FriendRequest acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found with id " + requestId));
        request.setStatus(FriendRequestStatus.ACCEPTED);
        return friendRequestRepository.save(request);
    }

    public FriendRequest rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found with id " + requestId));
        request.setStatus(FriendRequestStatus.REJECTED);
        return friendRequestRepository.save(request);
    }

    public List<FriendRequest> getReceivedFriendRequests(Long receiverId) {
        return friendRequestRepository.findByReceiverIdAndStatus(receiverId, FriendRequestStatus.PENDING);
    }

    // Получение списка исходящих запросов (ожидающих)
    public List<FriendRequest> getSentFriendRequests(Long senderId) {
        return friendRequestRepository.findBySenderIdAndStatus(senderId, FriendRequestStatus.PENDING);
    }

    // ✅ Получение списка одобренных друзей (входящие и исходящие с ACCEPTED)
    public List<FriendRequest> getApprovedFriendRequests(Long userId) {
        return friendRequestRepository.findBySenderIdOrReceiverIdAndStatus(userId, userId, FriendRequestStatus.ACCEPTED);
    }

    // ✅ Получение списка исходящих, но ещё не одобренных
    public List<FriendRequest> getPendingSentRequests(Long senderId) {
        return friendRequestRepository.findBySenderIdAndStatus(senderId, FriendRequestStatus.PENDING);
    }

    // ✅ Получение списка входящих, но ещё не одобренных
    public List<FriendRequest> getPendingReceivedRequests(Long receiverId) {
        return friendRequestRepository.findByReceiverIdAndStatus(receiverId, FriendRequestStatus.PENDING);
    }
}
