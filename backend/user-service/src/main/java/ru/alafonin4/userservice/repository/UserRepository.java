package ru.alafonin4.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.userservice.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Looks up a user by email address.
     *
     * @param email email address that should be matched
     * @return optional user with the supplied email
     */
    Optional<User> findByEmail(String email);
}
