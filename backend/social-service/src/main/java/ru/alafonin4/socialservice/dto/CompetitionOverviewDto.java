package ru.alafonin4.socialservice.dto;

public class CompetitionOverviewDto {
    private Long id;
    private String title;
    private String description;
    private String goalType;
    private String goalLabel;
    private String exerciseName;
    private Double targetValue;
    private Integer periodMonths;
    private Long creatorId;
    private String creatorName;
    private boolean createdByCurrentUser;
    private String currentUserStatus;
    private int acceptedParticipantsCount;
    private int pendingParticipantsCount;
    private double currentValue;
    private double progressPercent;
    private boolean targetReached;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public String getGoalLabel() {
        return goalLabel;
    }

    public void setGoalLabel(String goalLabel) {
        this.goalLabel = goalLabel;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public Integer getPeriodMonths() {
        return periodMonths;
    }

    public void setPeriodMonths(Integer periodMonths) {
        this.periodMonths = periodMonths;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public boolean isCreatedByCurrentUser() {
        return createdByCurrentUser;
    }

    public void setCreatedByCurrentUser(boolean createdByCurrentUser) {
        this.createdByCurrentUser = createdByCurrentUser;
    }

    public String getCurrentUserStatus() {
        return currentUserStatus;
    }

    public void setCurrentUserStatus(String currentUserStatus) {
        this.currentUserStatus = currentUserStatus;
    }

    public int getAcceptedParticipantsCount() {
        return acceptedParticipantsCount;
    }

    public void setAcceptedParticipantsCount(int acceptedParticipantsCount) {
        this.acceptedParticipantsCount = acceptedParticipantsCount;
    }

    public int getPendingParticipantsCount() {
        return pendingParticipantsCount;
    }

    public void setPendingParticipantsCount(int pendingParticipantsCount) {
        this.pendingParticipantsCount = pendingParticipantsCount;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(double progressPercent) {
        this.progressPercent = progressPercent;
    }

    public boolean isTargetReached() {
        return targetReached;
    }

    public void setTargetReached(boolean targetReached) {
        this.targetReached = targetReached;
    }
}
