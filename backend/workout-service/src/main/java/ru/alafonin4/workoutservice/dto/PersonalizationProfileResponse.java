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
    private List<AchievementDto> achievements = new ArrayList<>();
    private List<PersonalRecordDto> personalRecords = new ArrayList<>();
    private List<MuscleBalanceDto> muscleBalance = new ArrayList<>();
    private List<SmartReminderDto> smartReminders = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProfileMessage() {
        return profileMessage;
    }

    public void setProfileMessage(String profileMessage) {
        this.profileMessage = profileMessage;
    }

    public Integer getRecoveryScore() {
        return recoveryScore;
    }

    public void setRecoveryScore(Integer recoveryScore) {
        this.recoveryScore = recoveryScore;
    }

    public String getRecoveryStatus() {
        return recoveryStatus;
    }

    public void setRecoveryStatus(String recoveryStatus) {
        this.recoveryStatus = recoveryStatus;
    }

    public int getUnlockedAchievementsCount() {
        return unlockedAchievementsCount;
    }

    public void setUnlockedAchievementsCount(int unlockedAchievementsCount) {
        this.unlockedAchievementsCount = unlockedAchievementsCount;
    }

    public int getTotalAchievementsCount() {
        return totalAchievementsCount;
    }

    public void setTotalAchievementsCount(int totalAchievementsCount) {
        this.totalAchievementsCount = totalAchievementsCount;
    }

    public List<AchievementDto> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<AchievementDto> achievements) {
        this.achievements = achievements;
    }

    public List<PersonalRecordDto> getPersonalRecords() {
        return personalRecords;
    }

    public void setPersonalRecords(List<PersonalRecordDto> personalRecords) {
        this.personalRecords = personalRecords;
    }

    public List<MuscleBalanceDto> getMuscleBalance() {
        return muscleBalance;
    }

    public void setMuscleBalance(List<MuscleBalanceDto> muscleBalance) {
        this.muscleBalance = muscleBalance;
    }

    public List<SmartReminderDto> getSmartReminders() {
        return smartReminders;
    }

    public void setSmartReminders(List<SmartReminderDto> smartReminders) {
        this.smartReminders = smartReminders;
    }
}
