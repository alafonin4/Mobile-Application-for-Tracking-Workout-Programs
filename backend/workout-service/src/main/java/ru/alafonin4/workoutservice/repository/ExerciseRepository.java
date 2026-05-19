package ru.alafonin4.workoutservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.workoutservice.model.Exercise;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    /**
     * Returns exercises that belong to the supplied muscle group.
     *
     * @param muscleGroup muscle group filter
     * @return exercises for the supplied muscle group
     */
    List<Exercise> findByMuscleGroup(String muscleGroup);

    /**
     * Returns exercises that belong to the supplied muscle group ignoring case.
     *
     * @param muscleGroup muscle group filter
     * @return exercises for the supplied muscle group
     */
    List<Exercise> findByMuscleGroupIgnoreCase(String muscleGroup);
}
