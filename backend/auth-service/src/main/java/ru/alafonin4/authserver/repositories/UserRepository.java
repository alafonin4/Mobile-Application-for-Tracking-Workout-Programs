package ru.alafonin4.authserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.authserver.entities.User;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Looks up a user by email address.
     *
     * @param email email address used for authentication
     * @return optional user that matches the supplied email
     */
    Optional<User> findByEmail(String email);
}
