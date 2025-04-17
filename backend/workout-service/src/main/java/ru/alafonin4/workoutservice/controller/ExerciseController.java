package ru.alafonin4.workoutservice.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.alafonin4.workoutservice.model.Exercise;
import ru.alafonin4.workoutservice.repository.ExerciseRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = "*") // Разрешаем CORS, если фронт отделён
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    // Получить все упражнения
    @GetMapping("/")
    public List<Exercise> getAllExercises(@RequestParam(required = false) String muscleGroup) {
        if (muscleGroup != null) {
            return exerciseRepository.findByMuscleGroupIgnoreCase(muscleGroup);
        }
        return exerciseRepository.findAll();
    }

    // Получить одно упражнение по ID
    @GetMapping("/get/{id}")
    public Exercise getExerciseById(@PathVariable Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Упражнение не найдено: " + id));
    }

    // Создать новое упражнение
    @PostMapping("/create")
    public Exercise createExercise(@RequestBody Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @PutMapping("/{id}")
    public Exercise updateExercise(@PathVariable Long id, @RequestBody Exercise updated) {
        Exercise existing = exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Упражнение не найдено: " + id));

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

