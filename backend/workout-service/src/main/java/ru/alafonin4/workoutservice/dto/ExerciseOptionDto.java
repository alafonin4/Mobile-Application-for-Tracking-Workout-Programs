package ru.alafonin4.workoutservice.dto;

public class ExerciseOptionDto {
    private Long exerciseId;
    private String exerciseName;
    private String muscleGroup;

    public ExerciseOptionDto() {
    }

    public ExerciseOptionDto(Long exerciseId, String exerciseName, String muscleGroup) {
        this.exerciseId = exerciseId;
        this.exerciseName = exerciseName;
        this.muscleGroup = muscleGroup;
    }

    public Long getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
}
