package ru.alafonin4.workoutservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout_exercises")
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private Workout workout;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    @Column(length = 500)
    private String notes;

    /**
     * ArrayList<>.
     * @return result of the operation
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "workoutExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExerciseSet> sets = new ArrayList<>();

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
     * Returns the workout.
     * @return result of the operation
     */
    public Workout getWorkout() {
        return workout;
    }

    /**
     * Updates the workout.
     * @param workout workout being processed
     */
    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    /**
     * Returns the exercise.
     * @return result of the operation
     */
    public Exercise getExercise() {
        return exercise;
    }

    /**
     * Updates the exercise.
     * @param exercise exercise being processed
     */
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    /**
     * Returns the notes.
     * @return resulting text value
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Updates the notes.
     * @param notes new notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Returns the sets.
     * @return prepared list with the requested data
     */
    public List<ExerciseSet> getSets() {
        return sets;
    }

    /**
     * Updates the sets.
     * @param sets new sets
     */
    public void setSets(List<ExerciseSet> sets) {
        this.sets = sets;
    }
}
