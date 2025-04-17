package ru.alafonin4.authserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alafonin4.authserver.exceptions.InvalidTokenException;
import ru.alafonin4.authserver.pojo.UserInfoResponse;
import ru.alafonin4.authserver.services.JwtService;
import ru.alafonin4.authserver.services.UserService;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {
    private final JwtService jwtService;
    private final UserService userService;

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        boolean isValid = jwtService.isValidToken(authToken);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserInfoResponse> userInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authToken) {
        if (!jwtService.isValidToken(authToken)) {
            throw new InvalidTokenException("Invalid token.");
        }
        String email = jwtService.extractEmail(authToken.substring(7));
        return ResponseEntity.ok(userService.getUserInfoByEmail(email));
    }
}
