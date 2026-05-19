package ru.alafonin4.workoutservice.dto;

public class ProgressComponentDto {
    private String code;
    private String title;
    private double weight;
    private double score;
    private double baselineValue;
    private double currentValue;
    private double trendPercent;
    private String description;

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
     * Returns the weight.
     * @return calculated numeric value
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Updates the weight.
     * @param weight new weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Returns the score.
     * @return calculated numeric value
     */
    public double getScore() {
        return score;
    }

    /**
     * Updates the score.
     * @param score new score
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Returns the baseline value.
     * @return calculated numeric value
     */
    public double getBaselineValue() {
        return baselineValue;
    }

    /**
     * Updates the baseline value.
     * @param baselineValue new baseline value
     */
    public void setBaselineValue(double baselineValue) {
        this.baselineValue = baselineValue;
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
     * Returns the trend percent.
     * @return calculated numeric value
     */
    public double getTrendPercent() {
        return trendPercent;
    }

    /**
     * Updates the trend percent.
     * @param trendPercent new trend percent
     */
    public void setTrendPercent(double trendPercent) {
        this.trendPercent = trendPercent;
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
}
