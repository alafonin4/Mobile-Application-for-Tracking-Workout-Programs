package ru.alafonin4.workoutservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alafonin4.workoutservice.model.ExerciseSet;
import ru.alafonin4.workoutservice.model.Workout;
import ru.alafonin4.workoutservice.model.WorkoutExercise;
import ru.alafonin4.workoutservice.repository.ExerciseRepository;
import ru.alafonin4.workoutservice.repository.WorkoutRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    public Workout createWorkout(Workout workout) {
        normalizeWorkout(workout);
        return workoutRepository.save(workout);
    }

    @Transactional(readOnly = true)
    public Workout getWorkoutById(Long id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout not found with id " + id));
        initializeWorkout(workout);
        return workout;
    }

    @Transactional(readOnly = true)
    public List<Workout> getWorkoutsByUserId(Long userId) {
        List<Workout> workouts = workoutRepository.findByUserIdOrderByWorkoutDateAsc(userId);
        workouts.forEach(this::initializeWorkout);
        return workouts;
    }

    public Workout updateWorkout(Long id, Workout updatedWorkout) {
        return workoutRepository.findById(id).map(existingWorkout -> {
            existingWorkout.setUserId(updatedWorkout.getUserId());
            existingWorkout.setName(updatedWorkout.getName());
            existingWorkout.setWorkoutDate(updatedWorkout.getWorkoutDate());
            existingWorkout.getWorkoutExercises().clear();
            if (updatedWorkout.getWorkoutExercises() != null) {
                existingWorkout.getWorkoutExercises().addAll(updatedWorkout.getWorkoutExercises());
            }
            normalizeWorkout(existingWorkout);
            return workoutRepository.save(existingWorkout);
        }).orElseThrow(() -> new RuntimeException("Workout not found with id " + id));
    }

    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }


    private void normalizeWorkout(Workout workout) {

        if (workout.getName() == null || workout.getName().isBlank()) {
            workout.setName("Workout");
        }

        if (workout.getWorkoutDate() == null) {
            workout.setWorkoutDate(LocalDateTime.now());
        }

        if (workout.getWorkoutExercises() == null) {
            workout.setWorkoutExercises(new ArrayList<>());
            return;
        }

        for (WorkoutExercise workoutExercise : workout.getWorkoutExercises()) {

            workoutExercise.setWorkout(workout);

            if (workoutExercise.getExercise() == null ||
                    workoutExercise.getExercise().getId() == null) {
                throw new RuntimeException("Exercise id must be provided");
            }

            workoutExercise.setExercise(
                    exerciseRepository.findById(workoutExercise.getExercise().getId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Exercise not found with id " + workoutExercise.getExercise().getId()
                            ))
            );

            if (workoutExercise.getSets() == null) {
                workoutExercise.setSets(new ArrayList<>());
                continue;
            }

            for (int i = 0; i < workoutExercise.getSets().size(); i++) {
                ExerciseSet exerciseSet = workoutExercise.getSets().get(i);

                exerciseSet.setWorkoutExercise(workoutExercise);

                if (exerciseSet.getSetNumber() == null) {
                    exerciseSet.setSetNumber(i + 1);
                }

                if (exerciseSet.getReps() == null) {
                    exerciseSet.setReps(0);
                }

                if (exerciseSet.getWeight() == null) {
                    exerciseSet.setWeight(0.0);
                }
            }
        }
    }

    private void initializeWorkout(Workout workout) {
        workout.getWorkoutExercises().size();
        for (WorkoutExercise workoutExercise : workout.getWorkoutExercises()) {
            if (workoutExercise.getExercise() != null) {
                workoutExercise.getExercise().getName();
                workoutExercise.getExercise().getMuscleGroup();
            }
            workoutExercise.getSets().size();
        }
    }
}
