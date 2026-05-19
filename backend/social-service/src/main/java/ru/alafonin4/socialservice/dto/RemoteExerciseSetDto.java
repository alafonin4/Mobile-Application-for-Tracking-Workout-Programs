package ru.alafonin4.socialservice.dto;

public class RemoteExerciseSetDto {
    private Integer reps;
    private Double weight;

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
}
