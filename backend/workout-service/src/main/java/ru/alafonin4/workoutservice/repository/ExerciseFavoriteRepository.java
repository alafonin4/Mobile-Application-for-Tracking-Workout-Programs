package ru.alafonin4.workoutservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.workoutservice.model.ExerciseFavorite;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseFavoriteRepository extends JpaRepository<ExerciseFavorite, Long> {
    List<ExerciseFavorite> findByUserId(Long userId);

    Optional<ExerciseFavorite> findByUserIdAndExerciseId(Long userId, Long exerciseId);

    void deleteByUserIdAndExerciseId(Long userId, Long exerciseId);
}
