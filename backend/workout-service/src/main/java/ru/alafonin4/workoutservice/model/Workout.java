package ru.alafonin4.workoutservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workouts")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(length = 100)
    private String name;

    private LocalDateTime workoutDate;

    /**
     * ArrayList<>.
     * @return result of the operation
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();

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
     * Returns the name.
     * @return resulting text value
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name.
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the workout date.
     * @return result of the operation
     */
    public LocalDateTime getWorkoutDate() {
        return workoutDate;
    }

    /**
     * Updates the workout date.
     * @param workoutDate new workout date
     */
    public void setWorkoutDate(LocalDateTime workoutDate) {
        this.workoutDate = workoutDate;
    }

    /**
     * Returns the workout exercises.
     * @return prepared list with the requested data
     */
    public List<WorkoutExercise> getWorkoutExercises() {
        return workoutExercises;
    }

    /**
     * Updates the workout exercises.
     * @param workoutExercises new workout exercises
     */
    public void setWorkoutExercises(List<WorkoutExercise> workoutExercises) {
        this.workoutExercises = workoutExercises;
    }
}
