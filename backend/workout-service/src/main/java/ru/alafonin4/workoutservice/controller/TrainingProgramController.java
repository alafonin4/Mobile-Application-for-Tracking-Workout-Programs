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
import org.springframework.web.bind.annotation.RestController;
import ru.alafonin4.workoutservice.model.TrainingProgram;
import ru.alafonin4.workoutservice.service.TrainingProgramService;

import java.util.List;

@RestController
@RequestMapping("/api/training-programs")
public class TrainingProgramController {

    @Autowired
    private TrainingProgramService trainingProgramService;

    /**
     * Creates a new program.
     * @param program training program being processed
     * @return HTTP response containing the requested payload
     */
    @PostMapping({"", "/"})
    public ResponseEntity<TrainingProgram> createProgram(@RequestBody TrainingProgram program) {
        TrainingProgram createdProgram = trainingProgramService.createProgram(program);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProgram);
    }

    /**
     * Returns the program.
     * @param id identifier of the target record
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainingProgram> getProgram(@PathVariable("id") Long id) {
        TrainingProgram program = trainingProgramService.getProgramById(id);
        return ResponseEntity.ok(program);
    }

    /**
     * Returns the programs by user.
     * @param userId identifier of the user
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TrainingProgram>> getProgramsByUser(@PathVariable("userId") Long userId) {
        List<TrainingProgram> programs = trainingProgramService.getProgramsByUserId(userId);
        return ResponseEntity.ok(programs);
    }

    /**
     * Updates the program.
     * @param id identifier of the target record
     * @param program training program being processed
     * @return HTTP response containing the requested payload
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrainingProgram> updateProgram(@PathVariable("id") Long id, @RequestBody TrainingProgram program) {
        TrainingProgram updatedProgram = trainingProgramService.updateProgram(id, program);
        return ResponseEntity.ok(updatedProgram);
    }

    /**
     * Deletes the program.
     * @param id identifier of the target record
     * @return HTTP response containing the requested payload
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable("id") Long id) {
        trainingProgramService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }
}
