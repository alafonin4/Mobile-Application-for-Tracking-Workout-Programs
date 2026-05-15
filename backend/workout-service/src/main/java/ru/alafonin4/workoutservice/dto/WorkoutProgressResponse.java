package ru.alafonin4.workoutservice.dto;

import java.util.ArrayList;
import java.util.List;

public class WorkoutProgressResponse {
    private Long userId;
    private int periodMonths;
    private String fromDate;
    private String toDate;
    private ProgressSummaryDto summary;
    private List<MuscleGroupProgressDto> muscleGroupProgress = new ArrayList<>();
    private List<TimelinePointDto> timeline = new ArrayList<>();
    private List<ExerciseOptionDto> exercises = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPeriodMonths() {
        return periodMonths;
    }

    public void setPeriodMonths(int periodMonths) {
        this.periodMonths = periodMonths;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public ProgressSummaryDto getSummary() {
        return summary;
    }

    public void setSummary(ProgressSummaryDto summary) {
        this.summary = summary;
    }

    public List<MuscleGroupProgressDto> getMuscleGroupProgress() {
        return muscleGroupProgress;
    }

    public void setMuscleGroupProgress(List<MuscleGroupProgressDto> muscleGroupProgress) {
        this.muscleGroupProgress = muscleGroupProgress;
    }

    public List<TimelinePointDto> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<TimelinePointDto> timeline) {
        this.timeline = timeline;
    }

    public List<ExerciseOptionDto> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseOptionDto> exercises) {
        this.exercises = exercises;
    }
}
