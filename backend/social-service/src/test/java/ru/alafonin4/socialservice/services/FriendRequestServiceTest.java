package ru.alafonin4.socialservice.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;
import ru.alafonin4.socialservice.dto.FriendRelationshipResponse;
import ru.alafonin4.socialservice.dto.FriendRequestDTO;
import ru.alafonin4.socialservice.entities.FriendRequest;
import ru.alafonin4.socialservice.enums.FriendRequestStatus;
import ru.alafonin4.socialservice.repositories.FriendRequestRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendRequestServiceTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @InjectMocks
    private FriendRequestService friendRequestService;

    @Test
    void sendFriendRequestCreatesPendingRequest() {
        FriendRequestDTO request = new FriendRequestDTO();
        request.setSenderId(1L);
        request.setReceiverId(2L);

        when(friendRequestRepository.findRelationsBetweenUsers(eq(1L), eq(2L), any())).thenReturn(List.of());
        when(friendRequestRepository.save(any(FriendRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FriendRequest saved = friendRequestService.sendFriendRequest(request);

        assertEquals(1L, saved.getSenderId());
        assertEquals(2L, saved.getReceiverId());
        assertEquals(FriendRequestStatus.PENDING, saved.getStatus());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void sendFriendRequestRejectsSelfRequest() {
        FriendRequestDTO request = new FriendRequestDTO();
        request.setSenderId(1L);
        request.setReceiverId(1L);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> friendRequestService.sendFriendRequest(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(friendRequestRepository, never()).save(any(FriendRequest.class));
    }

    @Test
    void sendFriendRequestRejectsExistingAcceptedRelation() {
        FriendRequest existing = new FriendRequest();
        existing.setStatus(FriendRequestStatus.ACCEPTED);

        when(friendRequestRepository.findRelationsBetweenUsers(eq(1L), eq(2L), any()))
                .thenReturn(List.of(existing));

        FriendRequestDTO request = new FriendRequestDTO();
        request.setSenderId(1L);
        request.setReceiverId(2L);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> friendRequestService.sendFriendRequest(request)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void acceptFriendRequestMarksAcceptedAndResponded() {
        FriendRequest request = new FriendRequest();
        request.setStatus(FriendRequestStatus.PENDING);

        when(friendRequestRepository.findById(5L)).thenReturn(Optional.of(request));
        when(friendRequestRepository.save(request)).thenReturn(request);

        FriendRequest result = friendRequestService.acceptFriendRequest(5L);

        assertEquals(FriendRequestStatus.ACCEPTED, result.getStatus());
        assertNotNull(result.getRespondedAt());
    }

    @Test
    void cancelFriendRequestRejectsWrongSender() {
        FriendRequest request = new FriendRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setStatus(FriendRequestStatus.PENDING);

        when(friendRequestRepository.findById(7L)).thenReturn(Optional.of(request));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> friendRequestService.cancelFriendRequest(7L, 2L)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void cancelFriendRequestMarksRequestCanceled() {
        FriendRequest request = new FriendRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setStatus(FriendRequestStatus.PENDING);

        when(friendRequestRepository.findById(7L)).thenReturn(Optional.of(request));
        when(friendRequestRepository.save(request)).thenReturn(request);

        FriendRequest result = friendRequestService.cancelFriendRequest(7L, 1L);

        assertEquals(FriendRequestStatus.CANCELED, result.getStatus());
        assertNotNull(result.getRespondedAt());
    }

    @Test
    void removeFriendTransformsFriendshipIntoPendingInverseRequest() {
        FriendRequest request = new FriendRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setStatus(FriendRequestStatus.ACCEPTED);

        when(friendRequestRepository.findById(8L)).thenReturn(Optional.of(request));
        when(friendRequestRepository.save(request)).thenReturn(request);

        FriendRequest result = friendRequestService.removeFriend(8L, 1L);

        assertEquals(2L, result.getSenderId());
        assertEquals(1L, result.getReceiverId());
        assertEquals(FriendRequestStatus.PENDING, result.getStatus());
        assertNull(result.getRespondedAt());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void getRelationshipReturnsSelfRelation() {
        FriendRelationshipResponse response = friendRequestService.getRelationship(3L, 3L);

        assertEquals("SELF", response.getRelationType());
    }

    @Test
    void getRelationshipReturnsFriendsRelation() {
        FriendRequest request = new FriendRequest();
        ReflectionTestUtils.setField(request, "id", 11L);
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setStatus(FriendRequestStatus.ACCEPTED);

        when(friendRequestRepository.findRelationsBetweenUsers(eq(1L), eq(2L), any()))
                .thenReturn(List.of(request));

        FriendRelationshipResponse response = friendRequestService.getRelationship(1L, 2L);

        assertEquals("FRIENDS", response.getRelationType());
        assertEquals(11L, response.getRequestId());
        assertEquals(FriendRequestStatus.ACCEPTED, response.getStatus());
    }

    @Test
    void getRelationshipReturnsIncomingPendingRelation() {
        FriendRequest request = new FriendRequest();
        request.setSenderId(2L);
        request.setReceiverId(1L);
        request.setStatus(FriendRequestStatus.PENDING);

        when(friendRequestRepository.findRelationsBetweenUsers(eq(1L), eq(2L), any()))
                .thenReturn(List.of(request));

        FriendRelationshipResponse response = friendRequestService.getRelationship(1L, 2L);

        assertEquals("INCOMING_PENDING", response.getRelationType());
    }

    @Test
    void getPendingQueriesDelegateToRepository() {
        FriendRequest pending = new FriendRequest();
        pending.setStatus(FriendRequestStatus.PENDING);
        when(friendRequestRepository.findBySenderIdAndStatus(1L, FriendRequestStatus.PENDING)).thenReturn(List.of(pending));
        when(friendRequestRepository.findByReceiverIdAndStatus(2L, FriendRequestStatus.PENDING)).thenReturn(List.of(pending));

        assertEquals(1, friendRequestService.getPendingSentRequests(1L).size());
        assertEquals(1, friendRequestService.getPendingReceivedRequests(2L).size());
    }
}
