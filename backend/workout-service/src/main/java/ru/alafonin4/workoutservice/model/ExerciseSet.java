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
@Table(name = "exercise_sets")
public class ExerciseSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer setNumber;

    private Integer reps;

    private Double weight;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_exercise_id")
    private WorkoutExercise workoutExercise;

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
     * Returns the set number.
     * @return calculated numeric value
     */
    public Integer getSetNumber() {
        return setNumber;
    }

    /**
     * Updates the set number.
     * @param setNumber new set number
     */
    public void setSetNumber(Integer setNumber) {
        this.setNumber = setNumber;
    }

    /**
     * Returns the reps.
     * @return calculated numeric value
     */
    public Integer getReps() {
        return reps;
    }

    /**
     * Updates the reps.
     * @param reps new reps
     */
    public void setReps(Integer reps) {
        this.reps = reps;
    }

    /**
     * Returns the weight.
     * @return calculated numeric value
     */
    public Double getWeight() {
        return weight;
    }

    /**
     * Updates the weight.
     * @param weight new weight
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /**
     * Returns the workout exercise.
     * @return result of the operation
     */
    public WorkoutExercise getWorkoutExercise() {
        return workoutExercise;
    }

    /**
     * Updates the workout exercise.
     * @param workoutExercise new workout exercise
     */
    public void setWorkoutExercise(WorkoutExercise workoutExercise) {
        this.workoutExercise = workoutExercise;
    }
}
