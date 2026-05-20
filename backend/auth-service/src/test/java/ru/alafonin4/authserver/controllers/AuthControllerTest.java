package ru.alafonin4.authserver.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alafonin4.authserver.pojo.AuthResponse;
import ru.alafonin4.authserver.pojo.ChangePasswordRequest;
import ru.alafonin4.authserver.pojo.LoginRequest;
import ru.alafonin4.authserver.pojo.RegisterRequest;
import ru.alafonin4.authserver.services.AuthService;
import ru.alafonin4.authserver.services.SessionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerReturnsAuthResponseAndStoresSession() {
        RegisterRequest request = new RegisterRequest();
        AuthResponse response = new AuthResponse();
        response.setId(1L);
        response.setToken("jwt");

        when(authService.register(request)).thenReturn(response);

        var entity = authController.register(request);

        assertEquals(200, entity.getStatusCode().value());
        assertEquals(response, entity.getBody());
        verify(sessionService).save(request, response);
    }

    @Test
    void loginReturnsAuthResponseAndStoresSession() {
        LoginRequest request = new LoginRequest();
        AuthResponse response = new AuthResponse();
        response.setId(5L);

        when(authService.login(request)).thenReturn(response);

        var entity = authController.login(request);

        assertEquals(200, entity.getStatusCode().value());
        assertEquals(response, entity.getBody());
        verify(sessionService).save(request, response);
    }

    @Test
    void changePasswordReturnsSuccessMessage() {
        ChangePasswordRequest request = new ChangePasswordRequest();

        var entity = authController.changePassword(7L, request);

        assertEquals(200, entity.getStatusCode().value());
        assertEquals("Password updated successfully.", entity.getBody().getMessage());
        verify(authService).changePassword(7L, request);
    }

    @Test
    void deleteAccountReturnsNoContent() {
        var entity = authController.deleteAccount(9L);

        assertEquals(204, entity.getStatusCode().value());
        verify(authService).deleteAccount(9L);
    }

    @Test
    void testEndpointReturnsMarker() {
        assertEquals("wegniwg", authController.test());
    }
}
