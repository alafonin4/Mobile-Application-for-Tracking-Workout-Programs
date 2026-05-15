package ru.alafonin4.socialservice.dto;

import ru.alafonin4.socialservice.enums.CompetitionGoalType;

import java.util.ArrayList;
import java.util.List;

public class CompetitionCreateRequest {
    private Long creatorId;
    private String title;
    private String description;
    private CompetitionGoalType goalType;
    private Double targetValue;
    private Long exerciseId;
    private String exerciseName;
    private Integer periodMonths;
    private List<Long> invitedUserIds = new ArrayList<>();

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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

    public CompetitionGoalType getGoalType() {
        return goalType;
    }

    public void setGoalType(CompetitionGoalType goalType) {
        this.goalType = goalType;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Integer getPeriodMonths() {
        return periodMonths;
    }

    public void setPeriodMonths(Integer periodMonths) {
        this.periodMonths = periodMonths;
    }

    public List<Long> getInvitedUserIds() {
        return invitedUserIds;
    }

    public void setInvitedUserIds(List<Long> invitedUserIds) {
        this.invitedUserIds = invitedUserIds;
    }
}
