package ru.alafonin4.authserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Saves the supplied data.
     * @param request request payload
     * @param response response payload
     */
    public void save(AuthRequest request, AuthResponse response) {
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Not found user with email: " + request.getEmail()));

        Session session1 = new Session();
        session1.setSessionToken(response.getToken());
        session1.setUser(user);

        sessionRepository.save(session1);
    }
}
