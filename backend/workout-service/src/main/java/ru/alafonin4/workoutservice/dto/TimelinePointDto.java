package ru.alafonin4.workoutservice.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class TimelinePointDto {
    private String date;
    private double overallVolume;
    /**
     * LinkedHashMap<>.
     * @return result of the operation
     */
    private Map<String, Double> muscleGroupVolumes = new LinkedHashMap<>();

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
     * Returns the overall volume.
     * @return calculated numeric value
     */
    public double getOverallVolume() {
        return overallVolume;
    }

    /**
     * Updates the overall volume.
     * @param overallVolume new overall volume
     */
    public void setOverallVolume(double overallVolume) {
        this.overallVolume = overallVolume;
    }

    /**
     * Returns the muscle group volumes.
     * @return result of the operation
     */
    public Map<String, Double> getMuscleGroupVolumes() {
        return muscleGroupVolumes;
    }

    /**
     * Updates the muscle group volumes.
     * @param muscleGroupVolumes new muscle group volumes
     */
    public void setMuscleGroupVolumes(Map<String, Double> muscleGroupVolumes) {
        this.muscleGroupVolumes = muscleGroupVolumes;
    }
}
