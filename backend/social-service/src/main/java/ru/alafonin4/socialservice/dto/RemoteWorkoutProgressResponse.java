package ru.alafonin4.socialservice.dto;

public class RemoteWorkoutProgressResponse {
    private Long userId;
    private int periodMonths;
    private RemoteProgressSummaryDto summary;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPeriodMonths() {
        return periodMonths;
    }

    public void setPeriodMonths(int periodMonths) {
        this.periodMonths = periodMonths;
    }

    public RemoteProgressSummaryDto getSummary() {
        return summary;
    }

    public void setSummary(RemoteProgressSummaryDto summary) {
        this.summary = summary;
    }
}
