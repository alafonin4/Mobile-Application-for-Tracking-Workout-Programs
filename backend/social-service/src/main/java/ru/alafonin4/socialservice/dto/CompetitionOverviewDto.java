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

    /**
     * Returns the id.
     * @return result of the operation
     */
    public Long getId() {
        return id;
    }

    /**
     * Updates the id.
     * @param id identifier of the target record
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the title.
     * @return resulting text value
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates the title.
     * @param title human-readable title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description.
     * @return resulting text value
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description.
     * @param description human-readable description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the goal type.
     * @return resulting text value
     */
    public String getGoalType() {
        return goalType;
    }

    /**
     * Updates the goal type.
     * @param goalType new goal type
     */
    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    /**
     * Returns the goal label.
     * @return resulting text value
     */
    public String getGoalLabel() {
        return goalLabel;
    }

    /**
     * Updates the goal label.
     * @param goalLabel new goal label
     */
    public void setGoalLabel(String goalLabel) {
        this.goalLabel = goalLabel;
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
     * Returns the target value.
     * @return calculated numeric value
     */
    public Double getTargetValue() {
        return targetValue;
    }

    /**
     * Updates the target value.
     * @param targetValue target metric value
     */
    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    /**
     * Returns the period months.
     * @return calculated numeric value
     */
    public Integer getPeriodMonths() {
        return periodMonths;
    }

    /**
     * Updates the period months.
     * @param periodMonths new period months
     */
    public void setPeriodMonths(Integer periodMonths) {
        this.periodMonths = periodMonths;
    }

    /**
     * Returns the identifier of the creator.
     * @return result of the operation
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * Updates the identifier of the creator.
     * @param creatorId new identifier of the creator
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * Returns the creator name.
     * @return resulting text value
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * Updates the creator name.
     * @param creatorName new creator name
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * Indicates whether created by current user.
     * @return true when the condition is satisfied; otherwise false
     */
    public boolean isCreatedByCurrentUser() {
        return createdByCurrentUser;
    }

    /**
     * Updates the created by current user.
     * @param createdByCurrentUser new created by current user
     */
    public void setCreatedByCurrentUser(boolean createdByCurrentUser) {
        this.createdByCurrentUser = createdByCurrentUser;
    }

    /**
     * Returns the current user status.
     * @return resulting text value
     */
    public String getCurrentUserStatus() {
        return currentUserStatus;
    }

    /**
     * Updates the current user status.
     * @param currentUserStatus new current user status
     */
    public void setCurrentUserStatus(String currentUserStatus) {
        this.currentUserStatus = currentUserStatus;
    }

    /**
     * Returns the accepted participants count.
     * @return calculated numeric value
     */
    public int getAcceptedParticipantsCount() {
        return acceptedParticipantsCount;
    }

    /**
     * Updates the accepted participants count.
     * @param acceptedParticipantsCount new accepted participants count
     */
    public void setAcceptedParticipantsCount(int acceptedParticipantsCount) {
        this.acceptedParticipantsCount = acceptedParticipantsCount;
    }

    /**
     * Returns the pending participants count.
     * @return calculated numeric value
     */
    public int getPendingParticipantsCount() {
        return pendingParticipantsCount;
    }

    /**
     * Updates the pending participants count.
     * @param pendingParticipantsCount new pending participants count
     */
    public void setPendingParticipantsCount(int pendingParticipantsCount) {
        this.pendingParticipantsCount = pendingParticipantsCount;
    }

    /**
     * Returns the current value.
     * @return calculated numeric value
     */
    public double getCurrentValue() {
        return currentValue;
    }

    /**
     * Updates the current value.
     * @param currentValue current metric value
     */
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
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
     * Indicates whether target reached.
     * @return true when the condition is satisfied; otherwise false
     */
    public boolean isTargetReached() {
        return targetReached;
    }

    /**
     * Updates the target reached.
     * @param targetReached new target reached
     */
    public void setTargetReached(boolean targetReached) {
        this.targetReached = targetReached;
    }
}
