package ru.alafonin4.socialservice.dto;

public class RemoteWorkoutAchievementDto {
    private String code;
    private String title;
    private String description;
    private boolean unlocked;
    private String awardedAt;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public String getAwardedAt() {
        return awardedAt;
    }

    public void setAwardedAt(String awardedAt) {
        this.awardedAt = awardedAt;
    }
}
