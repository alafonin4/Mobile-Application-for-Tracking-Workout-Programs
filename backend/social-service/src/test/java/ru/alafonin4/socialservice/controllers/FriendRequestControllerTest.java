package ru.alafonin4.socialservice.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.alafonin4.socialservice.dto.FriendRelationshipResponse;
import ru.alafonin4.socialservice.dto.FriendRequestDTO;
import ru.alafonin4.socialservice.entities.FriendRequest;
import ru.alafonin4.socialservice.services.FriendRequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendRequestControllerTest {

    @Mock
    private FriendRequestService friendRequestService;

    @InjectMocks
    private FriendRequestController friendRequestController;

    @Test
    void sendFriendRequestReturnsOkPayload() {
        FriendRequest request = new FriendRequest();
        FriendRequestDTO dto = new FriendRequestDTO();
        when(friendRequestService.sendFriendRequest(dto)).thenReturn(request);

        ResponseEntity<FriendRequest> response = friendRequestController.sendFriendRequest(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request, response.getBody());
    }

    @Test
    void removeFriendReturnsUpdatedRequest() {
        FriendRequest request = new FriendRequest();
        when(friendRequestService.removeFriend(5L, 1L)).thenReturn(request);

        ResponseEntity<FriendRequest> response = friendRequestController.removeFriend(5L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request, response.getBody());
    }

    @Test
    void getRelationshipReturnsResponse() {
        FriendRelationshipResponse relationship = new FriendRelationshipResponse();
        relationship.setRelationType("FRIENDS");
        when(friendRequestService.getRelationship(1L, 2L)).thenReturn(relationship);

        ResponseEntity<FriendRelationshipResponse> response = friendRequestController.getRelationship(1L, 2L);

        assertEquals("FRIENDS", response.getBody().getRelationType());
    }

    @Test
    void getReceivedRequestsReturnsList() {
        when(friendRequestService.getReceivedFriendRequests(2L)).thenReturn(List.of(new FriendRequest()));

        ResponseEntity<List<FriendRequest>> response = friendRequestController.getReceivedFriendRequests(2L);

        assertEquals(1, response.getBody().size());
    }
}
