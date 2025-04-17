package ru.alafonin4.workoutservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alafonin4.workoutservice.model.Workout;
import ru.alafonin4.workoutservice.repository.WorkoutRepository;

import java.util.List;

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    public Workout createWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }

    public Workout getWorkoutById(Long id) {
        return workoutRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workout not found with id " + id));
    }

    public List<Workout> getWorkoutsByUserId(Long userId) {
        return workoutRepository.findByUserId(userId);
    }

    public Workout updateWorkout(Long id, Workout updatedWorkout) {
        return workoutRepository.findById(id).map(workout -> {
            workout.setWorkoutDate(updatedWorkout.getWorkoutDate());
            workout.setWorkoutExercises(updatedWorkout.getWorkoutExercises());
            return workoutRepository.save(workout);
        }).orElseThrow(() -> new RuntimeException("Workout not found with id " + id));
    }

    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }
}
