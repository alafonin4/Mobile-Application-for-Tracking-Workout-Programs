package ru.alafonin4.workoutservice.dto;

public class AchievementDto {
    private String code;
    private String title;
    private String description;
    private String category;
    private boolean unlocked;
    private double progressPercent;
    private double currentValue;
    private double targetValue;
    private String unit;
    private String awardedAt;

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
     * Returns the description.
     * @return resulting text value
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description.
     * @param description human-readable description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the category.
     * @return resulting text value
     */
    public String getCategory() {
        return category;
    }

    /**
     * Updates the category.
     * @param category achievement category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Indicates whether unlocked.
     * @return true when the condition is satisfied; otherwise false
     */
    public boolean isUnlocked() {
        return unlocked;
    }

    /**
     * Updates the unlocked.
     * @param unlocked new unlocked
     */
    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    /**
     * Returns the progress percent.
     * @return calculated numeric value
     */
    public double getProgressPercent() {
        return progressPercent;
    }

    /**
     * Updates the progress percent.
     * @param progressPercent new progress percent
     */
    public void setProgressPercent(double progressPercent) {
        this.progressPercent = progressPercent;
    }

    /**
     * Returns the current value.
     * @return calculated numeric value
     */
    public double getCurrentValue() {
        return currentValue;
    }

    /**
     * Updates the current value.
     * @param currentValue current metric value
     */
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    /**
     * Returns the target value.
     * @return calculated numeric value
     */
    public double getTargetValue() {
        return targetValue;
    }

    /**
     * Updates the target value.
     * @param targetValue target metric value
     */
    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
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
     * Returns the awarded at.
     * @return resulting text value
     */
    public String getAwardedAt() {
        return awardedAt;
    }

    /**
     * Updates the awarded at.
     * @param awardedAt new awarded at
     */
    public void setAwardedAt(String awardedAt) {
        this.awardedAt = awardedAt;
    }
}
