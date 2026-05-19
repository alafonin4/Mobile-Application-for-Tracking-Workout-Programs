package ru.alafonin4.socialservice.dto;

import java.util.ArrayList;
import java.util.List;

public class RemoteWorkoutExerciseDto {
    private RemoteExerciseDto exercise;
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<RemoteExerciseSetDto> sets = new ArrayList<>();

    /**
     * Returns the exercise.
     * @return result of the operation
     */
    public RemoteExerciseDto getExercise() {
        return exercise;
    }

    /**
     * Updates the exercise.
     * @param exercise exercise being processed
     */
    public void setExercise(RemoteExerciseDto exercise) {
        this.exercise = exercise;
    }

    /**
     * Returns the sets.
     * @return prepared list with the requested data
     */
    public List<RemoteExerciseSetDto> getSets() {
        return sets;
    }

    /**
     * Updates the sets.
     * @param sets new sets
     */
    public void setSets(List<RemoteExerciseSetDto> sets) {
        this.sets = sets;
    }
}
