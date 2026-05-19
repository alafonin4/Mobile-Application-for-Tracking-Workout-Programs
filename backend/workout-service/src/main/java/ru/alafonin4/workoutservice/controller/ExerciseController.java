package ru.alafonin4.workoutservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;
import ru.alafonin4.workoutservice.dto.ExerciseCatalogItemDto;
import ru.alafonin4.workoutservice.model.Exercise;
import ru.alafonin4.workoutservice.model.ExerciseFavorite;
import ru.alafonin4.workoutservice.repository.ExerciseFavoriteRepository;
import ru.alafonin4.workoutservice.repository.ExerciseRepository;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = "*")
public class ExerciseController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseFavoriteRepository exerciseFavoriteRepository;

    /**
     * Returns the all exercises.
     * @param muscleGroup muscle group
     * @param requiresAdditionalWeight requires additional weight
     * @return prepared list with the requested data
     */
    @GetMapping({"", "/"})
    public List<Exercise> getAllExercises(
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) Boolean requiresAdditionalWeight
    ) {
        return filterExercises(muscleGroup, requiresAdditionalWeight);
    }

    /**
     * Returns the exercise catalog.
     * @param userId identifier of the user
     * @param muscleGroup muscle group
     * @param requiresAdditionalWeight requires additional weight
     * @return prepared list with the requested data
     */
    @GetMapping("/catalog")
    public List<ExerciseCatalogItemDto> getExerciseCatalog(
            @RequestParam("userId") Long userId,
            @RequestParam(required = false) String muscleGroup,
            @RequestParam(required = false) Boolean requiresAdditionalWeight
    ) {
        Set<Long> favoriteIds = new HashSet<>(exerciseFavoriteRepository.findByUserId(userId).stream()
                .map(ExerciseFavorite::getExerciseId)
                .toList());

        return filterExercises(muscleGroup, requiresAdditionalWeight).stream()
                .map(exercise -> toCatalogItem(exercise, favoriteIds.contains(exercise.getId())))
                .sorted(Comparator
                        .comparing(ExerciseCatalogItemDto::getFavorite, Comparator.reverseOrder())
                        .thenComparing(ExerciseCatalogItemDto::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    /**
     * Returns the favorite exercises.
     * @param userId identifier of the user
     * @return prepared list with the requested data
     */
    @GetMapping("/favorites/{userId}")
    public List<ExerciseCatalogItemDto> getFavoriteExercises(@PathVariable Long userId) {
        Set<Long> favoriteIds = new HashSet<>(exerciseFavoriteRepository.findByUserId(userId).stream()
                .map(ExerciseFavorite::getExerciseId)
                .toList());

        return exerciseRepository.findAll().stream()
                .filter(exercise -> favoriteIds.contains(exercise.getId()))
                .map(exercise -> toCatalogItem(exercise, true))
                .sorted(Comparator.comparing(ExerciseCatalogItemDto::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    /**
     * AddFavorite.
     * @param exerciseId identifier of the exercise
     * @param userId identifier of the user
     */
    @PutMapping("/{exerciseId}/favorite/{userId}")
    public void addFavorite(@PathVariable Long exerciseId, @PathVariable Long userId) {
        exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found: " + exerciseId));

        boolean alreadyExists = exerciseFavoriteRepository.findByUserIdAndExerciseId(userId, exerciseId).isPresent();
        if (alreadyExists) {
            return;
        }

        ExerciseFavorite favorite = new ExerciseFavorite();
        favorite.setUserId(userId);
        favorite.setExerciseId(exerciseId);
        exerciseFavoriteRepository.save(favorite);
    }

    /**
     * RemoveFavorite.
     * @param exerciseId identifier of the exercise
     * @param userId identifier of the user
     */
    @DeleteMapping("/{exerciseId}/favorite/{userId}")
    public void removeFavorite(@PathVariable Long exerciseId, @PathVariable Long userId) {
        exerciseFavoriteRepository.deleteByUserIdAndExerciseId(userId, exerciseId);
    }

    /**
     * Returns the exercise by id.
     * @param id identifier of the target record
     * @return result of the operation
     */
    @GetMapping({"/{id}", "/get/{id}"})
    public Exercise getExerciseById(@PathVariable Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found: " + id));
    }

    /**
     * Creates a new exercise.
     * @param exercise exercise being processed
     * @return result of the operation
     */
    @PostMapping({"", "/", "/create"})
    public Exercise createExercise(@RequestBody Exercise exercise) {
        if (exercise.getRequiresAdditionalWeight() == null) {
            exercise.setRequiresAdditionalWeight(Boolean.FALSE);
        }
        return exerciseRepository.save(exercise);
    }

    /**
     * Updates the exercise.
     * @param id identifier of the target record
     * @param updated updated
     * @return result of the operation
     */
    @PutMapping("/{id}")
    public Exercise updateExercise(@PathVariable Long id, @RequestBody Exercise updated) {
        Exercise existing = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found: " + id));

        existing.setName(updated.getName());
        existing.setMuscleGroup(updated.getMuscleGroup());
        existing.setTechniqueUrl(updated.getTechniqueUrl());
        existing.setDescription(updated.getDescription());
        existing.setRequiresAdditionalWeight(Boolean.TRUE.equals(updated.getRequiresAdditionalWeight()));

        return exerciseRepository.save(existing);
    }

    /**
     * Deletes the exercise.
     * @param id identifier of the target record
     */
    @DeleteMapping("/{id}")
    public void deleteExercise(@PathVariable Long id) {
        if (!exerciseRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found: " + id);
        }
        exerciseRepository.deleteById(id);
    }

    /**
     * FilterExercises.
     * @param muscleGroup muscle group
     * @param requiresAdditionalWeight requires additional weight
     * @return prepared list with the requested data
     */
    private List<Exercise> filterExercises(String muscleGroup, Boolean requiresAdditionalWeight) {
        return exerciseRepository.findAll().stream()
                .filter(exercise -> muscleGroup == null
                        || muscleGroup.isBlank()
                        || "all".equalsIgnoreCase(muscleGroup)
                        || (exercise.getMuscleGroup() != null && exercise.getMuscleGroup().equalsIgnoreCase(muscleGroup)))
                .filter(exercise -> requiresAdditionalWeight == null
                        || Boolean.TRUE.equals(exercise.getRequiresAdditionalWeight()) == requiresAdditionalWeight)
                .sorted(Comparator.comparing(Exercise::getName, String.CASE_INSENSITIVE_ORDER))
                .toList();
    }

    /**
     * ToCatalogItem.
     * @param exercise exercise being processed
     * @param favorite favorite
     * @return result of the operation
     */
    private ExerciseCatalogItemDto toCatalogItem(Exercise exercise, boolean favorite) {
        ExerciseCatalogItemDto dto = new ExerciseCatalogItemDto();
        dto.setId(exercise.getId());
        dto.setName(exercise.getName());
        dto.setMuscleGroup(exercise.getMuscleGroup());
        dto.setDescription(exercise.getDescription());
        dto.setTechniqueUrl(exercise.getTechniqueUrl());
        dto.setRequiresAdditionalWeight(Boolean.TRUE.equals(exercise.getRequiresAdditionalWeight()));
        dto.setFavorite(favorite);
        return dto;
    }
}
