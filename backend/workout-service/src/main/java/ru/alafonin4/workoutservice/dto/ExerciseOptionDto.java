package ru.alafonin4.workoutservice.dto;

public class ExerciseOptionDto {
    private Long exerciseId;
    private String exerciseName;
    private String muscleGroup;

    /**
     * Creates a new ExerciseOptionDto instance.
     */
    public ExerciseOptionDto() {
    }

    /**
     * Creates a new ExerciseOptionDto instance.
     * @param exerciseId identifier of the exercise
     * @param exerciseName exercise name
     * @param muscleGroup muscle group
     */
    public ExerciseOptionDto(Long exerciseId, String exerciseName, String muscleGroup) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.muscleGroup = muscleGroup;
    }

    /**
     * Returns the identifier of the exercise.
     * @return result of the operation
     */
    public Long getExerciseId() {
        return exerciseId;
    }

    /**
     * Updates the identifier of the exercise.
     * @param exerciseId identifier of the exercise
     */
    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    /**
     * Returns the exercise name.
     * @return resulting text value
     */
    public String getExerciseName() {
        return exerciseName;
    }

    /**
     * Updates the exercise name.
     * @param exerciseName new exercise name
     */
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    /**
     * Returns the muscle group.
     * @return resulting text value
     */
    public String getMuscleGroup() {
        return muscleGroup;
    }

    /**
     * Updates the muscle group.
     * @param muscleGroup new muscle group
     */
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
}
