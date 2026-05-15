package ru.alafonin4.socialservice.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RemoteWorkoutDto {
    private Long id;
    private Long userId;
    private String name;
    private LocalDateTime workoutDate;
    private List<RemoteWorkoutExerciseDto> workoutExercises = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getWorkoutDate() {
        return workoutDate;
    }

    public void setWorkoutDate(LocalDateTime workoutDate) {
        this.workoutDate = workoutDate;
    }

    public List<RemoteWorkoutExerciseDto> getWorkoutExercises() {
        return workoutExercises;
    }

    public void setWorkoutExercises(List<RemoteWorkoutExerciseDto> workoutExercises) {
        this.workoutExercises = workoutExercises;
    }
}
