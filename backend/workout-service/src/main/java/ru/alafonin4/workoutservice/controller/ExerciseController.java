package ru.alafonin4.workoutservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alafonin4.workoutservice.model.Exercise;
import ru.alafonin4.workoutservice.repository.ExerciseRepository;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = "*")
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @GetMapping({"", "/"})
    public List<Exercise> getAllExercises(@RequestParam(required = false) String muscleGroup) {
        if (muscleGroup != null) {
            return exerciseRepository.findByMuscleGroupIgnoreCase(muscleGroup);
        }
        return exerciseRepository.findAll();
    }

    @GetMapping({"/{id}", "/get/{id}"})
    public Exercise getExerciseById(@PathVariable Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found: " + id));
    }

    @PostMapping({"", "/", "/create"})
    public Exercise createExercise(@RequestBody Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @PutMapping("/{id}")
    public Exercise updateExercise(@PathVariable Long id, @RequestBody Exercise updated) {
        Exercise existing = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found: " + id));

        existing.setName(updated.getName());
        existing.setMuscleGroup(updated.getMuscleGroup());
        existing.setTechniqueUrl(updated.getTechniqueUrl());
        existing.setDescription(updated.getDescription());

        return exerciseRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteExercise(@PathVariable Long id) {
        exerciseRepository.deleteById(id);
    }
}
