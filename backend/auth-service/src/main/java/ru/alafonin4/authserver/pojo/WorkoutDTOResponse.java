package ru.alafonin4.authserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class WorkoutDTOResponse {
    private String name;
    private LocalDateTime workoutTime;
    private List<ExerciseDTO> exercises;
}
