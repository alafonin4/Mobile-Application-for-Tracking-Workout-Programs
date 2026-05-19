package ru.alafonin4.workoutservice.dto;

import java.util.ArrayList;
import java.util.List;

public class WorkoutProgressResponse {
    private Long userId;
    private int periodMonths;
    private String fromDate;
    private String toDate;
    private ProgressSummaryDto summary;
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<MuscleGroupProgressDto> muscleGroupProgress = new ArrayList<>();
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<TimelinePointDto> timeline = new ArrayList<>();
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<ExerciseOptionDto> exercises = new ArrayList<>();

    /**
     * Returns the identifier of the user.
     * @return result of the operation
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Updates the identifier of the user.
     * @param userId identifier of the user
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the period months.
     * @return calculated numeric value
     */
    public int getPeriodMonths() {
        return periodMonths;
    }

    /**
     * Updates the period months.
     * @param periodMonths new period months
     */
    public void setPeriodMonths(int periodMonths) {
        this.periodMonths = periodMonths;
    }

    /**
     * Returns the from date.
     * @return resulting text value
     */
    public String getFromDate() {
        return fromDate;
    }

    /**
     * Updates the from date.
     * @param fromDate start date of the requested period
     */
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * Returns the to date.
     * @return resulting text value
     */
    public String getToDate() {
        return toDate;
    }

    /**
     * Updates the to date.
     * @param toDate end date of the requested period
     */
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    /**
     * Returns the summary.
     * @return result of the operation
     */
    public ProgressSummaryDto getSummary() {
        return summary;
    }

    /**
     * Updates the summary.
     * @param summary new summary
     */
    public void setSummary(ProgressSummaryDto summary) {
        this.summary = summary;
    }

    /**
     * Returns the muscle group progress.
     * @return prepared list with the requested data
     */
    public List<MuscleGroupProgressDto> getMuscleGroupProgress() {
        return muscleGroupProgress;
    }

    /**
     * Updates the muscle group progress.
     * @param muscleGroupProgress new muscle group progress
     */
    public void setMuscleGroupProgress(List<MuscleGroupProgressDto> muscleGroupProgress) {
        this.muscleGroupProgress = muscleGroupProgress;
    }

    /**
     * Returns the timeline.
     * @return prepared list with the requested data
     */
    public List<TimelinePointDto> getTimeline() {
        return timeline;
    }

    /**
     * Updates the timeline.
     * @param timeline new timeline
     */
    public void setTimeline(List<TimelinePointDto> timeline) {
        this.timeline = timeline;
    }

    /**
     * Returns the exercises.
     * @return prepared list with the requested data
     */
    public List<ExerciseOptionDto> getExercises() {
        return exercises;
    }

    /**
     * Updates the exercises.
     * @param exercises new exercises
     */
    public void setExercises(List<ExerciseOptionDto> exercises) {
        this.exercises = exercises;
    }
}
