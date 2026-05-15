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

    @GetMapping("/leaderboards/global/{userId}")
    public ResponseEntity<CompetitionLeaderboardResponse> getGlobalLeaderboard(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") int months
    ) {
        return ResponseEntity.ok(competitionService.getGlobalLeaderboard(userId, months));
    }

    @GetMapping("/leaderboards/friends/{userId}")
    public ResponseEntity<CompetitionLeaderboardResponse> getFriendsLeaderboard(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") int months
    ) {
        return ResponseEntity.ok(competitionService.getFriendsLeaderboard(userId, months));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CompetitionOverviewDto>> getUserCompetitions(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(competitionService.getUserCompetitions(userId));
    }

    @PostMapping("/")
    public ResponseEntity<Void> createCompetition(@RequestBody CompetitionCreateRequest request) {
        competitionService.createCompetition(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{competitionId}/leaderboard")
    public ResponseEntity<CompetitionLeaderboardResponse> getCompetitionLeaderboard(
            @PathVariable("competitionId") Long competitionId,
            @RequestParam("userId") Long userId
    ) {
        return ResponseEntity.ok(competitionService.getCompetitionLeaderboard(competitionId, userId));
    }

    @PutMapping("/{competitionId}/participants/{userId}/accept")
    public ResponseEntity<Void> acceptInvitation(
            @PathVariable("competitionId") Long competitionId,
            @PathVariable("userId") Long userId
    ) {
        competitionService.acceptInvitation(competitionId, userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{competitionId}/participants/{userId}/decline")
    public ResponseEntity<Void> declineInvitation(
            @PathVariable("competitionId") Long competitionId,
            @PathVariable("userId") Long userId
    ) {
        competitionService.declineInvitation(competitionId, userId);
        return ResponseEntity.noContent().build();
    }
}
