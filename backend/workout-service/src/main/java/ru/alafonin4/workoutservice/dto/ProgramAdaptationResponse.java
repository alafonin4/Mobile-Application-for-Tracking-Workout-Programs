package ru.alafonin4.workoutservice.dto;

import java.util.ArrayList;
import java.util.List;

public class ProgramAdaptationResponse {
    private Long userId;
    private Long programId;
    private String programName;
    private String readinessMessage;
    private Integer daysSinceLastWorkout;
    private List<ProgramAdaptationSuggestionDto> suggestions = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getReadinessMessage() {
        return readinessMessage;
    }

    public void setReadinessMessage(String readinessMessage) {
        this.readinessMessage = readinessMessage;
    }

    public Integer getDaysSinceLastWorkout() {
        return daysSinceLastWorkout;
    }

    public void setDaysSinceLastWorkout(Integer daysSinceLastWorkout) {
        this.daysSinceLastWorkout = daysSinceLastWorkout;
    }

    public List<ProgramAdaptationSuggestionDto> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<ProgramAdaptationSuggestionDto> suggestions) {
        this.suggestions = suggestions;
    }
}
