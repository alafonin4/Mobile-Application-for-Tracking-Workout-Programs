package ru.alafonin4.authserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkoutProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "workoutId", referencedColumnName = "id")
    private Workout workout;
    @Column(name = "user_email", nullable = false, updatable = false)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime dateTime;

    @Column(name = "progress", nullable = false, updatable = false)
    private double progress;
}
