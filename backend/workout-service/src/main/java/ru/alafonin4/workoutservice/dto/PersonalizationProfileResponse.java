package ru.alafonin4.workoutservice.dto;

import java.util.ArrayList;
import java.util.List;

public class PersonalizationProfileResponse {
    private Long userId;
    private String profileMessage;
    private Integer recoveryScore;
    private String recoveryStatus;
    private int unlockedAchievementsCount;
    private int totalAchievementsCount;
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<AchievementDto> achievements = new ArrayList<>();
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<PersonalRecordDto> personalRecords = new ArrayList<>();
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<MuscleBalanceDto> muscleBalance = new ArrayList<>();
    /**
     * ArrayList<>.
     * @return result of the operation
     */
    private List<SmartReminderDto> smartReminders = new ArrayList<>();

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
     * Returns the profile message.
     * @return resulting text value
     */
    public String getProfileMessage() {
        return profileMessage;
    }

    /**
     * Updates the profile message.
     * @param profileMessage new profile message
     */
    public void setProfileMessage(String profileMessage) {
        this.profileMessage = profileMessage;
    }

    /**
     * Returns the recovery score.
     * @return calculated numeric value
     */
    public Integer getRecoveryScore() {
        return recoveryScore;
    }

    /**
     * Updates the recovery score.
     * @param recoveryScore calculated recovery score
     */
    public void setRecoveryScore(Integer recoveryScore) {
        this.recoveryScore = recoveryScore;
    }

    /**
     * Returns the recovery status.
     * @return resulting text value
     */
    public String getRecoveryStatus() {
        return recoveryStatus;
    }

    /**
     * Updates the recovery status.
     * @param recoveryStatus new recovery status
     */
    public void setRecoveryStatus(String recoveryStatus) {
        this.recoveryStatus = recoveryStatus;
    }

    /**
     * Returns the unlocked achievements count.
     * @return calculated numeric value
     */
    public int getUnlockedAchievementsCount() {
        return unlockedAchievementsCount;
    }

    /**
     * Updates the unlocked achievements count.
     * @param unlockedAchievementsCount new unlocked achievements count
     */
    public void setUnlockedAchievementsCount(int unlockedAchievementsCount) {
        this.unlockedAchievementsCount = unlockedAchievementsCount;
    }

    /**
     * Returns the total achievements count.
     * @return calculated numeric value
     */
    public int getTotalAchievementsCount() {
        return totalAchievementsCount;
    }

    /**
     * Updates the total achievements count.
     * @param totalAchievementsCount new total achievements count
     */
    public void setTotalAchievementsCount(int totalAchievementsCount) {
        this.totalAchievementsCount = totalAchievementsCount;
    }

    /**
     * Returns the achievements.
     * @return prepared list with the requested data
     */
    public List<AchievementDto> getAchievements() {
        return achievements;
    }

    /**
     * Updates the achievements.
     * @param achievements new achievements
     */
    public void setAchievements(List<AchievementDto> achievements) {
        this.achievements = achievements;
    }

    /**
     * Returns the personal records.
     * @return prepared list with the requested data
     */
    public List<PersonalRecordDto> getPersonalRecords() {
        return personalRecords;
    }

    /**
     * Updates the personal records.
     * @param personalRecords new personal records
     */
    public void setPersonalRecords(List<PersonalRecordDto> personalRecords) {
        this.personalRecords = personalRecords;
    }

    /**
     * Returns the muscle balance.
     * @return prepared list with the requested data
     */
    public List<MuscleBalanceDto> getMuscleBalance() {
        return muscleBalance;
    }

    /**
     * Updates the muscle balance.
     * @param muscleBalance muscle-balance snapshot used for personalization
     */
    public void setMuscleBalance(List<MuscleBalanceDto> muscleBalance) {
        this.muscleBalance = muscleBalance;
    }

    /**
     * Returns the smart reminders.
     * @return prepared list with the requested data
     */
    public List<SmartReminderDto> getSmartReminders() {
        return smartReminders;
    }

    /**
     * Updates the smart reminders.
     * @param smartReminders new smart reminders
     */
    public void setSmartReminders(List<SmartReminderDto> smartReminders) {
        this.smartReminders = smartReminders;
    }
}
