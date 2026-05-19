package ru.alafonin4.workoutservice.dto;

public class ExerciseCatalogItemDto {
    private Long id;
    private String name;
    private String muscleGroup;
    private String description;
    private String techniqueUrl;
    private Boolean requiresAdditionalWeight;
    private Boolean favorite;

    /**
     * Returns the id.
     * @return result of the operation
     */
    public Long getId() {
        return id;
    }

    /**
     * Updates the id.
     * @param id identifier of the target record
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name.
     * @return resulting text value
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name.
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the muscle group.
     * @return resulting text value
     */
    public String getMuscleGroup() {
        return muscleGroup;
    }

    /**
     * Updates the muscle group.
     * @param muscleGroup new muscle group
     */
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
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
     * Returns the technique url.
     * @return resulting text value
     */
    public String getTechniqueUrl() {
        return techniqueUrl;
    }

    /**
     * Updates the technique url.
     * @param techniqueUrl new technique url
     */
    public void setTechniqueUrl(String techniqueUrl) {
        this.techniqueUrl = techniqueUrl;
    }

    /**
     * Returns the requires additional weight.
     * @return true when the condition is satisfied; otherwise false
     */
    public Boolean getRequiresAdditionalWeight() {
        return requiresAdditionalWeight;
    }

    /**
     * Updates the requires additional weight.
     * @param requiresAdditionalWeight new requires additional weight
     */
    public void setRequiresAdditionalWeight(Boolean requiresAdditionalWeight) {
        this.requiresAdditionalWeight = requiresAdditionalWeight;
    }

    /**
     * Returns the favorite.
     * @return true when the condition is satisfied; otherwise false
     */
    public Boolean getFavorite() {
        return favorite;
    }

    /**
     * Updates the favorite.
     * @param favorite new favorite
     */
    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}
