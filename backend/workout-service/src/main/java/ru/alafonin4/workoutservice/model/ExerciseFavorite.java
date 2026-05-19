package ru.alafonin4.workoutservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "exercise_favorites")
public class ExerciseFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long exerciseId;

    /**
     * Returns the id.
     * @return result of the operation
     */
    public Long getId() {
        return id;
    }

    /**
     * Updates the id.
     * @param id identifier of the target record
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the identifier of the user.
     * @return result of the operation
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Updates the identifier of the user.
     * @param userId identifier of the user
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the identifier of the exercise.
     * @return result of the operation
     */
    public Long getExerciseId() {
        return exerciseId;
    }

    /**
     * Updates the identifier of the exercise.
     * @param exerciseId identifier of the exercise
     */
    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }
}
