package ru.alafonin4.workoutservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "training_day_exercises")
public class TrainingDayExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_day_id")
    private TrainingDay trainingDay;

    private Long exerciseId;

    private Integer recommendedSets;
    private Integer recommendedReps;
    private Double recommendedWeight;
}
