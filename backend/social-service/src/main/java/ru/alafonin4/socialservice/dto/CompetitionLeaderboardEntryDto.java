package ru.alafonin4.socialservice.dto;

public class CompetitionLeaderboardEntryDto {
    private int rank;
    private Long userId;
    private String userName;
    private String avatarUrl;
    private double score;
    private double currentValue;
    private Double targetValue;
    private double goalProgressPercent;
    private double progressPercent;
    private boolean currentUser;
    private String subtitle;

    /**
     * Returns the rank.
     * @return calculated numeric value
     */
    public int getRank() {
        return rank;
    }

    /**
     * Updates the rank.
     * @param rank new rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

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
     * Returns the user name.
     * @return resulting text value
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Updates the user name.
     * @param userName new user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns the avatar url.
     * @return resulting text value
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Updates the avatar url.
     * @param avatarUrl new avatar url
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * Returns the score.
     * @return calculated numeric value
     */
    public double getScore() {
        return score;
    }

    /**
     * Updates the score.
     * @param score new score
     */
    public void setScore(double score) {
        this.score = score;
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
     * Returns the goal progress percent.
     * @return calculated numeric value
     */
    public double getGoalProgressPercent() {
        return goalProgressPercent;
    }

    /**
     * Updates the goal progress percent.
     * @param goalProgressPercent new goal progress percent
     */
    public void setGoalProgressPercent(double goalProgressPercent) {
        this.goalProgressPercent = goalProgressPercent;
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
     * Indicates whether current user.
     * @return true when the condition is satisfied; otherwise false
     */
    public boolean isCurrentUser() {
        return currentUser;
    }

    /**
     * Updates the current user.
     * @param currentUser new current user
     */
    public void setCurrentUser(boolean currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Returns the subtitle.
     * @return resulting text value
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Updates the subtitle.
     * @param subtitle new subtitle
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
