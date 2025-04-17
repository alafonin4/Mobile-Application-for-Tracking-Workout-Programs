package ru.alafonin4.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alafonin4.userservice.dto.UserRequest;
import ru.alafonin4.userservice.model.User;
import ru.alafonin4.userservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(updatedUser.getEmail());
            user.setBio(updatedUser.getBio());
            user.setBodyWeight(updatedUser.getBodyWeight());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public User createUser(UserRequest user) {
        User user1 = User.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .bio(user.getBio())
                .bodyWeight(user.getBodyWeight())
                .createdAt(LocalDateTime.now())
                .build();
        return userRepository.save(user1);
    }
}
