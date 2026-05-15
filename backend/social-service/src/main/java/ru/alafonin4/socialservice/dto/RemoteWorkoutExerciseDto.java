package ru.alafonin4.socialservice.dto;

import java.util.ArrayList;
import java.util.List;

public class RemoteWorkoutExerciseDto {
    private RemoteExerciseDto exercise;
    private List<RemoteExerciseSetDto> sets = new ArrayList<>();

    public RemoteExerciseDto getExercise() {
        return exercise;
    }

    public void setExercise(RemoteExerciseDto exercise) {
        this.exercise = exercise;
    }

    public List<RemoteExerciseSetDto> getSets() {
        return sets;
    }

    public void setSets(List<RemoteExerciseSetDto> sets) {
        this.sets = sets;
    }
}
