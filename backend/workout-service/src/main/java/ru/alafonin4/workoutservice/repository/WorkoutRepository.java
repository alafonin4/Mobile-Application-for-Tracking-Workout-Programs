package ru.alafonin4.workoutservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.workoutservice.model.Workout;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    /**
     * Returns all workouts for the supplied user ordered by workout date.
     *
     * @param userId identifier of the user
     * @return workouts for the supplied user
     */
    List<Workout> findByUserIdOrderByWorkoutDateAsc(Long userId);

    /**
     * Returns workouts for the supplied user that were completed on or after the provided date.
     *
     * @param userId identifier of the user
     * @param workoutDate lower inclusive boundary for the workout date
     * @return matching workouts ordered by workout date
     */
    List<Workout> findByUserIdAndWorkoutDateGreaterThanEqualOrderByWorkoutDateAsc(Long userId, LocalDateTime workoutDate);

    /**
     * Returns workouts for the supplied user inside the provided date range.
     *
     * @param userId identifier of the user
     * @param fromDate lower inclusive boundary for the workout date
     * @param toDate upper exclusive boundary for the workout date
     * @return matching workouts ordered by workout date
     */
    List<Workout> findByUserIdAndWorkoutDateGreaterThanEqualAndWorkoutDateLessThanOrderByWorkoutDateAsc(
            Long userId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    );
}
