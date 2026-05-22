package ru.alafonin4.socialservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    void handleResponseStatusExceptionUsesReasonAsMessage() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/friendRequests/1");

        ApiErrorResponse body = handler.handleResponseStatusException(
                new ResponseStatusException(HttpStatus.CONFLICT, "Conflict message"),
                request
        ).getBody();

        assertEquals(409, body.getStatus());
        assertEquals("Conflict message", body.getMessage());
        assertEquals("/api/friendRequests/1", body.getPath());
    }

    @Test
    void handleUnexpectedExceptionReturnsInternalServerErrorPayload() {
        HttpServletRequest request = new MockHttpServletRequest("GET", "/api/notifications/user/1");

        ApiErrorResponse body = handler.handleUnexpectedException(request).getBody();

        assertEquals(500, body.getStatus());
        assertEquals("Internal server error.", body.getMessage());
    }
}
