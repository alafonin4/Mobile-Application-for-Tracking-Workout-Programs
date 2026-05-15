package ru.alafonin4.workoutservice.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class TimelinePointDto {
    private String date;
    private double overallVolume;
    private Map<String, Double> muscleGroupVolumes = new LinkedHashMap<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getOverallVolume() {
        return overallVolume;
    }

    public void setOverallVolume(double overallVolume) {
        this.overallVolume = overallVolume;
    }

    public Map<String, Double> getMuscleGroupVolumes() {
        return muscleGroupVolumes;
    }

    public void setMuscleGroupVolumes(Map<String, Double> muscleGroupVolumes) {
        this.muscleGroupVolumes = muscleGroupVolumes;
    }
}
