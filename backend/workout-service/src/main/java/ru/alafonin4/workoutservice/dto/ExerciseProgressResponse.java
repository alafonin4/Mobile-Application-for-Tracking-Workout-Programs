package ru.alafonin4.workoutservice.dto;

import java.util.ArrayList;
import java.util.List;

public class ExerciseProgressResponse {
    private Long userId;
    private Long exerciseId;
    private String exerciseName;
    private String muscleGroup;
    private int periodMonths;
    private double totalVolume;
    private double maxWeight;
    private int totalReps;
    private double progressPercent;
    private double compositeScore;
    private double averageIntensity;
    private double averageDensity;
    private double estimatedOneRepMax;
    private double volumeTrendPercent;
    private double intensityTrendPercent;
    private double densityTrendPercent;
    private double personalRecordScore;
    private List<ExerciseProgressPointDto> timeline = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public int getPeriodMonths() {
        return periodMonths;
    }

    public void setPeriodMonths(int periodMonths) {
        this.periodMonths = periodMonths;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public int getTotalReps() {
        return totalReps;
    }

    public void setTotalReps(int totalReps) {
        this.totalReps = totalReps;
    }

    public double getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(double progressPercent) {
        this.progressPercent = progressPercent;
    }

    public double getCompositeScore() {
        return compositeScore;
    }

    public void setCompositeScore(double compositeScore) {
        this.compositeScore = compositeScore;
    }

    public double getAverageIntensity() {
        return averageIntensity;
    }

    public void setAverageIntensity(double averageIntensity) {
        this.averageIntensity = averageIntensity;
    }

    public double getAverageDensity() {
        return averageDensity;
    }

    public void setAverageDensity(double averageDensity) {
        this.averageDensity = averageDensity;
    }

    public double getEstimatedOneRepMax() {
        return estimatedOneRepMax;
    }

    public void setEstimatedOneRepMax(double estimatedOneRepMax) {
        this.estimatedOneRepMax = estimatedOneRepMax;
    }

    public double getVolumeTrendPercent() {
        return volumeTrendPercent;
    }

    public void setVolumeTrendPercent(double volumeTrendPercent) {
        this.volumeTrendPercent = volumeTrendPercent;
    }

    public double getIntensityTrendPercent() {
        return intensityTrendPercent;
    }

    public void setIntensityTrendPercent(double intensityTrendPercent) {
        this.intensityTrendPercent = intensityTrendPercent;
    }

    public double getDensityTrendPercent() {
        return densityTrendPercent;
    }

    public void setDensityTrendPercent(double densityTrendPercent) {
        this.densityTrendPercent = densityTrendPercent;
    }

    public double getPersonalRecordScore() {
        return personalRecordScore;
    }

    public void setPersonalRecordScore(double personalRecordScore) {
        this.personalRecordScore = personalRecordScore;
    }

    public List<ExerciseProgressPointDto> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<ExerciseProgressPointDto> timeline) {
        this.timeline = timeline;
    }
}
