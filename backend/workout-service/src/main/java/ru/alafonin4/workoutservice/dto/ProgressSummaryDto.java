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
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<ProgressComponentDto> components = new ArrayList<>();

    /**
     * Returns the workouts count.
     * @return calculated numeric value
     */
    public int getWorkoutsCount() {
        return workoutsCount;
    }

    /**
     * Updates the workouts count.
     * @param workoutsCount new workouts count
     */
    public void setWorkoutsCount(int workoutsCount) {
        this.workoutsCount = workoutsCount;
    }

    /**
     * Returns the exercises count.
     * @return calculated numeric value
     */
    public int getExercisesCount() {
        return exercisesCount;
    }

    /**
     * Updates the exercises count.
     * @param exercisesCount new exercises count
     */
    public void setExercisesCount(int exercisesCount) {
        this.exercisesCount = exercisesCount;
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

    /**
     * Returns the total sets.
     * @return calculated numeric value
     */
    public int getTotalSets() {
        return totalSets;
    }

    /**
     * Updates the total sets.
     * @param totalSets new total sets
     */
    public void setTotalSets(int totalSets) {
        this.totalSets = totalSets;
    }

    /**
     * Returns the total volume.
     * @return calculated numeric value
     */
    public double getTotalVolume() {
        return totalVolume;
    }

    /**
     * Updates the total volume.
     * @param totalVolume new total volume
     */
    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
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
     * Returns the progress percent.
     * @return calculated numeric value
     */
    public double getProgressPercent() {
        return progressPercent;
    }

    /**
     * Updates the progress percent.
     * @param progressPercent new progress percent
     */
    public void setProgressPercent(double progressPercent) {
        this.progressPercent = progressPercent;
    }

    /**
     * Returns the composite score.
     * @return calculated numeric value
     */
    public double getCompositeScore() {
        return compositeScore;
    }

    /**
     * Updates the composite score.
     * @param compositeScore new composite score
     */
    public void setCompositeScore(double compositeScore) {
        this.compositeScore = compositeScore;
    }

    /**
     * Returns the average session volume.
     * @return calculated numeric value
     */
    public double getAverageSessionVolume() {
        return averageSessionVolume;
    }

    /**
     * Updates the average session volume.
     * @param averageSessionVolume new average session volume
     */
    public void setAverageSessionVolume(double averageSessionVolume) {
        this.averageSessionVolume = averageSessionVolume;
    }

    /**
     * Returns the average intensity.
     * @return calculated numeric value
     */
    public double getAverageIntensity() {
        return averageIntensity;
    }

    /**
     * Updates the average intensity.
     * @param averageIntensity new average intensity
     */
    public void setAverageIntensity(double averageIntensity) {
        this.averageIntensity = averageIntensity;
    }

    /**
     * Returns the average density.
     * @return calculated numeric value
     */
    public double getAverageDensity() {
        return averageDensity;
    }

    /**
     * Updates the average density.
     * @param averageDensity new average density
     */
    public void setAverageDensity(double averageDensity) {
        this.averageDensity = averageDensity;
    }

    /**
     * Returns the peak estimated one rep max.
     * @return calculated numeric value
     */
    public double getPeakEstimatedOneRepMax() {
        return peakEstimatedOneRepMax;
    }

    /**
     * Updates the peak estimated one rep max.
     * @param peakEstimatedOneRepMax new peak estimated one rep max
     */
    public void setPeakEstimatedOneRepMax(double peakEstimatedOneRepMax) {
        this.peakEstimatedOneRepMax = peakEstimatedOneRepMax;
    }

    /**
     * Returns the volume trend percent.
     * @return calculated numeric value
     */
    public double getVolumeTrendPercent() {
        return volumeTrendPercent;
    }

    /**
     * Updates the volume trend percent.
     * @param volumeTrendPercent new volume trend percent
     */
    public void setVolumeTrendPercent(double volumeTrendPercent) {
        this.volumeTrendPercent = volumeTrendPercent;
    }

    /**
     * Returns the intensity trend percent.
     * @return calculated numeric value
     */
    public double getIntensityTrendPercent() {
        return intensityTrendPercent;
    }

    /**
     * Updates the intensity trend percent.
     * @param intensityTrendPercent new intensity trend percent
     */
    public void setIntensityTrendPercent(double intensityTrendPercent) {
        this.intensityTrendPercent = intensityTrendPercent;
    }

    /**
     * Returns the density trend percent.
     * @return calculated numeric value
     */
    public double getDensityTrendPercent() {
        return densityTrendPercent;
    }

    /**
     * Updates the density trend percent.
     * @param densityTrendPercent new density trend percent
     */
    public void setDensityTrendPercent(double densityTrendPercent) {
        this.densityTrendPercent = densityTrendPercent;
    }

    /**
     * Returns the consistency score.
     * @return calculated numeric value
     */
    public double getConsistencyScore() {
        return consistencyScore;
    }

    /**
     * Updates the consistency score.
     * @param consistencyScore new consistency score
     */
    public void setConsistencyScore(double consistencyScore) {
        this.consistencyScore = consistencyScore;
    }

    /**
     * Returns the recovery score.
     * @return calculated numeric value
     */
    public double getRecoveryScore() {
        return recoveryScore;
    }

    /**
     * Updates the recovery score.
     * @param recoveryScore calculated recovery score
     */
    public void setRecoveryScore(double recoveryScore) {
        this.recoveryScore = recoveryScore;
    }

    /**
     * Returns the balance score.
     * @return calculated numeric value
     */
    public double getBalanceScore() {
        return balanceScore;
    }

    /**
     * Updates the balance score.
     * @param balanceScore new balance score
     */
    public void setBalanceScore(double balanceScore) {
        this.balanceScore = balanceScore;
    }

    /**
     * Returns the diversity score.
     * @return calculated numeric value
     */
    public double getDiversityScore() {
        return diversityScore;
    }

    /**
     * Updates the diversity score.
     * @param diversityScore new diversity score
     */
    public void setDiversityScore(double diversityScore) {
        this.diversityScore = diversityScore;
    }

    /**
     * Returns the personal record score.
     * @return calculated numeric value
     */
    public double getPersonalRecordScore() {
        return personalRecordScore;
    }

    /**
     * Updates the personal record score.
     * @param personalRecordScore new personal record score
     */
    public void setPersonalRecordScore(double personalRecordScore) {
        this.personalRecordScore = personalRecordScore;
    }

    /**
     * Returns the components.
     * @return prepared list with the requested data
     */
    public List<ProgressComponentDto> getComponents() {
        return components;
    }

    /**
     * Updates the components.
     * @param components new components
     */
    public void setComponents(List<ProgressComponentDto> components) {
        this.components = components;
    }
}
