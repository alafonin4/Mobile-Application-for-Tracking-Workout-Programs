package ru.alafonin4.authserver.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import ru.alafonin4.authserver.entities.User;
import ru.alafonin4.authserver.enums.UserRole;
import ru.alafonin4.authserver.exceptions.IncorrectPasswordException;
import ru.alafonin4.authserver.exceptions.NotFoundEmailException;
import ru.alafonin4.authserver.exceptions.UserAlreadyExistException;
import ru.alafonin4.authserver.pojo.AuthResponse;
import ru.alafonin4.authserver.pojo.ChangePasswordRequest;
import ru.alafonin4.authserver.pojo.LoginRequest;
import ru.alafonin4.authserver.pojo.RegisterRequest;
import ru.alafonin4.authserver.repositories.SessionRepository;
import ru.alafonin4.authserver.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager manager;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerCreatesUserAndReturnsToken() {
        RegisterRequest request = new RegisterRequest();
        request.setFirstName("Ivan");
        request.setLastName("Petrov");
        request.setEmail("ivan@example.com");
        request.setPassword("secret");

        when(repository.findByEmail("ivan@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");
        when(repository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            ReflectionTestUtils.setField(user, "id", 15L);
            return user;
        });

        AuthResponse response = authService.register(request);

        assertEquals(15L, response.getId());
        assertEquals("jwt-token", response.getToken());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(repository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("Ivan", savedUser.getFirstName());
        assertEquals("Petrov", savedUser.getLastName());
        assertEquals("ivan@example.com", savedUser.getEmail());
        assertEquals("encoded-secret", savedUser.getPasswordHash());
        assertEquals(UserRole.CUSTOMER, savedUser.getRole());
        assertNotNull(savedUser.getCreatedAt());
    }

    @Test
    void registerThrowsWhenUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("ivan@example.com");

        when(repository.findByEmail("ivan@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistException.class, () -> authService.register(request));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void loginAuthenticatesAndReturnsToken() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ivan@example.com");
        request.setPassword("secret");

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 7L);
        user.setEmail("ivan@example.com");
        user.setPasswordHash("encoded-secret");

        when(repository.findByEmail("ivan@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "encoded-secret")).thenReturn(true);
        when(manager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        assertEquals(7L, response.getId());
        assertEquals("jwt-token", response.getToken());
        verify(manager).authenticate(any());
    }

    @Test
    void loginThrowsWhenUserDoesNotExist() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ghost@example.com");
        request.setPassword("secret");

        when(repository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundEmailException.class, () -> authService.login(request));
        verify(manager, never()).authenticate(any());
    }

    @Test
    void loginThrowsWhenPasswordIsIncorrect() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ivan@example.com");
        request.setPassword("wrong");

        User user = new User();
        user.setEmail("ivan@example.com");
        user.setPasswordHash("encoded-secret");

        when(repository.findByEmail("ivan@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded-secret")).thenReturn(false);

        assertThrows(IncorrectPasswordException.class, () -> authService.login(request));
        verify(manager, never()).authenticate(any());
    }

    @Test
    void changePasswordUpdatesHash() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("old");
        request.setNewPassword("new");

        User user = new User();
        ReflectionTestUtils.setField(user, "id", 9L);
        user.setPasswordHash("old-hash");

        when(repository.findById(9L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "old-hash")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("new-hash");

        authService.changePassword(9L, request);

        assertEquals("new-hash", user.getPasswordHash());
        verify(repository).save(user);
    }

    @Test
    void changePasswordThrowsWhenUserDoesNotExist() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(NotFoundEmailException.class, () -> authService.changePassword(5L, new ChangePasswordRequest()));
    }

    @Test
    void changePasswordThrowsWhenCurrentPasswordIsIncorrect() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setCurrentPassword("wrong");

        User user = new User();
        user.setPasswordHash("old-hash");

        when(repository.findById(5L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "old-hash")).thenReturn(false);

        assertThrows(IncorrectPasswordException.class, () -> authService.changePassword(5L, request));
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void deleteAccountRemovesSessionsAndUser() {
        when(repository.existsById(13L)).thenReturn(true);

        authService.deleteAccount(13L);

        verify(sessionRepository).deleteByUserId(13L);
        verify(repository).deleteById(13L);
    }

    @Test
    void deleteAccountThrowsWhenUserDoesNotExist() {
        when(repository.existsById(13L)).thenReturn(false);

        assertThrows(NotFoundEmailException.class, () -> authService.deleteAccount(13L));
        verify(sessionRepository, never()).deleteByUserId(13L);
    }
}
