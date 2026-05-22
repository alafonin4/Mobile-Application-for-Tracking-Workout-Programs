package ru.alafonin4.socialservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.alafonin4.socialservice.dto.CompetitionCreateRequest;
import ru.alafonin4.socialservice.dto.CompetitionLeaderboardResponse;
import ru.alafonin4.socialservice.dto.RemoteLeaderboardProgressEntryDto;
import ru.alafonin4.socialservice.dto.RemoteExerciseDto;
import ru.alafonin4.socialservice.dto.RemoteExerciseSetDto;
import ru.alafonin4.socialservice.dto.RemoteProgressSummaryDto;
import ru.alafonin4.socialservice.dto.RemoteUserDto;
import ru.alafonin4.socialservice.dto.RemoteWorkoutDto;
import ru.alafonin4.socialservice.dto.RemoteWorkoutExerciseDto;
import ru.alafonin4.socialservice.dto.RemoteWorkoutProgressResponse;
import ru.alafonin4.socialservice.entities.Competition;
import ru.alafonin4.socialservice.entities.CompetitionParticipant;
import ru.alafonin4.socialservice.entities.FriendRequest;
import ru.alafonin4.socialservice.enums.CompetitionGoalType;
import ru.alafonin4.socialservice.enums.CompetitionParticipantStatus;
import ru.alafonin4.socialservice.enums.FriendRequestStatus;
import ru.alafonin4.socialservice.repositories.CompetitionParticipantRepository;
import ru.alafonin4.socialservice.repositories.CompetitionRepository;
import ru.alafonin4.socialservice.repositories.FriendRequestRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompetitionServiceTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private CompetitionParticipantRepository competitionParticipantRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CompetitionService competitionService;

    @Test
    void createCompetitionAddsCreatorAndAcceptedFriendsOnly() {
        when(friendRequestRepository.findBySenderIdOrReceiverIdAndStatus(1L, 1L, FriendRequestStatus.ACCEPTED))
                .thenReturn(List.of(friendship(1L, 2L)));
        when(competitionRepository.save(any(Competition.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompetitionCreateRequest request = new CompetitionCreateRequest();
        request.setCreatorId(1L);
        request.setTitle("  Summer Cup  ");
        request.setDescription("  Weekly grind  ");
        request.setGoalType(CompetitionGoalType.WORKOUT_COUNT);
        request.setTargetValue(10.0);
        request.setPeriodMonths(2);
        request.setInvitedUserIds(List.of(1L, 2L, 3L));

        Competition result = competitionService.createCompetition(request);

        assertEquals("Summer Cup", result.getTitle());
        assertEquals("Weekly grind", result.getDescription());
        assertEquals(2, result.getParticipants().size());
        assertTrue(result.getParticipants().stream()
                .anyMatch(item -> item.getUserId().equals(1L) && item.getStatus() == CompetitionParticipantStatus.ACCEPTED));
        assertTrue(result.getParticipants().stream()
                .anyMatch(item -> item.getUserId().equals(2L) && item.getStatus() == CompetitionParticipantStatus.PENDING));
        assertFalse(result.getParticipants().stream().anyMatch(item -> item.getUserId().equals(3L)));
    }

    @Test
    void createCompetitionRejectsExerciseCompetitionWithoutExerciseId() {
        CompetitionCreateRequest request = new CompetitionCreateRequest();
        request.setCreatorId(1L);
        request.setTitle("Reps");
        request.setGoalType(CompetitionGoalType.EXERCISE_REPS);
        request.setTargetValue(50.0);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> competitionService.createCompetition(request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void getGlobalLeaderboardBuildsRankedResponse() {
        stubUserSummaries(user(1L, "Ivan"), user(2L, "Petr"), user(3L, "Anna"));
        LocalDate fromDate = YearMonth.now().atDay(1);
        LocalDate toDate = fromDate.plusMonths(1);
        stubBatchProgress(
                entry(1L, 40, 10),
                entry(2L, 120, 22),
                entry(3L, 80, 18)
        );

        CompetitionLeaderboardResponse response = competitionService.getGlobalLeaderboard(3L, 1);

        assertEquals(3, response.getEntries().size());
        assertEquals(1L, response.getEntries().get(0).getUserId());
        assertEquals(3L, response.getEntries().get(1).getUserId());
        assertEquals(2, response.getCurrentUserRank());
        assertNotNull(response.getStartsAt());
        assertNotNull(response.getEndsAt());
    }

    @Test
    void getFriendsLeaderboardIncludesCurrentUserAndAcceptedFriends() {
        when(friendRequestRepository.findBySenderIdOrReceiverIdAndStatus(1L, 1L, FriendRequestStatus.ACCEPTED))
                .thenReturn(List.of(friendship(1L, 2L)));
        stubUserBulk(user(1L, "Ivan"), user(2L, "Petr"));

        LocalDate fromDate = YearMonth.now().atDay(1);
        LocalDate toDate = fromDate.plusMonths(1);
        stubBatchProgress(
                entry(1L, 60, 11),
                entry(2L, 90, 17)
        );

        CompetitionLeaderboardResponse response = competitionService.getFriendsLeaderboard(1L, 1);

        assertEquals(2, response.getEntries().size());
        assertTrue(response.getEntries().stream().allMatch(item -> item.getUserId().equals(1L) || item.getUserId().equals(2L)));
    }

    @Test
    void getCompetitionLeaderboardUsesAcceptedParticipantsOnly() {
        Competition competition = new Competition();
        competition.setId(77L);
        competition.setTitle("Workout Rush");
        competition.setGoalType(CompetitionGoalType.WORKOUT_COUNT);
        competition.setTargetValue(3.0);
        competition.setPeriodMonths(1);
        competition.setParticipants(List.of(
                participant(competition, 1L, CompetitionParticipantStatus.ACCEPTED),
                participant(competition, 2L, CompetitionParticipantStatus.ACCEPTED),
                participant(competition, 3L, CompetitionParticipantStatus.DECLINED)
        ));

        when(competitionRepository.findDetailedById(77L)).thenReturn(Optional.of(competition));
        stubUserBulk(user(1L, "Ivan"), user(2L, "Petr"), user(3L, "Anna"));
        stubWorkouts(1L, workoutWithDate(LocalDateTime.now().minusDays(2)));
        stubWorkouts(2L, workoutWithDate(LocalDateTime.now().minusDays(3)), workoutWithDate(LocalDateTime.now().minusDays(1)));

        CompetitionLeaderboardResponse response = competitionService.getCompetitionLeaderboard(77L, 1L);

        assertEquals(2, response.getEntries().size());
        assertEquals(1L, response.getEntries().get(0).getUserId());
        assertEquals(2L, response.getEntries().get(1).getUserId());
        assertEquals(1, response.getEntries().get(0).getRank());
        assertEquals(2, response.getEntries().get(1).getRank());
        assertEquals(1, response.getCurrentUserRank());
    }

    @Test
    void evaluateCompetitionCountsExerciseRepetitionsAcrossWorkouts() {
        Competition competition = new Competition();
        competition.setGoalType(CompetitionGoalType.EXERCISE_REPS);
        competition.setExerciseId(10L);
        competition.setTargetValue(20.0);
        competition.setPeriodMonths(1);

        stubWorkouts(1L,
                workoutWithExercise(10L, 8, 40.0),
                workoutWithExercise(10L, 12, 45.0),
                workoutWithExercise(99L, 20, 20.0)
        );

        CompetitionService.CompetitionMetricSnapshot snapshot = competitionService.evaluateCompetition(competition, 1L);

        assertEquals(20.0, snapshot.currentValue());
        assertTrue(snapshot.targetReached());
        assertEquals(100.0, snapshot.goalProgressPercent());
    }

    @Test
    void acceptInvitationMarksParticipantAccepted() {
        Competition competition = new Competition();
        competition.setId(5L);
        CompetitionParticipant participant = participant(competition, 3L, CompetitionParticipantStatus.PENDING);

        when(competitionParticipantRepository.findByCompetitionIdAndUserId(5L, 3L)).thenReturn(Optional.of(participant));
        when(competitionRepository.findDetailedById(5L)).thenReturn(Optional.of(competition));

        Competition result = competitionService.acceptInvitation(5L, 3L);

        assertEquals(CompetitionParticipantStatus.ACCEPTED, participant.getStatus());
        assertNotNull(participant.getRespondedAt());
        assertEquals(5L, result.getId());
    }

    @Test
    void declineInvitationMarksParticipantDeclined() {
        Competition competition = new Competition();
        competition.setId(6L);
        CompetitionParticipant participant = participant(competition, 4L, CompetitionParticipantStatus.PENDING);

        when(competitionParticipantRepository.findByCompetitionIdAndUserId(6L, 4L)).thenReturn(Optional.of(participant));
        when(competitionRepository.findDetailedById(6L)).thenReturn(Optional.of(competition));

        Competition result = competitionService.declineInvitation(6L, 4L);

        assertEquals(CompetitionParticipantStatus.DECLINED, participant.getStatus());
        assertNotNull(participant.getRespondedAt());
        assertEquals(6L, result.getId());
    }

    private void stubUserSummaries(RemoteUserDto... users) {
        when(restTemplate.getForObject("http://user-service/api/users/summary", RemoteUserDto[].class)).thenReturn(users);
    }

    private void stubUserBulk(RemoteUserDto... users) {
        when(restTemplate.exchange(
                eq("http://user-service/api/users/bulk"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(RemoteUserDto[].class)
        )).thenReturn(ResponseEntity.ok(users));
    }

    private void stubBatchProgress(RemoteLeaderboardProgressEntryDto... entries) {
        when(restTemplate.exchange(
                eq("http://workout-service/api/workouts/progress/leaderboard"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(RemoteLeaderboardProgressEntryDto[].class)
        )).thenReturn(ResponseEntity.ok(entries));
    }

    private void stubWorkouts(Long userId, RemoteWorkoutDto... workouts) {
        when(restTemplate.getForObject(
                "http://workout-service/api/workouts/user/" + userId,
                RemoteWorkoutDto[].class
        )).thenReturn(workouts);
    }

    private RemoteUserDto user(Long id, String firstName) {
        RemoteUserDto user = new RemoteUserDto();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName("User");
        return user;
    }

    private RemoteLeaderboardProgressEntryDto entry(Long userId, double score, double percent) {
        RemoteLeaderboardProgressEntryDto entry = new RemoteLeaderboardProgressEntryDto();
        entry.setUserId(userId);
        entry.setCompositeScore(score);
        entry.setProgressPercent(percent);
        return entry;
    }

    private FriendRequest friendship(Long firstUserId, Long secondUserId) {
        FriendRequest request = new FriendRequest();
        request.setSenderId(firstUserId);
        request.setReceiverId(secondUserId);
        request.setStatus(FriendRequestStatus.ACCEPTED);
        return request;
    }

    private CompetitionParticipant participant(Competition competition, Long userId, CompetitionParticipantStatus status) {
        CompetitionParticipant participant = new CompetitionParticipant();
        participant.setCompetition(competition);
        participant.setUserId(userId);
        participant.setStatus(status);
        participant.setInvitedAt(LocalDateTime.now().minusDays(1));
        return participant;
    }

    private RemoteWorkoutDto workoutWithDate(LocalDateTime date) {
        RemoteWorkoutDto workout = new RemoteWorkoutDto();
        workout.setWorkoutDate(date);
        return workout;
    }

    private RemoteWorkoutDto workoutWithExercise(Long exerciseId, int reps, double weight) {
        RemoteExerciseDto exercise = new RemoteExerciseDto();
        exercise.setId(exerciseId);
        exercise.setName("Exercise");

        RemoteExerciseSetDto set = new RemoteExerciseSetDto();
        set.setReps(reps);
        set.setWeight(weight);

        RemoteWorkoutExerciseDto workoutExercise = new RemoteWorkoutExerciseDto();
        workoutExercise.setExercise(exercise);
        workoutExercise.setSets(List.of(set));

        RemoteWorkoutDto workout = new RemoteWorkoutDto();
        workout.setWorkoutDate(LocalDateTime.now().minusDays(1));
        workout.setWorkoutExercises(List.of(workoutExercise));
        return workout;
    }
}
