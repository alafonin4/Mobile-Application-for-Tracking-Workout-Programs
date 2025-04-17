package ru.alafonin4.authserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_exercise")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "sets", nullable = false)
    private Long sets;

    @Column(name = "reps", nullable = false)
    private Long reps;

    @ManyToOne
    @JoinColumn(name = "workout_id", referencedColumnName = "id")
    private Workout workout;
}
