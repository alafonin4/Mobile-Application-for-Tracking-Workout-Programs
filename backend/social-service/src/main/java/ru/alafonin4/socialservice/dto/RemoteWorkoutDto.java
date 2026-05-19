package ru.alafonin4.socialservice.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RemoteWorkoutDto {
    private Long id;
    private Long userId;
    private String name;
    private LocalDateTime workoutDate;
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<RemoteWorkoutExerciseDto> workoutExercises = new ArrayList<>();

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
    public List<RemoteWorkoutExerciseDto> getWorkoutExercises() {
        return workoutExercises;
    }

    /**
     * Updates the workout exercises.
     * @param workoutExercises new workout exercises
     */
    public void setWorkoutExercises(List<RemoteWorkoutExerciseDto> workoutExercises) {
        this.workoutExercises = workoutExercises;
    }
}
