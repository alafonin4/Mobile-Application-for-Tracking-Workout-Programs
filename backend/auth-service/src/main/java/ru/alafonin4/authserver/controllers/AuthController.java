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

    /**
     * Registers a new user and returns authentication data.
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        var response = authService.register(request);
        sessionService.save(request, response);
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates a user and returns authentication data.
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response  = authService.login(request);
        sessionService.save(request, response);
        return ResponseEntity.ok(response);
    }

    /**
     * Returns a simple service availability marker.
     * @return resulting text value
     */
    @GetMapping("/test")
    public String test() {
        return "wegniwg";
    }

    /**
     * Changes the password for the specified user.
     * @param id identifier of the target record
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
    @PutMapping("/password/{id}")
    public ResponseEntity<SimpleMessageResponse> changePassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        authService.changePassword(id, request);
        return ResponseEntity.ok(new SimpleMessageResponse("Password updated successfully."));
    }

    /**
     * Deletes the account.
     * @param id identifier of the target record
     * @return HTTP response containing the requested payload
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id) {
        authService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
