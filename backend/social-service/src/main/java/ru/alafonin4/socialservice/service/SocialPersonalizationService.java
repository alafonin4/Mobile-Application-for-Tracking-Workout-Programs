package ru.alafonin4.socialservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alafonin4.socialservice.dto.CompetitionLeaderboardResponse;
import ru.alafonin4.socialservice.dto.SocialAchievementDto;
import ru.alafonin4.socialservice.dto.SocialPersonalizationResponse;
import ru.alafonin4.socialservice.entities.Competition;
import ru.alafonin4.socialservice.entities.CompetitionParticipant;
import ru.alafonin4.socialservice.enums.CompetitionParticipantStatus;
import ru.alafonin4.socialservice.repositories.CompetitionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class SocialPersonalizationService {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private CompetitionService competitionService;

    /**
     * Builds the profile.
     * @param userId identifier of the user
     * @return result of the operation
     */
    public SocialPersonalizationResponse buildProfile(Long userId) {
        List<Competition> competitions = competitionRepository.findDetailedByUserId(userId);
        List<CompletedCompetitionRecord> completedCompetitions = new ArrayList<>();

        for (Competition competition : competitions) {
            CompetitionParticipant participant = competition.getParticipants().stream()
                    .filter(item -> Objects.equals(item.getUserId(), userId))
                    .findFirst()
                    .orElse(null);
            if (participant == null || participant.getStatus() != CompetitionParticipantStatus.ACCEPTED) {
                continue;
            }

            CompetitionService.CompetitionMetricSnapshot snapshot = competitionService.evaluateCompetition(competition, userId);
            if (snapshot.targetReached()) {
                if (participant.getCompletedAt() == null) {
                    participant.setCompletedAt(LocalDateTime.now());
                }
                completedCompetitions.add(new CompletedCompetitionRecord(
                        competition.getId(),
                        participant.getCompletedAt()
                ));
            }
        }

        completedCompetitions.sort(Comparator.comparing(CompletedCompetitionRecord::completedAt));

        CompetitionLeaderboardResponse globalLeaderboard = competitionService.getGlobalLeaderboard(userId, 1);
        int totalParticipants = globalLeaderboard.getEntries() == null ? 0 : globalLeaderboard.getEntries().size();
        int rank = globalLeaderboard.getCurrentUserRank() == null ? totalParticipants : globalLeaderboard.getCurrentUserRank();
        double percentile = totalParticipants == 0 ? 100 : ((double) rank / totalParticipants) * 100.0;
        double outrunPercent = Math.max(0, 100 - percentile);

        SocialPersonalizationResponse response = new SocialPersonalizationResponse();
        response.setUserId(userId);
        response.setCompletedCompetitionsCount(completedCompetitions.size());
        response.setMonthlyGlobalPercentile(round(percentile));

        List<SocialAchievementDto> achievements = new ArrayList<>();
        achievements.add(toAchievement(
                "COMPETITION_FINISHER_3",
                "Финишер соревнований",
                "Выполните 3 пользовательских соревнования до достижения цели.",
                "Соревнования",
                completedCompetitions.size(),
                3,
                "сорев.",
                completedCompetitions.size() >= 3
                        ? completedCompetitions.get(2).completedAt().toLocalDate()
                        : null
        ));
        achievements.add(toAchievement(
                "GLOBAL_TOP_25",
                "Топ-25% месяца",
                "Попадите в верхние 25% пользователей в общем ежемесячном соревновании.",
                "Рейтинг",
                outrunPercent,
                75,
                "%",
                outrunPercent >= 75 ? YearMonth.now().atDay(1) : null
        ));

        response.setAchievements(achievements);
        return response;
    }

    /**
     * Builds an achievement DTO from the supplied values.
     * @param code stable machine-readable code
     * @param title human-readable title
     * @param description human-readable description
     * @param category achievement category
     * @param currentValue current metric value
     * @param targetValue target metric value
     * @param unit display unit
     * @param awardedAt awarded at
     * @return result of the operation
     */
    private SocialAchievementDto toAchievement(
            String code,
            String title,
            String description,
            String category,
            double currentValue,
            double targetValue,
            String unit,
            LocalDate awardedAt
    ) {
        SocialAchievementDto dto = new SocialAchievementDto();
        dto.setCode(code);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setCategory(category);
        dto.setCurrentValue(round(currentValue));
        dto.setTargetValue(round(targetValue));
        dto.setUnit(unit);
        dto.setUnlocked(currentValue >= targetValue);
        dto.setProgressPercent(round(targetValue <= 0 ? 0 : Math.min(100, (currentValue / targetValue) * 100)));
        if (dto.isUnlocked() && awardedAt != null) {
            dto.setAwardedAt(awardedAt.toString());
        }
        return dto;
    }

    /**
     * Rounds the supplied numeric value to two decimal places.
     * @param value value being processed
     * @return calculated numeric value
     */
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private record CompletedCompetitionRecord(Long competitionId, LocalDateTime completedAt) {
    }
}
