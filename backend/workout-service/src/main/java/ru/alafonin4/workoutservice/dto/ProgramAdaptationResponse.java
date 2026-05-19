package ru.alafonin4.workoutservice.dto;

import java.util.ArrayList;
import java.util.List;

public class ProgramAdaptationResponse {
    private Long userId;
    private Long programId;
    private String programName;
    private String readinessMessage;
    private Integer daysSinceLastWorkout;
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<ProgramAdaptationSuggestionDto> suggestions = new ArrayList<>();

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
     * Returns the identifier of the program.
     * @return result of the operation
     */
    public Long getProgramId() {
        return programId;
    }

    /**
     * Updates the identifier of the program.
     * @param programId identifier of the training program
     */
    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    /**
     * Returns the program name.
     * @return resulting text value
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * Updates the program name.
     * @param programName new program name
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * Returns the readiness message.
     * @return resulting text value
     */
    public String getReadinessMessage() {
        return readinessMessage;
    }

    /**
     * Updates the readiness message.
     * @param readinessMessage new readiness message
     */
    public void setReadinessMessage(String readinessMessage) {
        this.readinessMessage = readinessMessage;
    }

    /**
     * Returns the days since last workout.
     * @return calculated numeric value
     */
    public Integer getDaysSinceLastWorkout() {
        return daysSinceLastWorkout;
    }

    /**
     * Updates the days since last workout.
     * @param daysSinceLastWorkout days elapsed since the last workout
     */
    public void setDaysSinceLastWorkout(Integer daysSinceLastWorkout) {
        this.daysSinceLastWorkout = daysSinceLastWorkout;
    }

    /**
     * Returns the suggestions.
     * @return prepared list with the requested data
     */
    public List<ProgramAdaptationSuggestionDto> getSuggestions() {
        return suggestions;
    }

    /**
     * Updates the suggestions.
     * @param suggestions program adaptation suggestions
     */
    public void setSuggestions(List<ProgramAdaptationSuggestionDto> suggestions) {
        this.suggestions = suggestions;
    }
}
