package ru.alafonin4.socialservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class NotificationService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private SocialPersonalizationService socialPersonalizationService;

    @Autowired
    private RestTemplate restTemplate;

    public NotificationsResponse buildFeed(Long userId) {
        Map<Long, RemoteUserDto> userMap = fetchAllUsers().stream()
                .collect(Collectors.toMap(RemoteUserDto::getId, user -> user, (left, right) -> left, HashMap::new));

        List<NotificationEnvelope> items = new ArrayList<>();

        for (FriendRequest request : friendRequestRepository.findByReceiverIdAndStatus(userId, FriendRequestStatus.PENDING)) {
            items.add(new NotificationEnvelope(
                    request.getCreatedAt(),
                    toNotification(
                            "friend-request-" + request.getId(),
                            "FRIEND_REQUEST",
                            "Новая заявка в друзья",
                            formatUserName(userMap.get(request.getSenderId()), request.getSenderId()) + " отправил(а) вам заявку в друзья.",
                            "high",
                            request.getCreatedAt(),
                            request.getSenderId(),
                            request.getId(),
                            null
                    )
            ));
        }

        for (FriendRequest request : friendRequestRepository.findBySenderIdAndStatus(userId, FriendRequestStatus.ACCEPTED)) {
            LocalDateTime respondedAt = request.getRespondedAt();
            if (respondedAt == null || respondedAt.isBefore(LocalDateTime.now().minusDays(14))) {
                continue;
            }

            items.add(new NotificationEnvelope(
                    respondedAt,
                    toNotification(
                            "friend-accepted-" + request.getId(),
                            "FRIEND_ACCEPTED",
                            "Заявка в друзья принята",
                            formatUserName(userMap.get(request.getReceiverId()), request.getReceiverId()) + " принял(а) вашу заявку в друзья.",
                            "medium",
                            respondedAt,
                            request.getReceiverId(),
                            request.getId(),
                            null
                    )
            ));
        }

        for (Competition competition : competitionRepository.findDetailedByUserId(userId)) {
            CompetitionParticipant participant = competition.getParticipants().stream()
                    .filter(item -> Objects.equals(item.getUserId(), userId))
                    .findFirst()
                    .orElse(null);
            if (participant == null || participant.getStatus() != CompetitionParticipantStatus.PENDING) {
                continue;
            }

            items.add(new NotificationEnvelope(
                    participant.getInvitedAt(),
                    toNotification(
                            "competition-invite-" + competition.getId(),
                            "COMPETITION_INVITE",
                            "Приглашение в соревнование",
                            formatUserName(userMap.get(competition.getCreatorId()), competition.getCreatorId())
                                    + " приглашает вас в соревнование \"" + competition.getTitle() + "\".",
                            "high",
                            participant.getInvitedAt(),
                            competition.getCreatorId(),
                            null,
                            competition.getId()
                    )
            ));
        }

        RemoteWorkoutPersonalizationProfileDto workoutProfile = fetchWorkoutPersonalization(userId);
        for (RemoteWorkoutSmartReminderDto reminder : workoutProfile.getSmartReminders()) {
            items.add(new NotificationEnvelope(
                    normalizeDateTime(reminder.getCreatedAt()),
                    toNotification(
                            "reminder-" + reminder.getCode(),
                            "SMART_REMINDER",
                            reminder.getTitle(),
                            reminder.getMessage(),
                            reminder.getSeverity() == null ? "low" : reminder.getSeverity(),
                            normalizeDateTime(reminder.getCreatedAt()),
                            null,
                            null,
                            null
                    )
            ));
        }

        for (RemoteWorkoutAchievementDto achievement : workoutProfile.getAchievements()) {
            LocalDateTime awardedAt = normalizeDateTime(achievement.getAwardedAt());
            if (!achievement.isUnlocked() || awardedAt == null || awardedAt.isBefore(LocalDateTime.now().minusDays(7))) {
                continue;
            }

            items.add(new NotificationEnvelope(
                    awardedAt,
                    toNotification(
                            "achievement-" + achievement.getCode(),
                            "ACHIEVEMENT",
                            "Новое достижение",
                            "Вы открыли достижение \"" + achievement.getTitle() + "\".",
                            "low",
                            awardedAt,
                            null,
                            null,
                            null
                    )
            ));
        }

        SocialPersonalizationResponse socialProfile = socialPersonalizationService.buildProfile(userId);
        for (SocialAchievementDto achievement : socialProfile.getAchievements()) {
            LocalDateTime awardedAt = normalizeDateTime(achievement.getAwardedAt());
            if (!achievement.isUnlocked() || awardedAt == null || awardedAt.isBefore(LocalDateTime.now().minusDays(7))) {
                continue;
            }

            items.add(new NotificationEnvelope(
                    awardedAt,
                    toNotification(
                            "social-achievement-" + achievement.getCode(),
                            "ACHIEVEMENT",
                            "Новое достижение",
                            "Вы открыли достижение \"" + achievement.getTitle() + "\".",
                            "low",
                            awardedAt,
                            null,
                            null,
                            null
                    )
            ));
        }

        items.sort(Comparator.comparing(NotificationEnvelope::createdAt, Comparator.nullsLast(Comparator.reverseOrder())));

        NotificationsResponse response = new NotificationsResponse();
        response.setUserId(userId);
        response.setNotifications(items.stream().map(NotificationEnvelope::notification).toList());
        return response;
    }

    private NotificationItemDto toNotification(
            String id,
            String type,
            String title,
            String message,
            String priority,
            LocalDateTime createdAt,
            Long relatedUserId,
            Long requestId,
            Long competitionId
    ) {
        NotificationItemDto dto = new NotificationItemDto();
        dto.setId(id);
        dto.setType(type);
        dto.setTitle(title);
        dto.setMessage(message);
        dto.setPriority(priority);
        dto.setCreatedAt(createdAt == null ? null : createdAt.toString());
        dto.setRelatedUserId(relatedUserId);
        dto.setRequestId(requestId);
        dto.setCompetitionId(competitionId);
        return dto;
    }

    private RemoteWorkoutPersonalizationProfileDto fetchWorkoutPersonalization(Long userId) {
        RemoteWorkoutPersonalizationProfileDto response = restTemplate.getForObject(
                "http://workout-service/api/personalization/user/" + userId,
                RemoteWorkoutPersonalizationProfileDto.class
        );
        return response == null ? new RemoteWorkoutPersonalizationProfileDto() : response;
    }

    private List<RemoteUserDto> fetchAllUsers() {
        RemoteUserDto[] response = restTemplate.getForObject("http://user-service/api/users", RemoteUserDto[].class);
        return response == null ? List.of() : Arrays.asList(response);
    }

    private String formatUserName(RemoteUserDto user, Long fallbackUserId) {
        if (user == null) {
            return "Пользователь #" + fallbackUserId;
        }

        String fullName = ((user.getFirstName() == null ? "" : user.getFirstName()) + " "
                + (user.getLastName() == null ? "" : user.getLastName())).trim();
        return fullName.isBlank() ? "Пользователь #" + fallbackUserId : fullName;
    }

    private LocalDateTime normalizeDateTime(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return null;
        }
        if (rawValue.contains("T")) {
            return LocalDateTime.parse(rawValue);
        }
        return LocalDate.parse(rawValue).atStartOfDay();
    }

    private record NotificationEnvelope(LocalDateTime createdAt, NotificationItemDto notification) {
    }
}
