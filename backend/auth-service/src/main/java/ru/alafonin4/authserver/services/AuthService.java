package ru.alafonin4.authserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private SessionRepository sessionRepository;

    /**
     * Registers a new user and returns authentication data.
     * @param request request payload
     * @return result of the operation
     */
    public AuthResponse register(RegisterRequest request) {

        if (!repository.findByEmail(request.getEmail()).isEmpty()) {
            System.out.println(request.getEmail());
            throw new UserAlreadyExistException("User already exists.");
        }

        User user1 = new User();
        user1.setFirstName(request.getFirstName());
        user1.setLastName(request.getLastName());
        user1.setEmail(request.getEmail());
        user1.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user1.setRole(UserRole.CUSTOMER);
        user1.setCreatedAt(LocalDateTime.now());

        User u = repository.save(user1);

        var jwtToken = jwtService.generateToken(user1);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtToken);
        authResponse.setId(u.getId());
        return authResponse;
    }

    /**
     * Authenticates a user and returns authentication data.
     * @param request request payload
     * @return result of the operation
     */
    public AuthResponse login(LoginRequest request) {
        var user = repository.findByEmail(request.getEmail());
        if (user.isEmpty()){
            throw new NotFoundEmailException("User with this email was not found.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            throw new IncorrectPasswordException("");
        }
        manager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user.get());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtToken);
        authResponse.setId(user.get().getId());
        return authResponse;
    }

    /**
     * Changes the password for the specified user.
     * @param userId identifier of the user
     * @param request request payload
     */
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundEmailException("User not found."));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Current password is incorrect.");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        repository.save(user);
    }

    /**
     * Deletes the account.
     * @param userId identifier of the user
     */
    public void deleteAccount(Long userId) {
        if (!repository.existsById(userId)) {
            throw new NotFoundEmailException("User not found.");
        }

        sessionRepository.deleteByUserId(userId);
        repository.deleteById(userId);
    }
}
