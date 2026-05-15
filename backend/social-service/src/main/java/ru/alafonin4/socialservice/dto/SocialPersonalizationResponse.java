package ru.alafonin4.socialservice.dto;

import java.util.ArrayList;
import java.util.List;

public class SocialPersonalizationResponse {
    private Long userId;
    private int completedCompetitionsCount;
    private double monthlyGlobalPercentile;
    private List<SocialAchievementDto> achievements = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getCompletedCompetitionsCount() {
        return completedCompetitionsCount;
    }

    public void setCompletedCompetitionsCount(int completedCompetitionsCount) {
        this.completedCompetitionsCount = completedCompetitionsCount;
    }

    public double getMonthlyGlobalPercentile() {
        return monthlyGlobalPercentile;
    }

    public void setMonthlyGlobalPercentile(double monthlyGlobalPercentile) {
        this.monthlyGlobalPercentile = monthlyGlobalPercentile;
    }

    public List<SocialAchievementDto> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<SocialAchievementDto> achievements) {
        this.achievements = achievements;
    }
}
