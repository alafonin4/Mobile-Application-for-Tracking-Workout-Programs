package ru.alafonin4.workoutservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "training_programs")
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String name;

    @Column(length = 1000)
    private String description;

    /**
     * ArrayList<>.
     * @return result of the operation
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingDay> trainingDays = new ArrayList<>();

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
     * Returns the identifier of the user.
     * @return result of the operation
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Updates the identifier of the user.
     * @param userId identifier of the user
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
     * Returns the training days.
     * @return prepared list with the requested data
     */
    public List<TrainingDay> getTrainingDays() {
        return trainingDays;
    }

    /**
     * Updates the training days.
     * @param trainingDays new training days
     */
    public void setTrainingDays(List<TrainingDay> trainingDays) {
        this.trainingDays = trainingDays;
    }
}
