package ru.alafonin4.socialservice.dto;

import java.util.ArrayList;
import java.util.List;

public class RemoteWorkoutPersonalizationProfileDto {
    private Long userId;
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<RemoteWorkoutAchievementDto> achievements = new ArrayList<>();
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<RemoteWorkoutSmartReminderDto> smartReminders = new ArrayList<>();

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
     * Returns the achievements.
     * @return prepared list with the requested data
     */
    public List<RemoteWorkoutAchievementDto> getAchievements() {
        return achievements;
    }

    /**
     * Updates the achievements.
     * @param achievements new achievements
     */
    public void setAchievements(List<RemoteWorkoutAchievementDto> achievements) {
        this.achievements = achievements;
    }

    /**
     * Returns the smart reminders.
     * @return prepared list with the requested data
     */
    public List<RemoteWorkoutSmartReminderDto> getSmartReminders() {
        return smartReminders;
    }

    /**
     * Updates the smart reminders.
     * @param smartReminders new smart reminders
     */
    public void setSmartReminders(List<RemoteWorkoutSmartReminderDto> smartReminders) {
        this.smartReminders = smartReminders;
    }
}
