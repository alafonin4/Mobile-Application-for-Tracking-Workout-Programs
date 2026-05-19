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
     * Returns the normalized score.
     * @return calculated numeric value
     */
    public double getNormalizedScore() {
        return normalizedScore;
    }

    /**
     * Updates the normalized score.
     * @param normalizedScore new normalized score
     */
    public void setNormalizedScore(double normalizedScore) {
        this.normalizedScore = normalizedScore;
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
}
