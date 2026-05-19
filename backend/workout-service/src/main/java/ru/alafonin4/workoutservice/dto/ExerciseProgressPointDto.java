package ru.alafonin4.workoutservice.dto;

public class ExerciseProgressPointDto {
    private String date;
    private double volume;
    private double maxWeight;
    private int totalReps;

    /**
     * Returns the date.
     * @return resulting text value
     */
    public String getDate() {
        return date;
    }

    /**
     * Updates the date.
     * @param date date being processed
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the volume.
     * @return calculated numeric value
     */
    public double getVolume() {
        return volume;
    }

    /**
     * Updates the volume.
     * @param volume new volume
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * Returns the max weight.
     * @return calculated numeric value
     */
    public double getMaxWeight() {
        return maxWeight;
    }

    /**
     * Updates the max weight.
     * @param maxWeight new max weight
     */
    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    /**
     * Returns the total reps.
     * @return calculated numeric value
     */
    public int getTotalReps() {
        return totalReps;
    }

    /**
     * Updates the total reps.
     * @param totalReps new total reps
     */
    public void setTotalReps(int totalReps) {
        this.totalReps = totalReps;
    }
}
