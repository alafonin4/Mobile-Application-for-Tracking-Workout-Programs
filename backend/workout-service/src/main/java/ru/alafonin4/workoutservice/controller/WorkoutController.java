package ru.alafonin4.workoutservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alafonin4.workoutservice.dto.ExerciseProgressResponse;
import ru.alafonin4.workoutservice.dto.WorkoutProgressResponse;
import ru.alafonin4.workoutservice.model.Workout;
import ru.alafonin4.workoutservice.service.WorkoutProgressService;
import ru.alafonin4.workoutservice.service.WorkoutService;

import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private WorkoutProgressService workoutProgressService;

    /**
     * Creates a new workout.
     * @param workout workout being processed
     * @return HTTP response containing the requested payload
     */
    @PostMapping({"", "/"})
    public ResponseEntity<Workout> createWorkout(@RequestBody Workout workout) {
        Workout createdWorkout = workoutService.createWorkout(workout);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWorkout);
    }

    /**
     * Returns the workout.
     * @param id identifier of the target record
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/{id}")
    public ResponseEntity<Workout> getWorkout(@PathVariable("id") Long id) {
        Workout workout = workoutService.getWorkoutById(id);
        return ResponseEntity.ok(workout);
    }

    /**
     * Returns the workouts by user.
     * @param userId identifier of the user
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Workout>> getWorkoutsByUser(@PathVariable("userId") Long userId) {
        List<Workout> workouts = workoutService.getWorkoutsByUserId(userId);
        return ResponseEntity.ok(workouts);
    }

    /**
     * Updates the workout.
     * @param id identifier of the target record
     * @param workout workout being processed
     * @return HTTP response containing the requested payload
     */
    @PutMapping("/{id}")
    public ResponseEntity<Workout> updateWorkout(@PathVariable("id") Long id, @RequestBody Workout workout) {
        Workout updatedWorkout = workoutService.updateWorkout(id, workout);
        return ResponseEntity.ok(updatedWorkout);
    }

    /**
     * Deletes the workout.
     * @param id identifier of the target record
     * @return HTTP response containing the requested payload
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable("id") Long id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Returns the user progress.
     * @param userId identifier of the user
     * @param months amount of months included in the analysis
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/progress/user/{userId}")
    public ResponseEntity<WorkoutProgressResponse> getUserProgress(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") int months
    ) {
        return ResponseEntity.ok(workoutProgressService.getUserProgress(userId, months));
    }

    /**
     * Returns the user progress by range.
     * @param userId identifier of the user
     * @param fromDate start date of the requested period
     * @param toDate end date of the requested period
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/progress/user/{userId}/range")
    public ResponseEntity<WorkoutProgressResponse> getUserProgressByRange(
            @PathVariable("userId") Long userId,
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate
    ) {
        return ResponseEntity.ok(workoutProgressService.getUserProgressByRange(userId, fromDate, toDate));
    }

    /**
     * Returns the exercise progress.
     * @param userId identifier of the user
     * @param exerciseId identifier of the exercise
     * @param months amount of months included in the analysis
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/progress/user/{userId}/exercise/{exerciseId}")
    public ResponseEntity<ExerciseProgressResponse> getExerciseProgress(
            @PathVariable("userId") Long userId,
            @PathVariable("exerciseId") Long exerciseId,
            @RequestParam(defaultValue = "1") int months
    ) {
        return ResponseEntity.ok(workoutProgressService.getExerciseProgress(userId, exerciseId, months));
    }
}
