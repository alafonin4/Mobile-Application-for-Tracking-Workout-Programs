package ru.alafonin4.authserver.services;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alafonin4.authserver.entities.Exercise;
import ru.alafonin4.authserver.pojo.ExerciseDTO;
import ru.alafonin4.authserver.repositories.ExerciseRepository;
import ru.alafonin4.authserver.repositories.WorkoutRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;

    public List<ExerciseDTO> getExercisesByWorkoutName(Long id) {
        List<Exercise> exercises = exerciseRepository.findAllByWorkout_Id(id);
        return exercises.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void save(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    public List<Exercise> saveAll(List<Exercise> exercises) {
        return exerciseRepository.saveAll(exercises);
    }
    private ExerciseDTO convertToDTO(Exercise exercise) {
        return new ExerciseDTO(exercise.getName(), exercise.getSets(), exercise.getReps());
    }

    public List<ExerciseDTO> convertToListDTO(List<Exercise> exercises) {
        List<ExerciseDTO> exerciseDTOS = new ArrayList<>();
        for (var i: exercises) {
            exerciseDTOS.add(convertToDTO(i));
        }
        return exerciseDTOS;
    }
}
