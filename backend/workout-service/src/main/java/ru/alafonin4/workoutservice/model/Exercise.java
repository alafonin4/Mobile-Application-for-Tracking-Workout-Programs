package ru.alafonin4.workoutservice.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Группа мышц, к которой относится упражнение
    private String muscleGroup;

    // Ссылка на ресурс с техникой выпо лнения
    private String techniqueUrl;

    @Column(length = 1000)
    private String description;
}
