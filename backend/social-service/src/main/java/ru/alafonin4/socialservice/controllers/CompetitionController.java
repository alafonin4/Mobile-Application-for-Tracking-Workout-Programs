package ru.alafonin4.socialservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alafonin4.socialservice.dto.CompetitionCreateRequest;
import ru.alafonin4.socialservice.dto.CompetitionLeaderboardResponse;
import ru.alafonin4.socialservice.dto.CompetitionOverviewDto;
import ru.alafonin4.socialservice.service.CompetitionService;

import java.util.List;

@RestController
@RequestMapping("/api/competitions")
public class CompetitionController {

    @Autowired
    private CompetitionService competitionService;

    /**
     * Returns the global leaderboard.
     * @param userId identifier of the user
     * @param months amount of months included in the analysis
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/leaderboards/global/{userId}")
    public ResponseEntity<CompetitionLeaderboardResponse> getGlobalLeaderboard(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") int months
    ) {
        return ResponseEntity.ok(competitionService.getGlobalLeaderboard(userId, months));
    }

    /**
     * Returns the friends leaderboard.
     * @param userId identifier of the user
     * @param months amount of months included in the analysis
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/leaderboards/friends/{userId}")
    public ResponseEntity<CompetitionLeaderboardResponse> getFriendsLeaderboard(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") int months
    ) {
        return ResponseEntity.ok(competitionService.getFriendsLeaderboard(userId, months));
    }

    /**
     * Returns the user competitions.
     * @param userId identifier of the user
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CompetitionOverviewDto>> getUserCompetitions(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(competitionService.getUserCompetitions(userId));
    }

    /**
     * Creates a new competition.
     * @param request request payload
     * @return HTTP response containing the requested payload
     */
    @PostMapping("/")
    public ResponseEntity<Void> createCompetition(@RequestBody CompetitionCreateRequest request) {
        competitionService.createCompetition(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Returns the competition leaderboard.
     * @param competitionId identifier of the competition
     * @param userId identifier of the user
     * @return HTTP response containing the requested payload
     */
    @GetMapping("/{competitionId}/leaderboard")
    public ResponseEntity<CompetitionLeaderboardResponse> getCompetitionLeaderboard(
            @PathVariable("competitionId") Long competitionId,
            @RequestParam("userId") Long userId
    ) {
        return ResponseEntity.ok(competitionService.getCompetitionLeaderboard(competitionId, userId));
    }

    /**
     * AcceptInvitation.
     * @param competitionId identifier of the competition
     * @param userId identifier of the user
     * @return HTTP response containing the requested payload
     */
    @PutMapping("/{competitionId}/participants/{userId}/accept")
    public ResponseEntity<Void> acceptInvitation(
            @PathVariable("competitionId") Long competitionId,
            @PathVariable("userId") Long userId
    ) {
        competitionService.acceptInvitation(competitionId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * DeclineInvitation.
     * @param competitionId identifier of the competition
     * @param userId identifier of the user
     * @return HTTP response containing the requested payload
     */
    @PutMapping("/{competitionId}/participants/{userId}/decline")
    public ResponseEntity<Void> declineInvitation(
            @PathVariable("competitionId") Long competitionId,
            @PathVariable("userId") Long userId
    ) {
        competitionService.declineInvitation(competitionId, userId);
        return ResponseEntity.noContent().build();
    }
}
