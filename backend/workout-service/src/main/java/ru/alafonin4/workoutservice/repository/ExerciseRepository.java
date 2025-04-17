package ru.alafonin4.workoutservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.workoutservice.model.Exercise;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByMuscleGroup(String muscleGroup);

    List<Exercise> findByMuscleGroupIgnoreCase(String muscleGroup);
}
