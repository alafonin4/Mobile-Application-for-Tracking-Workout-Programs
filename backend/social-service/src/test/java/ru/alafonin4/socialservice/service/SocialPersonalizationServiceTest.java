package ru.alafonin4.socialservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alafonin4.socialservice.dto.SocialPersonalizationResponse;
import ru.alafonin4.socialservice.entities.Competition;
import ru.alafonin4.socialservice.entities.CompetitionParticipant;
import ru.alafonin4.socialservice.enums.CompetitionGoalType;
import ru.alafonin4.socialservice.enums.CompetitionParticipantStatus;
import ru.alafonin4.socialservice.repositories.CompetitionRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocialPersonalizationServiceTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private CompetitionService competitionService;

    @InjectMocks
    private SocialPersonalizationService socialPersonalizationService;

    @Test
    void buildProfileCountsCompletedCompetitionsAndUnlocksAchievements() {
        Competition first = completedCompetition(101L, 1L);
        Competition second = completedCompetition(102L, 1L);
        Competition third = completedCompetition(103L, 1L);

        when(competitionRepository.findDetailedByUserId(1L)).thenReturn(List.of(first, second, third));
        when(competitionService.evaluateCompetition(first, 1L)).thenReturn(snapshot(true));
        when(competitionService.evaluateCompetition(second, 1L)).thenReturn(snapshot(true));
        when(competitionService.evaluateCompetition(third, 1L)).thenReturn(snapshot(true));
        when(competitionService.getGlobalLeaderboardStats(1L)).thenReturn(new CompetitionService.LeaderboardStats(4, 1));

        SocialPersonalizationResponse response = socialPersonalizationService.buildProfile(1L);

        assertEquals(3, response.getCompletedCompetitionsCount());
        assertEquals(25.0, response.getMonthlyGlobalPercentile());
        assertTrue(response.getAchievements().stream().anyMatch(item -> item.getCode().equals("COMPETITION_FINISHER_3") && item.isUnlocked()));
        assertTrue(response.getAchievements().stream().anyMatch(item -> item.getCode().equals("GLOBAL_TOP_25") && item.isUnlocked()));
    }

    @Test
    void buildProfileKeepsAchievementsLockedWhenThresholdsAreNotMet() {
        Competition competition = completedCompetition(201L, 2L);

        when(competitionRepository.findDetailedByUserId(2L)).thenReturn(List.of(competition));
        when(competitionService.evaluateCompetition(competition, 2L)).thenReturn(snapshot(false));
        when(competitionService.getGlobalLeaderboardStats(2L)).thenReturn(new CompetitionService.LeaderboardStats(10, 9));

        SocialPersonalizationResponse response = socialPersonalizationService.buildProfile(2L);

        assertEquals(0, response.getCompletedCompetitionsCount());
        assertTrue(response.getAchievements().stream().noneMatch(item -> item.isUnlocked()));
    }

    private Competition completedCompetition(Long competitionId, Long userId) {
        Competition competition = new Competition();
        competition.setId(competitionId);
        competition.setGoalType(CompetitionGoalType.WORKOUT_COUNT);
        CompetitionParticipant participant = new CompetitionParticipant();
        participant.setCompetition(competition);
        participant.setUserId(userId);
        participant.setStatus(CompetitionParticipantStatus.ACCEPTED);
        participant.setCompletedAt(LocalDateTime.now().minusDays(1));
        competition.setParticipants(List.of(participant));
        return competition;
    }

    private CompetitionService.CompetitionMetricSnapshot snapshot(boolean targetReached) {
        return new CompetitionService.CompetitionMetricSnapshot(10, 10, 80, 100, targetReached, "done");
    }

}
