package ru.alafonin4.workoutservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.workoutservice.model.Workout;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByUserIdOrderByWorkoutDateAsc(Long userId);

    List<Workout> findByUserIdAndWorkoutDateGreaterThanEqualOrderByWorkoutDateAsc(Long userId, LocalDateTime workoutDate);

    List<Workout> findByUserIdAndWorkoutDateGreaterThanEqualAndWorkoutDateLessThanOrderByWorkoutDateAsc(
            Long userId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    );
}
