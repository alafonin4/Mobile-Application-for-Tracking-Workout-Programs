package ru.alafonin4.socialservice.dto;

import java.util.ArrayList;
import java.util.List;

public class SocialPersonalizationResponse {
    private Long userId;
    private int completedCompetitionsCount;
    private double monthlyGlobalPercentile;
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<SocialAchievementDto> achievements = new ArrayList<>();

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
     * Returns the completed competitions count.
     * @return calculated numeric value
     */
    public int getCompletedCompetitionsCount() {
        return completedCompetitionsCount;
    }

    /**
     * Updates the completed competitions count.
     * @param completedCompetitionsCount new completed competitions count
     */
    public void setCompletedCompetitionsCount(int completedCompetitionsCount) {
        this.completedCompetitionsCount = completedCompetitionsCount;
    }

    /**
     * Returns the monthly global percentile.
     * @return calculated numeric value
     */
    public double getMonthlyGlobalPercentile() {
        return monthlyGlobalPercentile;
    }

    /**
     * Updates the monthly global percentile.
     * @param monthlyGlobalPercentile new monthly global percentile
     */
    public void setMonthlyGlobalPercentile(double monthlyGlobalPercentile) {
        this.monthlyGlobalPercentile = monthlyGlobalPercentile;
    }

    /**
     * Returns the achievements.
     * @return prepared list with the requested data
     */
    public List<SocialAchievementDto> getAchievements() {
        return achievements;
    }

    /**
     * Updates the achievements.
     * @param achievements new achievements
     */
    public void setAchievements(List<SocialAchievementDto> achievements) {
        this.achievements = achievements;
    }
}
