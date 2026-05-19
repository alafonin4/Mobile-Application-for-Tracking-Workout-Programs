package ru.alafonin4.workoutservice.dto;

public class MuscleBalanceDto {
    private String muscleGroup;
    private double totalVolume;
    private double sharePercent;
    private String status;

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
     * Returns the share percent.
     * @return calculated numeric value
     */
    public double getSharePercent() {
        return sharePercent;
    }

    /**
     * Updates the share percent.
     * @param sharePercent new share percent
     */
    public void setSharePercent(double sharePercent) {
        this.sharePercent = sharePercent;
    }

    /**
     * Returns the status.
     * @return resulting text value
     */
    public String getStatus() {
        return status;
    }

    /**
     * Updates the status.
     * @param status new status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
