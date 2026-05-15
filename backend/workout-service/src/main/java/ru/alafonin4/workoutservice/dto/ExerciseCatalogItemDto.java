package ru.alafonin4.workoutservice.dto;

public class ExerciseCatalogItemDto {
    private Long id;
    private String name;
    private String muscleGroup;
    private String description;
    private String techniqueUrl;
    private Boolean requiresAdditionalWeight;
    private Boolean favorite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechniqueUrl() {
        return techniqueUrl;
    }

    public void setTechniqueUrl(String techniqueUrl) {
        this.techniqueUrl = techniqueUrl;
    }

    public Boolean getRequiresAdditionalWeight() {
        return requiresAdditionalWeight;
    }

    public void setRequiresAdditionalWeight(Boolean requiresAdditionalWeight) {
        this.requiresAdditionalWeight = requiresAdditionalWeight;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
