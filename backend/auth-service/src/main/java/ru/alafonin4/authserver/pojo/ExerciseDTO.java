package ru.alafonin4.authserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ExerciseDTO {
    private String name;
    private Long sets;
    private Long reps;
}
