package ru.alafonin4.workoutservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alafonin4.workoutservice.model.Workout;
import ru.alafonin4.workoutservice.service.WorkoutService;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @PostMapping("/")
    public ResponseEntity<Workout> createWorkout(@RequestBody Workout workout) {
        Workout createdWorkout = workoutService.createWorkout(workout);
        return ResponseEntity.ok(createdWorkout);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workout> getWorkout(@PathVariable("id") Long id) {
        Workout workout = workoutService.getWorkoutById(id);
        return ResponseEntity.ok(workout);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Workout>> getWorkoutsByUser(@PathVariable("userId") Long userId) {
        List<Workout> workouts = workoutService.getWorkoutsByUserId(userId);
        return ResponseEntity.ok(workouts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workout> updateWorkout(@PathVariable("id") Long id, @RequestBody Workout workout) {
        try {
            Workout updatedWorkout = workoutService.updateWorkout(id, workout);
            return ResponseEntity.ok(updatedWorkout);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable("id") Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }
}