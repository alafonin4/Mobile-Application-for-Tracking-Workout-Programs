package ru.alafonin4.workoutservice.dto;

public class MuscleGroupProgressDto {
    private String muscleGroup;
    private double totalVolume;
    private int totalSets;
    private int totalReps;
    private double progressPercent;
    private double normalizedScore;
    private double compositeScore;
    private double averageIntensity;
    private double peakEstimatedOneRepMax;

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public int getTotalSets() {
        return totalSets;
    }

    public void setTotalSets(int totalSets) {
        this.totalSets = totalSets;
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

    public double getNormalizedScore() {
        return normalizedScore;
    }

    public void setNormalizedScore(double normalizedScore) {
        this.normalizedScore = normalizedScore;
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

    public double getPeakEstimatedOneRepMax() {
        return peakEstimatedOneRepMax;
    }

    public void setPeakEstimatedOneRepMax(double peakEstimatedOneRepMax) {
        this.peakEstimatedOneRepMax = peakEstimatedOneRepMax;
    }
}
