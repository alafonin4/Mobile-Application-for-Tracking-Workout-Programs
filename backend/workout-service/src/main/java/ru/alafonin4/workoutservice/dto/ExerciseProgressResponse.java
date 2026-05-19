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
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<ExerciseProgressPointDto> timeline = new ArrayList<>();

    /**
     * Returns the identifier of the user.
     * @return result of the operation
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Updates the identifier of the user.
     * @param userId identifier of the user
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the identifier of the exercise.
     * @return result of the operation
     */
    public Long getExerciseId() {
        return exerciseId;
    }

    /**
     * Updates the identifier of the exercise.
     * @param exerciseId identifier of the exercise
     */
    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    /**
     * Returns the exercise name.
     * @return resulting text value
     */
    public String getExerciseName() {
        return exerciseName;
    }

    /**
     * Updates the exercise name.
     * @param exerciseName new exercise name
     */
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    /**
     * Returns the muscle group.
     * @return resulting text value
     */
    public String getMuscleGroup() {
        return muscleGroup;
    }

    /**
     * Updates the muscle group.
     * @param muscleGroup new muscle group
     */
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    /**
     * Returns the period months.
     * @return calculated numeric value
     */
    public int getPeriodMonths() {
        return periodMonths;
    }

    /**
     * Updates the period months.
     * @param periodMonths new period months
     */
    public void setPeriodMonths(int periodMonths) {
        this.periodMonths = periodMonths;
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
     * Returns the estimated one rep max.
     * @return calculated numeric value
     */
    public double getEstimatedOneRepMax() {
        return estimatedOneRepMax;
    }

    /**
     * Updates the estimated one rep max.
     * @param estimatedOneRepMax new estimated one rep max
     */
    public void setEstimatedOneRepMax(double estimatedOneRepMax) {
        this.estimatedOneRepMax = estimatedOneRepMax;
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
     * Returns the timeline.
     * @return prepared list with the requested data
     */
    public List<ExerciseProgressPointDto> getTimeline() {
        return timeline;
    }

    /**
     * Updates the timeline.
     * @param timeline new timeline
     */
    public void setTimeline(List<ExerciseProgressPointDto> timeline) {
        this.timeline = timeline;
    }
}
