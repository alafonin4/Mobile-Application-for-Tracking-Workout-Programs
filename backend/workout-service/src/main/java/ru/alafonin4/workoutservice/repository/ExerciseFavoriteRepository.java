package ru.alafonin4.workoutservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.workoutservice.model.ExerciseFavorite;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseFavoriteRepository extends JpaRepository<ExerciseFavorite, Long> {
    /**
     * Returns all favorite exercises saved by the supplied user.
     *
     * @param userId identifier of the user
     * @return favorite exercise records for the supplied user
     */
    List<ExerciseFavorite> findByUserId(Long userId);

    /**
     * Looks up a favorite exercise relation by user and exercise identifiers.
     *
     * @param userId identifier of the user
     * @param exerciseId identifier of the exercise
     * @return optional favorite relation
     */
    Optional<ExerciseFavorite> findByUserIdAndExerciseId(Long userId, Long exerciseId);

    /**
     * Deletes a favorite exercise relation for the supplied user and exercise.
     *
     * @param userId identifier of the user
     * @param exerciseId identifier of the exercise
     */
    void deleteByUserIdAndExerciseId(Long userId, Long exerciseId);
}
