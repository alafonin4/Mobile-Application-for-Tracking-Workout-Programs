package ru.alafonin4.socialservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import ru.alafonin4.socialservice.dto.NotificationItemDto;
import ru.alafonin4.socialservice.dto.NotificationsResponse;
import ru.alafonin4.socialservice.dto.RemoteUserDto;
import ru.alafonin4.socialservice.dto.RemoteWorkoutAchievementDto;
import ru.alafonin4.socialservice.dto.RemoteWorkoutPersonalizationProfileDto;
import ru.alafonin4.socialservice.dto.RemoteWorkoutSmartReminderDto;
import ru.alafonin4.socialservice.dto.SocialAchievementDto;
import ru.alafonin4.socialservice.dto.SocialPersonalizationResponse;
import ru.alafonin4.socialservice.entities.Competition;
import ru.alafonin4.socialservice.entities.CompetitionParticipant;
import ru.alafonin4.socialservice.entities.FriendRequest;
import ru.alafonin4.socialservice.enums.CompetitionParticipantStatus;
import ru.alafonin4.socialservice.enums.FriendRequestStatus;
import ru.alafonin4.socialservice.repositories.CompetitionRepository;
import ru.alafonin4.socialservice.repositories.FriendRequestRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private SocialPersonalizationService socialPersonalizationService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void buildFeedAggregatesAndSortsRelevantNotifications() {
        FriendRequest incoming = friendRequest(10L, 2L, 1L, FriendRequestStatus.PENDING, LocalDateTime.now().minusHours(4), null);
        FriendRequest accepted = friendRequest(11L, 1L, 3L, FriendRequestStatus.ACCEPTED, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(1));
        Competition competition = pendingCompetitionInvite(20L, 4L, 1L, LocalDateTime.now().minusHours(2));

        when(friendRequestRepository.findByReceiverIdAndStatus(1L, FriendRequestStatus.PENDING)).thenReturn(List.of(incoming));
        when(friendRequestRepository.findBySenderIdAndStatus(1L, FriendRequestStatus.ACCEPTED)).thenReturn(List.of(accepted));
        when(competitionRepository.findDetailedByUserId(1L)).thenReturn(List.of(competition));
        when(restTemplate.exchange(
                eq("http://user-service/api/users/bulk"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(RemoteUserDto[].class)
        )).thenReturn(ResponseEntity.ok(new RemoteUserDto[]{user(2L, "Petr"), user(3L, "Anna"), user(4L, "Oleg")}));
        when(restTemplate.getForObject(
                "http://workout-service/api/personalization/user/1",
                RemoteWorkoutPersonalizationProfileDto.class
        )).thenReturn(workoutProfile());
        when(socialPersonalizationService.buildProfile(1L)).thenReturn(socialProfile());

        NotificationsResponse response = notificationService.buildFeed(1L);

        assertEquals(6, response.getNotifications().size());
        assertEquals("COMPETITION_INVITE", response.getNotifications().get(0).getType());
        assertEquals("FRIEND_REQUEST", response.getNotifications().get(1).getType());
        assertTrue(response.getNotifications().stream().map(NotificationItemDto::getType).toList().contains("SMART_REMINDER"));
        assertTrue(response.getNotifications().stream().map(NotificationItemDto::getType).toList().contains("FRIEND_REQUEST"));
        assertTrue(response.getNotifications().stream().map(NotificationItemDto::getType).toList().contains("FRIEND_ACCEPTED"));
        assertTrue(response.getNotifications().stream().map(NotificationItemDto::getType).toList().contains("ACHIEVEMENT"));
    }

    @Test
    void buildFeedSkipsOldAndLockedEvents() {
        FriendRequest oldAccepted = friendRequest(12L, 1L, 2L, FriendRequestStatus.ACCEPTED, LocalDateTime.now().minusDays(20), LocalDateTime.now().minusDays(20));

        when(friendRequestRepository.findByReceiverIdAndStatus(1L, FriendRequestStatus.PENDING)).thenReturn(List.of());
        when(friendRequestRepository.findBySenderIdAndStatus(1L, FriendRequestStatus.ACCEPTED)).thenReturn(List.of(oldAccepted));
        when(competitionRepository.findDetailedByUserId(1L)).thenReturn(List.of());
        when(restTemplate.exchange(
                eq("http://user-service/api/users/bulk"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(RemoteUserDto[].class)
        )).thenReturn(ResponseEntity.ok(new RemoteUserDto[0]));
        when(restTemplate.getForObject(
                "http://workout-service/api/personalization/user/1",
                RemoteWorkoutPersonalizationProfileDto.class
        )).thenReturn(new RemoteWorkoutPersonalizationProfileDto());
        when(socialPersonalizationService.buildProfile(1L)).thenReturn(new SocialPersonalizationResponse());

        NotificationsResponse response = notificationService.buildFeed(1L);

        assertEquals(0, response.getNotifications().size());
    }

    private FriendRequest friendRequest(
            Long id,
            Long senderId,
            Long receiverId,
            FriendRequestStatus status,
            LocalDateTime createdAt,
            LocalDateTime respondedAt
    ) {
        FriendRequest request = new FriendRequest();
        ReflectionTestUtils.setField(request, "id", id);
        request.setSenderId(senderId);
        request.setReceiverId(receiverId);
        request.setStatus(status);
        request.setCreatedAt(createdAt);
        request.setRespondedAt(respondedAt);
        return request;
    }

    private Competition pendingCompetitionInvite(Long competitionId, Long creatorId, Long userId, LocalDateTime invitedAt) {
        Competition competition = new Competition();
        competition.setId(competitionId);
        competition.setTitle("May Cup");
        competition.setCreatorId(creatorId);

        CompetitionParticipant participant = new CompetitionParticipant();
        participant.setCompetition(competition);
        participant.setUserId(userId);
        participant.setStatus(CompetitionParticipantStatus.PENDING);
        participant.setInvitedAt(invitedAt);
        competition.setParticipants(List.of(participant));
        return competition;
    }

    private RemoteUserDto user(Long id, String firstName) {
        RemoteUserDto user = new RemoteUserDto();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName("User");
        return user;
    }

    private RemoteWorkoutPersonalizationProfileDto workoutProfile() {
        RemoteWorkoutSmartReminderDto reminder = new RemoteWorkoutSmartReminderDto();
        reminder.setCode("MISSED_WORKOUTS");
        reminder.setTitle("Back to training");
        reminder.setMessage("Time to return.");
        reminder.setSeverity("medium");
        reminder.setCreatedAt(LocalDate.now().minusDays(1).toString());

        RemoteWorkoutAchievementDto workoutAchievement = new RemoteWorkoutAchievementDto();
        workoutAchievement.setCode("FIRST_WORKOUT");
        workoutAchievement.setTitle("First Workout");
        workoutAchievement.setUnlocked(true);
        workoutAchievement.setAwardedAt(LocalDate.now().toString());

        RemoteWorkoutPersonalizationProfileDto profile = new RemoteWorkoutPersonalizationProfileDto();
        profile.setAchievements(List.of(workoutAchievement));
        profile.setSmartReminders(List.of(reminder));
        return profile;
    }

    private SocialPersonalizationResponse socialProfile() {
        SocialAchievementDto socialAchievement = new SocialAchievementDto();
        socialAchievement.setCode("GLOBAL_TOP_25");
        socialAchievement.setTitle("Top 25%");
        socialAchievement.setUnlocked(true);
        socialAchievement.setAwardedAt(LocalDate.now().minusDays(1).toString());

        SocialPersonalizationResponse response = new SocialPersonalizationResponse();
        response.setAchievements(List.of(socialAchievement));
        return response;
    }
}
