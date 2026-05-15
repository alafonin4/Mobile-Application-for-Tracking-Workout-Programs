package ru.alafonin4.socialservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.alafonin4.socialservice.dto.CompetitionCreateRequest;
import ru.alafonin4.socialservice.dto.CompetitionLeaderboardEntryDto;
import ru.alafonin4.socialservice.dto.CompetitionLeaderboardResponse;
import ru.alafonin4.socialservice.dto.CompetitionOverviewDto;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CompetitionService {

    private static final int DEFAULT_PERIOD_MONTHS = 1;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private CompetitionParticipantRepository competitionParticipantRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private RestTemplate restTemplate;

    public CompetitionLeaderboardResponse getGlobalLeaderboard(Long currentUserId, int months) {
        LocalDate monthStart = YearMonth.now().atDay(1);
        LocalDate nextMonthStart = monthStart.plusMonths(1);
        List<RemoteUserDto> users = fetchAllUsers();
        List<CompetitionLeaderboardEntryDto> entries = users.stream()
                .map(user -> buildMonthlyProgressLeaderboardEntry(user, currentUserId, monthStart, nextMonthStart))
                .sorted(Comparator.comparingDouble(CompetitionLeaderboardEntryDto::getScore).reversed()
                        .thenComparingDouble(CompetitionLeaderboardEntryDto::getProgressPercent).reversed()
                        .thenComparing(CompetitionLeaderboardEntryDto::getUserName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        CompetitionLeaderboardResponse response = buildLeaderboardResponse(
                "GLOBAL_PROGRESS",
                "Общее соревнование по прогрессу",
                "Ежемесячный рейтинг всех пользователей по прогрессу. Сбрасывается первого числа каждого месяца.",
                "PROGRESS_SCORE",
                "Набрать как можно больше баллов прогресса за текущий месяц",
                "Баллы прогресса",
                null,
                entries,
                currentUserId
        );
        response.setStartsAt(monthStart.atStartOfDay().toString());
        response.setEndsAt(nextMonthStart.atStartOfDay().toString());
        return response;
    }

    public CompetitionLeaderboardResponse getFriendsLeaderboard(Long currentUserId, int months) {
        LocalDate monthStart = YearMonth.now().atDay(1);
        LocalDate nextMonthStart = monthStart.plusMonths(1);
        Set<Long> friendIds = getAcceptedFriendIds(currentUserId);
        friendIds.add(currentUserId);

        List<CompetitionLeaderboardEntryDto> entries = fetchAllUsers().stream()
                .filter(user -> friendIds.contains(user.getId()))
                .map(user -> buildMonthlyProgressLeaderboardEntry(user, currentUserId, monthStart, nextMonthStart))
                .sorted(Comparator.comparingDouble(CompetitionLeaderboardEntryDto::getScore).reversed()
                        .thenComparingDouble(CompetitionLeaderboardEntryDto::getProgressPercent).reversed()
                        .thenComparing(CompetitionLeaderboardEntryDto::getUserName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        CompetitionLeaderboardResponse response = buildLeaderboardResponse(
                "FRIENDS_PROGRESS",
                "Соревнование по прогрессу среди друзей",
                "Ежемесячный рейтинг друзей по прогрессу. Сбрасывается первого числа каждого месяца.",
                "PROGRESS_SCORE",
                "Набрать как можно больше баллов прогресса за текущий месяц",
                "Баллы прогресса",
                null,
                entries,
                currentUserId
        );
        response.setStartsAt(monthStart.atStartOfDay().toString());
        response.setEndsAt(nextMonthStart.atStartOfDay().toString());
        return response;
    }

    public List<CompetitionOverviewDto> getUserCompetitions(Long userId) {
        Map<Long, RemoteUserDto> userMap = fetchAllUsers().stream()
                .collect(Collectors.toMap(RemoteUserDto::getId, user -> user, (left, right) -> left));

        return competitionRepository.findDetailedByUserId(userId).stream()
                .map(competition -> toOverview(competition, userId, userMap))
                .sorted(Comparator.comparing(CompetitionOverviewDto::getCurrentUserStatus)
                        .thenComparing(CompetitionOverviewDto::getId, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public Competition createCompetition(CompetitionCreateRequest request) {
        validateCreateRequest(request);

        Competition competition = new Competition();
        competition.setCreatorId(request.getCreatorId());
        competition.setTitle(request.getTitle().trim());
        competition.setDescription(request.getDescription() == null ? "" : request.getDescription().trim());
        competition.setGoalType(request.getGoalType());
        competition.setTargetValue(request.getTargetValue());
        competition.setExerciseId(request.getExerciseId());
        competition.setExerciseName(request.getExerciseName());
        competition.setPeriodMonths(normalizePeriodMonths(request.getPeriodMonths()));
        competition.setCreatedAt(LocalDateTime.now());

        addParticipant(competition, request.getCreatorId(), CompetitionParticipantStatus.ACCEPTED, LocalDateTime.now());

        Set<Long> allowedFriendIds = getAcceptedFriendIds(request.getCreatorId());
        Set<Long> invitedUserIds = request.getInvitedUserIds() == null
                ? Set.of()
                : new HashSet<>(request.getInvitedUserIds());

        for (Long invitedUserId : invitedUserIds) {
            if (Objects.equals(invitedUserId, request.getCreatorId())) {
                continue;
            }
            if (!allowedFriendIds.contains(invitedUserId)) {
                continue;
            }
            addParticipant(competition, invitedUserId, CompetitionParticipantStatus.PENDING, LocalDateTime.now());
        }

        return competitionRepository.save(competition);
    }

    public CompetitionLeaderboardResponse getCompetitionLeaderboard(Long competitionId, Long currentUserId) {
        Competition competition = getCompetitionOrThrow(competitionId);
        List<CompetitionParticipant> acceptedParticipants = competition.getParticipants().stream()
                .filter(participant -> participant.getStatus() == CompetitionParticipantStatus.ACCEPTED)
                .collect(Collectors.toList());

        Map<Long, RemoteUserDto> userMap = fetchAllUsers().stream()
                .collect(Collectors.toMap(RemoteUserDto::getId, user -> user, (left, right) -> left));

        List<CompetitionLeaderboardEntryDto> entries = acceptedParticipants.stream()
                .map(participant -> buildCompetitionEntry(competition, participant.getUserId(), currentUserId, userMap))
                .sorted(Comparator.comparingDouble(CompetitionLeaderboardEntryDto::getScore).reversed()
                        .thenComparingDouble(CompetitionLeaderboardEntryDto::getGoalProgressPercent).reversed()
                        .thenComparing(CompetitionLeaderboardEntryDto::getUserName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());

        return buildLeaderboardResponse(
                "PERSONAL_COMPETITION",
                competition.getTitle(),
                competition.getDescription(),
                competition.getGoalType().name(),
                buildGoalLabel(competition),
                buildMetricLabel(competition.getGoalType()),
                competition.getId(),
                entries,
                currentUserId
        );
    }

    public Competition acceptInvitation(Long competitionId, Long userId) {
        CompetitionParticipant participant = getParticipantOrThrow(competitionId, userId);
        participant.setStatus(CompetitionParticipantStatus.ACCEPTED);
        participant.setRespondedAt(LocalDateTime.now());
        return competitionRepository.findDetailedById(competitionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Competition not found with id " + competitionId));
    }

    public Competition declineInvitation(Long competitionId, Long userId) {
        CompetitionParticipant participant = getParticipantOrThrow(competitionId, userId);
        participant.setStatus(CompetitionParticipantStatus.DECLINED);
        participant.setRespondedAt(LocalDateTime.now());
        return competitionRepository.findDetailedById(competitionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Competition not found with id " + competitionId));
    }

    private CompetitionParticipant getParticipantOrThrow(Long competitionId, Long userId) {
        return competitionParticipantRepository.findByCompetitionIdAndUserId(competitionId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Competition participant not found."));
    }

    private Competition getCompetitionOrThrow(Long competitionId) {
        return competitionRepository.findDetailedById(competitionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Competition not found with id " + competitionId));
    }

    private CompetitionOverviewDto toOverview(Competition competition, Long currentUserId, Map<Long, RemoteUserDto> userMap) {
        CompetitionOverviewDto dto = new CompetitionOverviewDto();
        dto.setId(competition.getId());
        dto.setTitle(competition.getTitle());
        dto.setDescription(competition.getDescription());
        dto.setGoalType(competition.getGoalType().name());
        dto.setGoalLabel(buildGoalLabel(competition));
        dto.setExerciseName(competition.getExerciseName());
        dto.setTargetValue(competition.getTargetValue());
        dto.setPeriodMonths(competition.getPeriodMonths());
        dto.setCreatorId(competition.getCreatorId());
        dto.setCreatorName(formatUserName(userMap.get(competition.getCreatorId()), competition.getCreatorId()));
        dto.setCreatedByCurrentUser(Objects.equals(competition.getCreatorId(), currentUserId));
        dto.setAcceptedParticipantsCount((int) competition.getParticipants().stream()
                .filter(participant -> participant.getStatus() == CompetitionParticipantStatus.ACCEPTED)
                .count());
        dto.setPendingParticipantsCount((int) competition.getParticipants().stream()
                .filter(participant -> participant.getStatus() == CompetitionParticipantStatus.PENDING)
                .count());

        CompetitionParticipant currentParticipant = competition.getParticipants().stream()
                .filter(participant -> Objects.equals(participant.getUserId(), currentUserId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not part of the competition."));

        dto.setCurrentUserStatus(currentParticipant.getStatus().name());

        CompetitionMetricSnapshot snapshot = evaluateCompetition(competition, currentUserId);
        dto.setCurrentValue(snapshot.currentValue());
        dto.setProgressPercent(snapshot.progressPercent());
        dto.setTargetReached(snapshot.targetReached());

        if (snapshot.targetReached() && currentParticipant.getCompletedAt() == null) {
            currentParticipant.setCompletedAt(LocalDateTime.now());
        }
        return dto;
    }

    private CompetitionLeaderboardEntryDto buildMonthlyProgressLeaderboardEntry(
            RemoteUserDto user,
            Long currentUserId,
            LocalDate fromDate,
            LocalDate toDateExclusive
    ) {
        RemoteProgressSummaryDto summary = fetchUserProgressInRange(user.getId(), fromDate, toDateExclusive).getSummary();
        CompetitionLeaderboardEntryDto entry = new CompetitionLeaderboardEntryDto();
        entry.setUserId(user.getId());
        entry.setUserName(formatUserName(user, user.getId()));
        entry.setAvatarUrl(user.getAvatarUrl());
        entry.setScore(summary == null ? 0 : summary.getCompositeScore());
        entry.setCurrentValue(entry.getScore());
        entry.setProgressPercent(summary == null ? 0 : summary.getProgressPercent());
        entry.setGoalProgressPercent(0);
        entry.setCurrentUser(Objects.equals(user.getId(), currentUserId));
        entry.setSubtitle(String.format(Locale.US, "Прогресс за месяц %.1f%%", entry.getProgressPercent()));
        return entry;
    }

    private CompetitionLeaderboardEntryDto buildCompetitionEntry(
            Competition competition,
            Long userId,
            Long currentUserId,
            Map<Long, RemoteUserDto> userMap
    ) {
        RemoteUserDto user = userMap.get(userId);
        CompetitionMetricSnapshot snapshot = evaluateCompetition(competition, userId);

        CompetitionLeaderboardEntryDto entry = new CompetitionLeaderboardEntryDto();
        entry.setUserId(userId);
        entry.setUserName(formatUserName(user, userId));
        entry.setAvatarUrl(user == null ? null : user.getAvatarUrl());
        entry.setScore(snapshot.score());
        entry.setCurrentValue(snapshot.currentValue());
        entry.setTargetValue(competition.getTargetValue());
        entry.setGoalProgressPercent(snapshot.goalProgressPercent());
        entry.setProgressPercent(snapshot.progressPercent());
        entry.setCurrentUser(Objects.equals(userId, currentUserId));
        entry.setSubtitle(snapshot.subtitle());
        return entry;
    }

    private CompetitionLeaderboardResponse buildLeaderboardResponse(
            String scope,
            String title,
            String description,
            String goalType,
            String goalLabel,
            String metricLabel,
            Long competitionId,
            List<CompetitionLeaderboardEntryDto> entries,
            Long currentUserId
    ) {
        CompetitionLeaderboardResponse response = new CompetitionLeaderboardResponse();
        response.setScope(scope);
        response.setTitle(title);
        response.setDescription(description);
        response.setGoalType(goalType);
        response.setGoalLabel(goalLabel);
        response.setMetricLabel(metricLabel);
        response.setCompetitionId(competitionId);

        List<CompetitionLeaderboardEntryDto> rankedEntries = new ArrayList<>();
        Integer currentUserRank = null;
        for (int i = 0; i < entries.size(); i++) {
            CompetitionLeaderboardEntryDto entry = entries.get(i);
            entry.setRank(i + 1);
            rankedEntries.add(entry);
            if (Objects.equals(entry.getUserId(), currentUserId)) {
                currentUserRank = i + 1;
            }
        }

        response.setEntries(rankedEntries);
        response.setCurrentUserRank(currentUserRank);
        return response;
    }

    public CompetitionMetricSnapshot evaluateCompetition(Competition competition, Long userId) {
        int periodMonths = normalizePeriodMonths(competition.getPeriodMonths());

        if (competition.getGoalType() == CompetitionGoalType.PROGRESS_SCORE) {
            RemoteProgressSummaryDto summary = fetchUserProgress(userId, periodMonths).getSummary();
            double score = summary == null ? 0 : summary.getCompositeScore();
            double progressPercent = summary == null ? 0 : summary.getProgressPercent();
            double goalProgress = toGoalProgressPercent(score, competition.getTargetValue());
            return new CompetitionMetricSnapshot(
                    score,
                    score,
                    progressPercent,
                    goalProgress,
                    isTargetReached(score, competition.getTargetValue()),
                    String.format(Locale.US, "Интегральный прогресс %.1f%%", progressPercent)
            );
        }

        List<RemoteWorkoutDto> workouts = filterWorkoutsByPeriod(fetchUserWorkouts(userId), periodMonths);
        if (competition.getGoalType() == CompetitionGoalType.WORKOUT_COUNT) {
            double currentValue = workouts.size();
            return new CompetitionMetricSnapshot(
                    currentValue,
                    currentValue,
                    toGoalProgressPercent(currentValue, competition.getTargetValue()),
                    toGoalProgressPercent(currentValue, competition.getTargetValue()),
                    isTargetReached(currentValue, competition.getTargetValue()),
                    String.format(Locale.US, "%.0f тренировок за период", currentValue)
            );
        }

        double totalReps = workouts.stream()
                .flatMap(workout -> safeList(workout.getWorkoutExercises()).stream())
                .filter(workoutExercise -> workoutExercise.getExercise() != null
                        && Objects.equals(workoutExercise.getExercise().getId(), competition.getExerciseId()))
                .flatMap(workoutExercise -> safeList(workoutExercise.getSets()).stream())
                .mapToDouble(set -> set.getReps() == null ? 0 : set.getReps())
                .sum();

        return new CompetitionMetricSnapshot(
                totalReps,
                totalReps,
                toGoalProgressPercent(totalReps, competition.getTargetValue()),
                toGoalProgressPercent(totalReps, competition.getTargetValue()),
                isTargetReached(totalReps, competition.getTargetValue()),
                String.format(Locale.US, "%.0f повторений упражнения", totalReps)
        );
    }

    private List<RemoteWorkoutDto> filterWorkoutsByPeriod(List<RemoteWorkoutDto> workouts, int periodMonths) {
        LocalDateTime fromDate = LocalDateTime.now().minusMonths(periodMonths);
        return workouts.stream()
                .filter(workout -> workout.getWorkoutDate() != null && !workout.getWorkoutDate().isBefore(fromDate))
                .collect(Collectors.toList());
    }

    private String buildGoalLabel(Competition competition) {
        double targetValue = competition.getTargetValue() == null ? 0 : competition.getTargetValue();
        return switch (competition.getGoalType()) {
            case PROGRESS_SCORE ->
                    String.format(Locale.US, "Набрать %.0f баллов прогресса", targetValue);
            case EXERCISE_REPS ->
                    String.format(Locale.US, "Сделать %s %.0f повторений",
                            competition.getExerciseName() == null ? "упражнения" : competition.getExerciseName(),
                            targetValue);
            case WORKOUT_COUNT ->
                    String.format(Locale.US, "Сделать %.0f тренировок", targetValue);
        };
    }

    private String buildMetricLabel(CompetitionGoalType goalType) {
        return switch (goalType) {
            case PROGRESS_SCORE -> "Баллы прогресса";
            case EXERCISE_REPS -> "Повторения";
            case WORKOUT_COUNT -> "Тренировки";
        };
    }

    private void validateCreateRequest(CompetitionCreateRequest request) {
        if (request.getCreatorId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Competition creator id is required.");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Competition title is required.");
        }
        if (request.getGoalType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Competition goal type is required.");
        }
        if (request.getTargetValue() == null || request.getTargetValue() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Competition target value must be positive.");
        }
        if (request.getGoalType() == CompetitionGoalType.EXERCISE_REPS && request.getExerciseId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exercise id is required for exercise reps competition.");
        }
    }

    private void addParticipant(
            Competition competition,
            Long userId,
            CompetitionParticipantStatus status,
            LocalDateTime invitedAt
    ) {
        CompetitionParticipant participant = new CompetitionParticipant();
        participant.setCompetition(competition);
        participant.setUserId(userId);
        participant.setStatus(status);
        participant.setInvitedAt(invitedAt);
        if (status == CompetitionParticipantStatus.ACCEPTED) {
            participant.setRespondedAt(invitedAt);
        }
        competition.getParticipants().add(participant);
    }

    private int normalizePeriodMonths(Integer periodMonths) {
        if (periodMonths == null || periodMonths <= 0) {
            return DEFAULT_PERIOD_MONTHS;
        }
        return periodMonths;
    }

    private Set<Long> getAcceptedFriendIds(Long userId) {
        List<FriendRequest> approved = friendRequestRepository.findBySenderIdOrReceiverIdAndStatus(
                userId,
                userId,
                FriendRequestStatus.ACCEPTED
        );

        Set<Long> ids = new HashSet<>();
        for (FriendRequest request : approved) {
            if (Objects.equals(request.getSenderId(), userId)) {
                ids.add(request.getReceiverId());
            } else {
                ids.add(request.getSenderId());
            }
        }
        return ids;
    }

    private List<RemoteUserDto> fetchAllUsers() {
        RemoteUserDto[] response = restTemplate.getForObject("http://user-service/api/users", RemoteUserDto[].class);
        return response == null ? List.of() : Arrays.asList(response);
    }

    private RemoteWorkoutProgressResponse fetchUserProgress(Long userId, int months) {
        String url = "http://workout-service/api/workouts/progress/user/" + userId + "?months=" + months;
        RemoteWorkoutProgressResponse response = restTemplate.getForObject(url, RemoteWorkoutProgressResponse.class);
        if (response == null) {
            response = new RemoteWorkoutProgressResponse();
            response.setUserId(userId);
        }
        if (response.getSummary() == null) {
            response.setSummary(new RemoteProgressSummaryDto());
        }
        return response;
    }

    private RemoteWorkoutProgressResponse fetchUserProgressInRange(Long userId, LocalDate fromDate, LocalDate toDateExclusive) {
        String url = "http://workout-service/api/workouts/progress/user/" + userId
                + "/range?fromDate=" + fromDate + "&toDate=" + toDateExclusive;
        RemoteWorkoutProgressResponse response = restTemplate.getForObject(url, RemoteWorkoutProgressResponse.class);
        if (response == null) {
            response = new RemoteWorkoutProgressResponse();
            response.setUserId(userId);
        }
        if (response.getSummary() == null) {
            response.setSummary(new RemoteProgressSummaryDto());
        }
        return response;
    }

    private List<RemoteWorkoutDto> fetchUserWorkouts(Long userId) {
        RemoteWorkoutDto[] response = restTemplate.getForObject(
                "http://workout-service/api/workouts/user/" + userId,
                RemoteWorkoutDto[].class
        );
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

    private double toGoalProgressPercent(double currentValue, Double targetValue) {
        if (targetValue == null || targetValue <= 0) {
            return 0;
        }
        return Math.min(1000, (currentValue / targetValue) * 100.0);
    }

    private boolean isTargetReached(double currentValue, Double targetValue) {
        return targetValue != null && targetValue > 0 && currentValue >= targetValue;
    }

    private <T> List<T> safeList(List<T> items) {
        return items == null ? List.of() : items;
    }

    public record CompetitionMetricSnapshot(
            double score,
            double currentValue,
            double progressPercent,
            double goalProgressPercent,
            boolean targetReached,
            String subtitle
    ) {
    }
}
