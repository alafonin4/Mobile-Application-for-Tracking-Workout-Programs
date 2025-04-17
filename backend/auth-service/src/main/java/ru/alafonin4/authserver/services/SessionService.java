package ru.alafonin4.authserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.alafonin4.authserver.entities.Session;
import ru.alafonin4.authserver.pojo.AuthRequest;
import ru.alafonin4.authserver.pojo.AuthResponse;
import ru.alafonin4.authserver.repositories.SessionRepository;
import ru.alafonin4.authserver.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public void save(AuthRequest request, AuthResponse response) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Not found user with email: " + request.getEmail()));

        Session session = Session.builder()
                .sessionToken(response.getToken())
                .user(user)
                .build();

        sessionRepository.save(session);
    }
}
