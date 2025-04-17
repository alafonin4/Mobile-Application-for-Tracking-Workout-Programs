package ru.alafonin4.workoutservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alafonin4.workoutservice.model.TrainingProgram;
import ru.alafonin4.workoutservice.service.TrainingProgramService;

import java.util.List;

@RestController
@RequestMapping("/api/training-programs")
public class TrainingProgramController {

    @Autowired
    private TrainingProgramService trainingProgramService;

    @PostMapping("/")
    public ResponseEntity<TrainingProgram> createProgram(@RequestBody TrainingProgram program) {
        TrainingProgram createdProgram = trainingProgramService.createProgram(program);
        return ResponseEntity.ok(createdProgram);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingProgram> getProgram(@PathVariable("id") Long id) {
        TrainingProgram program = trainingProgramService.getProgramById(id);
        return ResponseEntity.ok(program);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TrainingProgram>> getProgramsByUser(@PathVariable("userId") Long userId) {
        List<TrainingProgram> programs = trainingProgramService.getProgramsByUserId(userId);
        return ResponseEntity.ok(programs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingProgram> updateProgram(@PathVariable("id") Long id, @RequestBody TrainingProgram program) {
        try {
            TrainingProgram updatedProgram = trainingProgramService.updateProgram(id, program);
            return ResponseEntity.ok(updatedProgram);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable("id") Long id) {
        trainingProgramService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}
