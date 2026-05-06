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
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setBio(updatedUser.getBio());
            user.setBodyWeight(updatedUser.getBodyWeight());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id " + id);
        }

        userRepository.deleteById(id);
    }

    public User createUser(UserRequest user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User id is required.");
        }

        Optional<User> existingById = userRepository.findById(user.getId());
        if (existingById.isPresent()) {
            User existingUser = existingById.get();
            if (!existingUser.getEmail().equals(user.getEmail())) {
                throw new IllegalStateException("User id already belongs to another email.");
            }
            return existingUser;
        }

        Optional<User> existingByEmail = userRepository.findByEmail(user.getEmail());
        if (existingByEmail.isPresent()) {
            throw new IllegalStateException("Email already belongs to another user.");
        }

        User user2 = new User();
        user2.setId(user.getId());
        user2.setEmail(user.getEmail());
        user2.setFirstName(user.getFirstName());
        user2.setLastName(user.getLastName());
        user2.setBio("");
        user2.setBodyWeight(0.0);
        user2.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user2);
    }
}
