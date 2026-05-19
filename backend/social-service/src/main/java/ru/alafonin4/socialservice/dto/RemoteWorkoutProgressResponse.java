package ru.alafonin4.socialservice.dto;

public class RemoteWorkoutProgressResponse {
    private Long userId;
    private int periodMonths;
    private RemoteProgressSummaryDto summary;

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
     * Returns the summary.
     * @return result of the operation
     */
    public RemoteProgressSummaryDto getSummary() {
        return summary;
    }

    /**
     * Updates the summary.
     * @param summary new summary
     */
    public void setSummary(RemoteProgressSummaryDto summary) {
        this.summary = summary;
    }
}
