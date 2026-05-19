package ru.alafonin4.workoutservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "exercises")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String muscleGroup;

    private String techniqueUrl;

    @Column(length = 1000)
    private String description;

    private Boolean requiresAdditionalWeight = Boolean.FALSE;

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
}
