package ru.alafonin4.socialservice.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.alafonin4.socialservice.dto.CompetitionCreateRequest;
import ru.alafonin4.socialservice.dto.CompetitionLeaderboardResponse;
import ru.alafonin4.socialservice.dto.CompetitionOverviewDto;
import ru.alafonin4.socialservice.service.CompetitionService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompetitionControllerTest {

    @Mock
    private CompetitionService competitionService;

    @InjectMocks
    private CompetitionController competitionController;

    @Test
    void createCompetitionReturnsCreatedStatus() {
        CompetitionCreateRequest request = new CompetitionCreateRequest();

        ResponseEntity<Void> response = competitionController.createCompetition(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void getGlobalLeaderboardReturnsPayload() {
        CompetitionLeaderboardResponse leaderboard = new CompetitionLeaderboardResponse();
        when(competitionService.getGlobalLeaderboard(1L, 2)).thenReturn(leaderboard);

        ResponseEntity<CompetitionLeaderboardResponse> response = competitionController.getGlobalLeaderboard(1L, 2);

        assertEquals(leaderboard, response.getBody());
    }

    @Test
    void getUserCompetitionsReturnsList() {
        when(competitionService.getUserCompetitions(1L)).thenReturn(List.of(new CompetitionOverviewDto()));

        ResponseEntity<List<CompetitionOverviewDto>> response = competitionController.getUserCompetitions(1L);

        assertEquals(1, response.getBody().size());
    }

    @Test
    void acceptInvitationReturnsNoContent() {
        ResponseEntity<Void> response = competitionController.acceptInvitation(5L, 6L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
