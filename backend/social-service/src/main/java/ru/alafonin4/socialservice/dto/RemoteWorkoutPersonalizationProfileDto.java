package ru.alafonin4.socialservice.dto;

import java.util.ArrayList;
import java.util.List;

public class RemoteWorkoutPersonalizationProfileDto {
    private Long userId;
    private List<RemoteWorkoutAchievementDto> achievements = new ArrayList<>();
    private List<RemoteWorkoutSmartReminderDto> smartReminders = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<RemoteWorkoutAchievementDto> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<RemoteWorkoutAchievementDto> achievements) {
        this.achievements = achievements;
    }

    public List<RemoteWorkoutSmartReminderDto> getSmartReminders() {
        return smartReminders;
    }

    public void setSmartReminders(List<RemoteWorkoutSmartReminderDto> smartReminders) {
        this.smartReminders = smartReminders;
    }
}
