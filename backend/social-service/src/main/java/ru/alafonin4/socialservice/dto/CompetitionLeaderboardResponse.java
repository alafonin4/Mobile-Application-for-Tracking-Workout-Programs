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
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<CompetitionLeaderboardEntryDto> entries = new ArrayList<>();

    /**
     * Returns the scope.
     * @return resulting text value
     */
    public String getScope() {
        return scope;
    }

    /**
     * Updates the scope.
     * @param scope new scope
     */
    public void setScope(String scope) {
        this.scope = scope;
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
     * Returns the metric label.
     * @return resulting text value
     */
    public String getMetricLabel() {
        return metricLabel;
    }

    /**
     * Updates the metric label.
     * @param metricLabel new metric label
     */
    public void setMetricLabel(String metricLabel) {
        this.metricLabel = metricLabel;
    }

    /**
     * Returns the identifier of the competition.
     * @return result of the operation
     */
    public Long getCompetitionId() {
        return competitionId;
    }

    /**
     * Updates the identifier of the competition.
     * @param competitionId identifier of the competition
     */
    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }

    /**
     * Returns the starts at.
     * @return resulting text value
     */
    public String getStartsAt() {
        return startsAt;
    }

    /**
     * Updates the starts at.
     * @param startsAt new starts at
     */
    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    /**
     * Returns the ends at.
     * @return resulting text value
     */
    public String getEndsAt() {
        return endsAt;
    }

    /**
     * Updates the ends at.
     * @param endsAt new ends at
     */
    public void setEndsAt(String endsAt) {
        this.endsAt = endsAt;
    }

    /**
     * Returns the current user rank.
     * @return calculated numeric value
     */
    public Integer getCurrentUserRank() {
        return currentUserRank;
    }

    /**
     * Updates the current user rank.
     * @param currentUserRank new current user rank
     */
    public void setCurrentUserRank(Integer currentUserRank) {
        this.currentUserRank = currentUserRank;
    }

    /**
     * Returns the entries.
     * @return prepared list with the requested data
     */
    public List<CompetitionLeaderboardEntryDto> getEntries() {
        return entries;
    }

    /**
     * Updates the entries.
     * @param entries new entries
     */
    public void setEntries(List<CompetitionLeaderboardEntryDto> entries) {
        this.entries = entries;
    }
}
