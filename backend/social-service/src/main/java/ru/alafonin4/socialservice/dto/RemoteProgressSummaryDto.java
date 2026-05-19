package ru.alafonin4.socialservice.dto;

public class RemoteProgressSummaryDto {
    private int workoutsCount;
    private double progressPercent;
    private double compositeScore;

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
}
