package ru.alafonin4.workoutservice.dto;

import java.util.ArrayList;
import java.util.List;

public class ProgressSummaryDto {
    private int workoutsCount;
    private int exercisesCount;
    private int totalReps;
    private int totalSets;
    private double totalVolume;
    private double maxWeight;
    private double progressPercent;
    private double compositeScore;
    private double averageSessionVolume;
    private double averageIntensity;
    private double averageDensity;
    private double peakEstimatedOneRepMax;
    private double volumeTrendPercent;
    private double intensityTrendPercent;
    private double densityTrendPercent;
    private double consistencyScore;
    private double recoveryScore;
    private double balanceScore;
    private double diversityScore;
    private double personalRecordScore;
    private List<ProgressComponentDto> components = new ArrayList<>();

    public int getWorkoutsCount() {
        return workoutsCount;
    }

    public void setWorkoutsCount(int workoutsCount) {
        this.workoutsCount = workoutsCount;
    }

    public int getExercisesCount() {
        return exercisesCount;
    }

    public void setExercisesCount(int exercisesCount) {
        this.exercisesCount = exercisesCount;
    }

    public int getTotalReps() {
        return totalReps;
    }

    public void setTotalReps(int totalReps) {
        this.totalReps = totalReps;
    }

    public int getTotalSets() {
        return totalSets;
    }

    public void setTotalSets(int totalSets) {
        this.totalSets = totalSets;
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

    public double getAverageSessionVolume() {
        return averageSessionVolume;
    }

    public void setAverageSessionVolume(double averageSessionVolume) {
        this.averageSessionVolume = averageSessionVolume;
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

    public double getPeakEstimatedOneRepMax() {
        return peakEstimatedOneRepMax;
    }

    public void setPeakEstimatedOneRepMax(double peakEstimatedOneRepMax) {
        this.peakEstimatedOneRepMax = peakEstimatedOneRepMax;
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

    public double getConsistencyScore() {
        return consistencyScore;
    }

    public void setConsistencyScore(double consistencyScore) {
        this.consistencyScore = consistencyScore;
    }

    public double getRecoveryScore() {
        return recoveryScore;
    }

    public void setRecoveryScore(double recoveryScore) {
        this.recoveryScore = recoveryScore;
    }

    public double getBalanceScore() {
        return balanceScore;
    }

    public void setBalanceScore(double balanceScore) {
        this.balanceScore = balanceScore;
    }

    public double getDiversityScore() {
        return diversityScore;
    }

    public void setDiversityScore(double diversityScore) {
        this.diversityScore = diversityScore;
    }

    public double getPersonalRecordScore() {
        return personalRecordScore;
    }

    public void setPersonalRecordScore(double personalRecordScore) {
        this.personalRecordScore = personalRecordScore;
    }

    public List<ProgressComponentDto> getComponents() {
        return components;
    }

    public void setComponents(List<ProgressComponentDto> components) {
        this.components = components;
    }
}
