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
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<Long> invitedUserIds = new ArrayList<>();

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
     * @return result of the operation
     */
    public CompetitionGoalType getGoalType() {
        return goalType;
    }

    /**
     * Updates the goal type.
     * @param goalType new goal type
     */
    public void setGoalType(CompetitionGoalType goalType) {
        this.goalType = goalType;
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
     * Returns the identifier of the exercise.
     * @return result of the operation
     */
    public Long getExerciseId() {
        return exerciseId;
    }

    /**
     * Updates the identifier of the exercise.
     * @param exerciseId identifier of the exercise
     */
    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
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
     * Returns the invited user ids.
     * @return prepared list with the requested data
     */
    public List<Long> getInvitedUserIds() {
        return invitedUserIds;
    }

    /**
     * Updates the invited user ids.
     * @param invitedUserIds new invited user ids
     */
    public void setInvitedUserIds(List<Long> invitedUserIds) {
        this.invitedUserIds = invitedUserIds;
    }
}
