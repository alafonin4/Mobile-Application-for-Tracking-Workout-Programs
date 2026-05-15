package ru.alafonin4.authserver.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alafonin4.authserver.pojo.AuthResponse;
import ru.alafonin4.authserver.pojo.ChangePasswordRequest;
import ru.alafonin4.authserver.pojo.LoginRequest;
import ru.alafonin4.authserver.pojo.RegisterRequest;
import ru.alafonin4.authserver.pojo.SimpleMessageResponse;
import ru.alafonin4.authserver.services.AuthService;
import ru.alafonin4.authserver.services.SessionService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private SessionService sessionService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        var response = authService.register(request);
        sessionService.save(request, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response  = authService.login(request);
        sessionService.save(request, response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public String test() {
        return "wegniwg";
    }

    @PutMapping("/password/{id}")
    public ResponseEntity<SimpleMessageResponse> changePassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        authService.changePassword(id, request);
        return ResponseEntity.ok(new SimpleMessageResponse("Password updated successfully."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id) {
        authService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
