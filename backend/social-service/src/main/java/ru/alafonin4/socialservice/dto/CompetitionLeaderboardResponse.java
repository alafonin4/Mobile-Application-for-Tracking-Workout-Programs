package ru.alafonin4.socialservice.dto;

import java.util.ArrayList;
import java.util.List;

public class CompetitionLeaderboardResponse {
    private String scope;
    private String title;
    private String description;
    private String goalType;
    private String goalLabel;
    private String metricLabel;
    private Long competitionId;
    private String startsAt;
    private String endsAt;
    private Integer currentUserRank;
    private List<CompetitionLeaderboardEntryDto> entries = new ArrayList<>();

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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

    public String getMetricLabel() {
        return metricLabel;
    }

    public void setMetricLabel(String metricLabel) {
        this.metricLabel = metricLabel;
    }

    public Long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public String getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(String endsAt) {
        this.endsAt = endsAt;
    }

    public Integer getCurrentUserRank() {
        return currentUserRank;
    }

    public void setCurrentUserRank(Integer currentUserRank) {
        this.currentUserRank = currentUserRank;
    }

    public List<CompetitionLeaderboardEntryDto> getEntries() {
        return entries;
    }

    public void setEntries(List<CompetitionLeaderboardEntryDto> entries) {
        this.entries = entries;
    }
}
