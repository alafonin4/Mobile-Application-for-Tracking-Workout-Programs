package ru.alafonin4.workoutservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "training_day_exercises")
public class TrainingDayExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_day_id")
    private TrainingDay trainingDay;

    private Long exerciseId;

    private String exerciseName;

    private Integer recommendedSets;
    private Integer recommendedReps;
    private Double recommendedWeight;

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
     * Returns the training day.
     * @return result of the operation
     */
    public TrainingDay getTrainingDay() {
        return trainingDay;
    }

    /**
     * Updates the training day.
     * @param trainingDay new training day
     */
    public void setTrainingDay(TrainingDay trainingDay) {
        this.trainingDay = trainingDay;
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

    /**
     * Returns the exercise name.
     * @return resulting text value
     */
    public String getExerciseName() {
        return exerciseName;
    }

    /**
     * Updates the exercise name.
     * @param exerciseName new exercise name
     */
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    /**
     * Returns the recommended sets.
     * @return calculated numeric value
     */
    public Integer getRecommendedSets() {
        return recommendedSets;
    }

    /**
     * Updates the recommended sets.
     * @param recommendedSets new recommended sets
     */
    public void setRecommendedSets(Integer recommendedSets) {
        this.recommendedSets = recommendedSets;
    }

    /**
     * Returns the recommended reps.
     * @return calculated numeric value
     */
    public Integer getRecommendedReps() {
        return recommendedReps;
    }

    /**
     * Updates the recommended reps.
     * @param recommendedReps new recommended reps
     */
    public void setRecommendedReps(Integer recommendedReps) {
        this.recommendedReps = recommendedReps;
    }

    /**
     * Returns the recommended weight.
     * @return calculated numeric value
     */
    public Double getRecommendedWeight() {
        return recommendedWeight;
    }

    /**
     * Updates the recommended weight.
     * @param recommendedWeight new recommended weight
     */
    public void setRecommendedWeight(Double recommendedWeight) {
        this.recommendedWeight = recommendedWeight;
    }
}
