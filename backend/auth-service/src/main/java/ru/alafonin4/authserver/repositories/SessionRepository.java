package ru.alafonin4.authserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.authserver.entities.Session;
@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    void deleteByUserId(Long userId);
}
