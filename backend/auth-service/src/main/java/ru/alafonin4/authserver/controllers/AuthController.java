package ru.alafonin4.authserver.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alafonin4.authserver.exceptions.IncorrectPasswordException;
import ru.alafonin4.authserver.exceptions.NotFoundEmailException;
import ru.alafonin4.authserver.exceptions.UserAlreadyExistException;
import ru.alafonin4.authserver.pojo.AuthResponse;
import ru.alafonin4.authserver.pojo.LoginRequest;
import ru.alafonin4.authserver.pojo.RegisterRequest;
import ru.alafonin4.authserver.services.AuthService;
import ru.alafonin4.authserver.services.SessionService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            var response = authService.register(request);
            sessionService.save(request, response);
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response;
        try {
            response  = authService.login(request);
            sessionService.save(request, response);
            return ResponseEntity.ok(response);
        } catch (NotFoundEmailException | IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/test")
    public String test() {
        return "wegniwg";
    }
}
