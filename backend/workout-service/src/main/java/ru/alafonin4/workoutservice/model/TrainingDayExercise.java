package ru.alafonin4.workoutservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "training_day_exercises")
public class TrainingDayExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_day_id")
    private TrainingDay trainingDay;

    private Long exerciseId;

    private Integer recommendedSets;
    private Integer recommendedReps;
    private Double recommendedWeight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrainingDay getTrainingDay() {
        return trainingDay;
    }

    public void setTrainingDay(TrainingDay trainingDay) {
        this.trainingDay = trainingDay;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getRecommendedSets() {
        return recommendedSets;
    }

    public void setRecommendedSets(Integer recommendedSets) {
        this.recommendedSets = recommendedSets;
    }

    public Integer getRecommendedReps() {
        return recommendedReps;
    }

    public void setRecommendedReps(Integer recommendedReps) {
        this.recommendedReps = recommendedReps;
    }

    public Double getRecommendedWeight() {
        return recommendedWeight;
    }

    public void setRecommendedWeight(Double recommendedWeight) {
        this.recommendedWeight = recommendedWeight;
    }
}
