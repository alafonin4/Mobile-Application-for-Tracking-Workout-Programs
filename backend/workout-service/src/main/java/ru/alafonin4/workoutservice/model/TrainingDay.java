package ru.alafonin4.workoutservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "training_days")
public class TrainingDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id")
    private TrainingProgram trainingProgram;

    // Например, "Понедельник" или "День 1"
    private String dayIdentifier;

    // Группа мышц или тип тренировки (full-body)
    private String muscleGroup;

    @OneToMany(mappedBy = "trainingDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrainingDayExercise> exercises = new ArrayList<>();

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public TrainingProgram getTrainingProgram() {
        return trainingProgram;
    }
    public void setTrainingProgram(TrainingProgram trainingProgram) {
        this.trainingProgram = trainingProgram;
    }
    public String getDayIdentifier() {
        return dayIdentifier;
    }
    public void setDayIdentifier(String dayIdentifier) {
        this.dayIdentifier = dayIdentifier;
    }
    public String getMuscleGroup() {
        return muscleGroup;
    }
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
    public List<TrainingDayExercise> getExercises() {
        return exercises;
    }
    public void setExercises(List<TrainingDayExercise> exercises) {
        this.exercises = exercises;
    }
}
