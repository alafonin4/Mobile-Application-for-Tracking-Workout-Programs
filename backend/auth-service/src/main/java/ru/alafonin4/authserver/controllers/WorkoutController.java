package ru.alafonin4.authserver.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.alafonin4.authserver.exceptions.NotFoundEmailException;
import ru.alafonin4.authserver.pojo.GetResponse;
import ru.alafonin4.authserver.pojo.WorkoutDTORequest;
import ru.alafonin4.authserver.pojo.WorkoutDTOResponse;
import ru.alafonin4.authserver.services.JwtService;
import ru.alafonin4.authserver.services.WorkoutService;

import java.util.List;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
public class WorkoutController {

    private final WorkoutService workoutService;
    private final JwtService jwtService;

    @CrossOrigin(origins = "http://localhost:3001")
    @PostMapping("/get")
    public ResponseEntity<List<WorkoutDTOResponse>> getAllWorkouts(@Valid @RequestBody GetResponse workoutDTO) {
        String userEmail = jwtService.extractEmail(workoutDTO.getToken());
        List<WorkoutDTOResponse> workouts = workoutService.getAllWorkoutsByEmail(userEmail);
        if (workouts.isEmpty()) {
            throw new NotFoundEmailException("");
        }
        return ResponseEntity.ok(workouts);
    }
    @CrossOrigin(origins = "http://localhost:3001", methods = {RequestMethod.OPTIONS})
    @RequestMapping(value = "/get", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleGetOptionsRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "http://localhost:3001");
        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/get/{name}")
    public ResponseEntity<WorkoutDTOResponse> getWorkoutByName(@Valid @PathVariable String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        WorkoutDTOResponse workout = workoutService.getWorkoutByName(name, userEmail);
        if (workout == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(workout);
    }

    @CrossOrigin(origins = "http://localhost:3001")
    @PostMapping("/create")
    public ResponseEntity<?> createWorkout(@Valid @RequestBody WorkoutDTORequest workoutDTO) {

        String userEmail = jwtService.extractEmail(workoutDTO.getToken());
        WorkoutDTOResponse createdWorkout = workoutService.createWorkout(userEmail, workoutDTO);
        return ResponseEntity.ok(createdWorkout);
    }
    @CrossOrigin(origins = "http://localhost:3001", methods = {RequestMethod.OPTIONS})
    @RequestMapping(value = "/create", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleCreateOptionsRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "http://localhost:3001");
        return ResponseEntity.ok().headers(headers).build();
    }
}