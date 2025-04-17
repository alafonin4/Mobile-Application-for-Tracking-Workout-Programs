package ru.alafonin4.authserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.alafonin4.authserver.entities.Exercise;
import ru.alafonin4.authserver.entities.Workout;
import ru.alafonin4.authserver.entities.WorkoutProgress;
import ru.alafonin4.authserver.pojo.ExerciseDTO;
import ru.alafonin4.authserver.pojo.WorkoutDTORequest;
import ru.alafonin4.authserver.pojo.WorkoutDTOResponse;
import ru.alafonin4.authserver.repositories.UserRepository;
import ru.alafonin4.authserver.repositories.WorkoutProgressRepository;
import ru.alafonin4.authserver.repositories.WorkoutRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;
    private final ExerciseService exerciseService;
    private final WorkoutProgressRepository workoutProgressRepository;

    public List<WorkoutDTOResponse> getAllWorkoutsByEmail(String email) {
        List<Workout> workouts = workoutRepository.findAllByUserEmailOrderByCreatedAtDesc(email);
        return workouts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public WorkoutDTOResponse getWorkoutByName(String workoutName, String email) {
        var workout = workoutRepository.findByNameAndUserEmail(workoutName, email);
        if (workout.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return WorkoutDTOResponse.builder()
                .name(workout.get().getName())
                .workoutTime(workout.get().getCreatedAt())
                .exercises(exerciseService.convertToListDTO(workout.get().getExercises()))
                .build();
    }

    private WorkoutDTOResponse convertToDTO(Workout workout) {
        List<ExerciseDTO> exercises = exerciseService.getExercisesByWorkoutName(workout.getId());
        System.out.println(exercises);
        return new WorkoutDTOResponse(workout.getName(), workout.getCreatedAt(), exercises);
    }

    public WorkoutDTOResponse createWorkout(String userEmail, WorkoutDTORequest workoutDTO) {
        var user = userRepository.findByEmail(userEmail);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        var workout = Workout.builder()
                .name(workoutDTO.getName())
                .createdAt(localDateTime)
                .user(user.get())
                .build();

        workout = workoutRepository.save(workout); // Сохраняем тренировку и получаем ее сохраненный экземпляр

        List<ExerciseDTO> exerciseDTOS = workoutDTO.getExercises();
        List<Exercise> exercises = new ArrayList<>();
        for (var i: exerciseDTOS) {
            var ex = Exercise.builder()
                    .name(i.getName())
                    .reps(i.getReps())
                    .sets(i.getSets())
                    .workout(workout)
                    .build();
            exercises.add(ex);
        }
        exerciseService.saveAll(exercises);

        WorkoutProgress workoutProgress = WorkoutProgress.builder()
                .email(userEmail)
                .progress(calculateWorkoutProgress(exercises))
                .dateTime(workout.getCreatedAt())
                .workout(workout)
                .build();

        workoutProgressRepository.save(workoutProgress);

        var w = WorkoutDTOResponse.builder()
                .name(workoutDTO.getName())
                .workoutTime(localDateTime)
                .exercises(workoutDTO.getExercises())
                .build();

        return w;
    }

    private double calculateWorkoutProgress(List<Exercise> exercises) {
        double totalProgress = 0;
        for (var ex : exercises) {
            totalProgress += ex.getReps() * ex.getSets();
        }
        return totalProgress;
    }
}
