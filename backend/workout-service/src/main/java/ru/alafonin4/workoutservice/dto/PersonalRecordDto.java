package ru.alafonin4.workoutservice.dto;

public class PersonalRecordDto {
    private String code;
    private String title;
    private String exerciseName;
    private double value;
    private String unit;
    private String date;
    private String subtitle;

    /**
     * Returns the code.
     * @return resulting text value
     */
    public String getCode() {
        return code;
    }

    /**
     * Updates the code.
     * @param code stable machine-readable code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Returns the title.
     * @return resulting text value
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates the title.
     * @param title human-readable title
     */
    public void setTitle(String title) {
        this.title = title;
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
     * Returns the value.
     * @return calculated numeric value
     */
    public double getValue() {
        return value;
    }

    /**
     * Updates the value.
     * @param value value being processed
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * Returns the unit.
     * @return resulting text value
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Updates the unit.
     * @param unit display unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Returns the date.
     * @return resulting text value
     */
    public String getDate() {
        return date;
    }

    /**
     * Updates the date.
     * @param date date being processed
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns the subtitle.
     * @return resulting text value
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Updates the subtitle.
     * @param subtitle new subtitle
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
