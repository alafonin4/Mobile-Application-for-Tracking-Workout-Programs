package ru.alafonin4.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.alafonin4.userservice.dto.UserRequest;
import ru.alafonin4.userservice.model.User;
import ru.alafonin4.userservice.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User getRequiredUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setBio(updatedUser.getBio());
            user.setBodyWeight(updatedUser.getBodyWeight());
            user.setFitnessGoal(updatedUser.getFitnessGoal());
            user.setAvatarUrl(updatedUser.getAvatarUrl());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + id);
        }

        userRepository.deleteById(id);
    }

    public User createUser(UserRequest user) {
        if (user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User id is required.");
        }

        Optional<User> existingById = userRepository.findById(user.getId());
        if (existingById.isPresent()) {
            User existingUser = existingById.get();
            if (!existingUser.getEmail().equals(user.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User id already belongs to another email.");
            }
            return existingUser;
        }

        Optional<User> existingByEmail = userRepository.findByEmail(user.getEmail());
        if (existingByEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already belongs to another user.");
        }

        User user2 = new User();
        user2.setId(user.getId());
        user2.setEmail(user.getEmail());
        user2.setFirstName(user.getFirstName());
        user2.setLastName(user.getLastName());
        user2.setBio("");
        user2.setBodyWeight(0.0);
        user2.setFitnessGoal(user.getFitnessGoal() == null || user.getFitnessGoal().isBlank()
                ? "GENERAL_FITNESS"
                : user.getFitnessGoal());
        user2.setAvatarUrl(user.getAvatarUrl());
        user2.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user2);
    }
}
