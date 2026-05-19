package ru.alafonin4.authserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.alafonin4.authserver.pojo.UserInfoResponse;
import ru.alafonin4.authserver.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Returns the user info by email.
     * @param email email
     * @return result of the operation
     */
    public UserInfoResponse getUserInfoByEmail(String email) {
        var user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setEmail(user.get().getEmail());
        userInfoResponse.setRole(user.get().getRole());
        return userInfoResponse;
    }
}
