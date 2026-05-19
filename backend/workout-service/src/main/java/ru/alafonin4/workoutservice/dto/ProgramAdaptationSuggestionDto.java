package ru.alafonin4.workoutservice.dto;

public class ProgramAdaptationSuggestionDto {
    private String dayIdentifier;
    private Long exerciseId;
    private String exerciseName;
    private String recommendationType;
    private String title;
    private String reason;
    private Double currentRecommendedWeight;
    private Double suggestedWeight;
    private Integer currentRecommendedSets;
    private Integer suggestedSets;
    private Integer currentRecommendedReps;
    private Integer suggestedReps;

    /**
     * Returns the day identifier.
     * @return resulting text value
     */
    public String getDayIdentifier() {
        return dayIdentifier;
    }

    /**
     * Updates the day identifier.
     * @param dayIdentifier identifier of the training day
     */
    public void setDayIdentifier(String dayIdentifier) {
        this.dayIdentifier = dayIdentifier;
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
     * Returns the recommendation type.
     * @return resulting text value
     */
    public String getRecommendationType() {
        return recommendationType;
    }

    /**
     * Updates the recommendation type.
     * @param recommendationType new recommendation type
     */
    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
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
     * Returns the reason.
     * @return resulting text value
     */
    public String getReason() {
        return reason;
    }

    /**
     * Updates the reason.
     * @param reason new reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Returns the current recommended weight.
     * @return calculated numeric value
     */
    public Double getCurrentRecommendedWeight() {
        return currentRecommendedWeight;
    }

    /**
     * Updates the current recommended weight.
     * @param currentRecommendedWeight new current recommended weight
     */
    public void setCurrentRecommendedWeight(Double currentRecommendedWeight) {
        this.currentRecommendedWeight = currentRecommendedWeight;
    }

    /**
     * Returns the suggested weight.
     * @return calculated numeric value
     */
    public Double getSuggestedWeight() {
        return suggestedWeight;
    }

    /**
     * Updates the suggested weight.
     * @param suggestedWeight new suggested weight
     */
    public void setSuggestedWeight(Double suggestedWeight) {
        this.suggestedWeight = suggestedWeight;
    }

    /**
     * Returns the current recommended sets.
     * @return calculated numeric value
     */
    public Integer getCurrentRecommendedSets() {
        return currentRecommendedSets;
    }

    /**
     * Updates the current recommended sets.
     * @param currentRecommendedSets new current recommended sets
     */
    public void setCurrentRecommendedSets(Integer currentRecommendedSets) {
        this.currentRecommendedSets = currentRecommendedSets;
    }

    /**
     * Returns the suggested sets.
     * @return calculated numeric value
     */
    public Integer getSuggestedSets() {
        return suggestedSets;
    }

    /**
     * Updates the suggested sets.
     * @param suggestedSets new suggested sets
     */
    public void setSuggestedSets(Integer suggestedSets) {
        this.suggestedSets = suggestedSets;
    }

    /**
     * Returns the current recommended reps.
     * @return calculated numeric value
     */
    public Integer getCurrentRecommendedReps() {
        return currentRecommendedReps;
    }

    /**
     * Updates the current recommended reps.
     * @param currentRecommendedReps new current recommended reps
     */
    public void setCurrentRecommendedReps(Integer currentRecommendedReps) {
        this.currentRecommendedReps = currentRecommendedReps;
    }

    /**
     * Returns the suggested reps.
     * @return calculated numeric value
     */
    public Integer getSuggestedReps() {
        return suggestedReps;
    }

    /**
     * Updates the suggested reps.
     * @param suggestedReps new suggested reps
     */
    public void setSuggestedReps(Integer suggestedReps) {
        this.suggestedReps = suggestedReps;
    }
}
