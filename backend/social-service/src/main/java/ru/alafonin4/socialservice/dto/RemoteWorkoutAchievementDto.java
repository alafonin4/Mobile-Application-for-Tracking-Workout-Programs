package ru.alafonin4.socialservice.dto;

public class RemoteWorkoutAchievementDto {
    private String code;
    private String title;
    private String description;
    private boolean unlocked;
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
