package ru.alafonin4.authserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.alafonin4.authserver.pojo.UserInfoResponse;
import ru.alafonin4.authserver.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserInfoResponse getUserInfoByEmail(String email) {
        var user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return UserInfoResponse.builder()
                .email(user.get().getEmail())
                .role(user.get().getRole())
                .build();
    }
}
