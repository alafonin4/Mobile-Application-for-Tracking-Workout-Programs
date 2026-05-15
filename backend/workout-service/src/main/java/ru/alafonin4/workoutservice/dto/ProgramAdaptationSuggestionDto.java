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

    public String getDayIdentifier() {
        return dayIdentifier;
    }

    public void setDayIdentifier(String dayIdentifier) {
        this.dayIdentifier = dayIdentifier;
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

    public String getRecommendationType() {
        return recommendationType;
    }

    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getCurrentRecommendedWeight() {
        return currentRecommendedWeight;
    }

    public void setCurrentRecommendedWeight(Double currentRecommendedWeight) {
        this.currentRecommendedWeight = currentRecommendedWeight;
    }

    public Double getSuggestedWeight() {
        return suggestedWeight;
    }

    public void setSuggestedWeight(Double suggestedWeight) {
        this.suggestedWeight = suggestedWeight;
    }

    public Integer getCurrentRecommendedSets() {
        return currentRecommendedSets;
    }

    public void setCurrentRecommendedSets(Integer currentRecommendedSets) {
        this.currentRecommendedSets = currentRecommendedSets;
    }

    public Integer getSuggestedSets() {
        return suggestedSets;
    }

    public void setSuggestedSets(Integer suggestedSets) {
        this.suggestedSets = suggestedSets;
    }

    public Integer getCurrentRecommendedReps() {
        return currentRecommendedReps;
    }

    public void setCurrentRecommendedReps(Integer currentRecommendedReps) {
        this.currentRecommendedReps = currentRecommendedReps;
    }

    public Integer getSuggestedReps() {
        return suggestedReps;
    }

    public void setSuggestedReps(Integer suggestedReps) {
        this.suggestedReps = suggestedReps;
    }
}
