package ru.alafonin4.workoutservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "training_days")
public class TrainingDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private TrainingProgram trainingProgram;

    private String dayIdentifier;

    private String muscleGroup;

    /**
     * ArrayList<>.
     * @return result of the operation
     */
    @JsonManagedReference
    @OneToMany(mappedBy = "trainingDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingDayExercise> exercises = new ArrayList<>();

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
     * Returns the training program.
     * @return result of the operation
     */
    public TrainingProgram getTrainingProgram() {
        return trainingProgram;
    }

    /**
     * Updates the training program.
     * @param trainingProgram new training program
     */
    public void setTrainingProgram(TrainingProgram trainingProgram) {
        this.trainingProgram = trainingProgram;
    }

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
     * Returns the exercises.
     * @return prepared list with the requested data
     */
    public List<TrainingDayExercise> getExercises() {
        return exercises;
    }

    /**
     * Updates the exercises.
     * @param exercises new exercises
     */
    public void setExercises(List<TrainingDayExercise> exercises) {
        this.exercises = exercises;
    }
}
