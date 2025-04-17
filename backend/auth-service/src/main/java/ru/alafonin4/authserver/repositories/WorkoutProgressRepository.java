package ru.alafonin4.authserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.authserver.entities.WorkoutProgress;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkoutProgressRepository extends JpaRepository<WorkoutProgress, Long> {
    List<WorkoutProgress> findByEmailAndDateTimeBetween(String userEmail, LocalDateTime startDateTime, LocalDateTime endDateTime);
}