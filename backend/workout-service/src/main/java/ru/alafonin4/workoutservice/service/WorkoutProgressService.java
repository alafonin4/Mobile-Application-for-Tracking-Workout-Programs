package ru.alafonin4.workoutservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.alafonin4.workoutservice.dto.ExerciseOptionDto;
import ru.alafonin4.workoutservice.dto.ExerciseProgressPointDto;
import ru.alafonin4.workoutservice.dto.ExerciseProgressResponse;
import ru.alafonin4.workoutservice.dto.MuscleGroupProgressDto;
import ru.alafonin4.workoutservice.dto.ProgressComponentDto;
import ru.alafonin4.workoutservice.dto.ProgressSummaryDto;
import ru.alafonin4.workoutservice.dto.TimelinePointDto;
import ru.alafonin4.workoutservice.dto.WorkoutProgressResponse;
import ru.alafonin4.workoutservice.model.Exercise;
import ru.alafonin4.workoutservice.model.ExerciseSet;
import ru.alafonin4.workoutservice.model.Workout;
import ru.alafonin4.workoutservice.model.WorkoutExercise;
import ru.alafonin4.workoutservice.repository.WorkoutRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkoutProgressService {

    private static final double TARGET_WORKOUTS_PER_WEEK = 3.0;
    private static final double TARGET_RECOVERY_DAYS = 2.5;
    private static final double EPSILON = 0.0001;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Transactional(readOnly = true)
    public WorkoutProgressResponse getUserProgress(Long userId, int months) {
        int normalizedMonths = normalizeMonths(months);
        LocalDateTime fromDate = LocalDateTime.now().minusMonths(normalizedMonths);
        List<Workout> workouts = workoutRepository
                .findByUserIdAndWorkoutDateGreaterThanEqualOrderByWorkoutDateAsc(userId, fromDate);

        return buildUserProgressResponse(
                userId,
                normalizedMonths,
                fromDate.toLocalDate(),
                LocalDate.now(),
                workouts
        );
    }

    @Transactional(readOnly = true)
    public WorkoutProgressResponse getUserProgressByRange(Long userId, LocalDate fromDate, LocalDate toDate) {
        LocalDate normalizedFrom = fromDate == null ? LocalDate.now().withDayOfMonth(1) : fromDate;
        LocalDate normalizedTo = toDate == null ? normalizedFrom.plusMonths(1) : toDate;
        if (!normalizedTo.isAfter(normalizedFrom)) {
            normalizedTo = normalizedFrom.plusDays(1);
        }

        List<Workout> workouts = workoutRepository
                .findByUserIdAndWorkoutDateGreaterThanEqualAndWorkoutDateLessThanOrderByWorkoutDateAsc(
                        userId,
                        normalizedFrom.atStartOfDay(),
                        normalizedTo.atStartOfDay()
                );

        return buildUserProgressResponse(
                userId,
                Math.max(1, (int) ChronoUnit.MONTHS.between(
                        normalizedFrom.withDayOfMonth(1),
                        normalizedTo.withDayOfMonth(1)
                )),
                normalizedFrom,
                normalizedTo.minusDays(1),
                workouts
        );
    }

    private WorkoutProgressResponse buildUserProgressResponse(
            Long userId,
            int periodMonths,
            LocalDate fromDate,
            LocalDate toDate,
            List<Workout> workouts
    ) {
        List<SessionMetrics> sessions = buildSessions(workouts);
        AggregatedMetrics aggregated = aggregateSessions(sessions);

        WorkoutProgressResponse response = new WorkoutProgressResponse();
        response.setUserId(userId);
        response.setPeriodMonths(periodMonths);
        response.setFromDate(fromDate.toString());
        response.setToDate(toDate.toString());
        response.setSummary(buildSummary(aggregated, sessions));
        response.setTimeline(buildTimeline(sessions));
        response.setMuscleGroupProgress(buildMuscleGroupProgress(aggregated));
        response.setExercises(buildExerciseOptions(aggregated));
        return response;
    }

    @Transactional(readOnly = true)
    public ExerciseProgressResponse getExerciseProgress(Long userId, Long exerciseId, int months) {
        int normalizedMonths = normalizeMonths(months);
        LocalDateTime fromDate = LocalDateTime.now().minusMonths(normalizedMonths);
        List<Workout> workouts = workoutRepository
                .findByUserIdAndWorkoutDateGreaterThanEqualOrderByWorkoutDateAsc(userId, fromDate);

        List<SessionMetrics> allSessions = buildSessions(workouts);
        List<MetricPoint> exercisePoints = new ArrayList<>();
        String exerciseName = null;
        String muscleGroup = null;
        Map<LocalDate, ExerciseProgressPointDto> timelineMap = new LinkedHashMap<>();

        for (SessionMetrics session : allSessions) {
            ExerciseAggregate aggregate = session.exerciseMetrics.get(exerciseId);
            if (aggregate == null) {
                continue;
            }

            exerciseName = aggregate.exerciseName;
            muscleGroup = aggregate.muscleGroup;

            MetricPoint point = new MetricPoint(
                    session.date,
                    aggregate.totalVolume,
                    aggregate.averageIntensity(),
                    aggregate.averageDensity(),
                    aggregate.peakEstimatedOneRepMax,
                    aggregate.personalRecord ? 1 : 0
            );
            exercisePoints.add(point);

            ExerciseProgressPointDto timelinePoint = timelineMap.computeIfAbsent(session.date, ignored -> {
                ExerciseProgressPointDto dto = new ExerciseProgressPointDto();
                dto.setDate(session.date.toString());
                return dto;
            });
            timelinePoint.setVolume(round(timelinePoint.getVolume() + aggregate.totalVolume));
            timelinePoint.setTotalReps(timelinePoint.getTotalReps() + aggregate.totalReps);
            timelinePoint.setMaxWeight(round(Math.max(timelinePoint.getMaxWeight(), aggregate.maxWeight)));
        }

        if (exerciseName == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise progress not found for exercise id " + exerciseId);
        }

        CompositeScore compositeScore = calculateExerciseCompositeScore(exercisePoints);

        ExerciseProgressResponse response = new ExerciseProgressResponse();
        response.setUserId(userId);
        response.setExerciseId(exerciseId);
        response.setExerciseName(exerciseName);
        response.setMuscleGroup(muscleGroup);
        response.setPeriodMonths(normalizedMonths);
        response.setTimeline(timelineMap.values().stream()
                .sorted(Comparator.comparing(ExerciseProgressPointDto::getDate))
                .toList());

        double totalVolume = exercisePoints.stream().mapToDouble(point -> point.volume).sum();
        int totalReps = (int) Math.round(exercisePoints.stream().mapToDouble(point -> point.totalRepsProxy()).sum());
        double maxWeight = response.getTimeline().stream()
                .mapToDouble(ExerciseProgressPointDto::getMaxWeight)
                .max()
                .orElse(0);

        response.setTotalVolume(round(totalVolume));
        response.setTotalReps(totalReps);
        response.setMaxWeight(round(maxWeight));
        response.setAverageIntensity(round(average(exercisePoints.stream()
                .map(point -> point.intensity)
                .collect(Collectors.toList()))));
        response.setAverageDensity(round(average(exercisePoints.stream()
                .map(point -> point.density)
                .collect(Collectors.toList()))));
        response.setEstimatedOneRepMax(round(exercisePoints.stream()
                .mapToDouble(point -> point.peakEstimatedOneRepMax)
                .max()
                .orElse(0)));
        response.setVolumeTrendPercent(round(compositeScore.volumeTrendPercent));
        response.setIntensityTrendPercent(round(compositeScore.intensityTrendPercent));
        response.setDensityTrendPercent(round(compositeScore.densityTrendPercent));
        response.setPersonalRecordScore(round(compositeScore.personalRecordScore));
        response.setCompositeScore(round(compositeScore.absoluteScore));
        response.setProgressPercent(round(compositeScore.relativeProgressPercent));
        return response;
    }

    private ProgressSummaryDto buildSummary(AggregatedMetrics aggregated, List<SessionMetrics> sessions) {
        ProgressSummaryDto summary = new ProgressSummaryDto();
        summary.setWorkoutsCount(sessions.size());
        summary.setExercisesCount(aggregated.totalExerciseOccurrences);
        summary.setTotalReps(aggregated.totalReps);
        summary.setTotalSets(aggregated.totalSets);
        summary.setTotalVolume(round(aggregated.totalVolume));
        summary.setMaxWeight(round(aggregated.maxWeight));
        summary.setAverageSessionVolume(round(aggregated.averageSessionVolume()));
        summary.setAverageIntensity(round(aggregated.averageIntensity()));
        summary.setAverageDensity(round(aggregated.averageDensity()));
        summary.setPeakEstimatedOneRepMax(round(aggregated.peakEstimatedOneRepMax));

        CompositeScore compositeScore = calculateOverallCompositeScore(aggregated, sessions);

        summary.setVolumeTrendPercent(round(compositeScore.volumeTrendPercent));
        summary.setIntensityTrendPercent(round(compositeScore.intensityTrendPercent));
        summary.setDensityTrendPercent(round(compositeScore.densityTrendPercent));
        summary.setConsistencyScore(round(compositeScore.consistencyScore));
        summary.setRecoveryScore(round(compositeScore.recoveryScore));
        summary.setBalanceScore(round(compositeScore.balanceScore));
        summary.setDiversityScore(round(compositeScore.diversityScore));
        summary.setPersonalRecordScore(round(compositeScore.personalRecordScore));
        summary.setCompositeScore(round(compositeScore.absoluteScore));
        summary.setProgressPercent(round(compositeScore.relativeProgressPercent));
        summary.setComponents(compositeScore.components);
        return summary;
    }

    private List<MuscleGroupProgressDto> buildMuscleGroupProgress(AggregatedMetrics aggregated) {
        double maxVolume = aggregated.muscleGroups.values().stream()
                .mapToDouble(group -> group.totalVolume)
                .max()
                .orElse(0);

        List<MuscleGroupProgressDto> result = new ArrayList<>();
        for (Map.Entry<String, GroupAggregate> entry : aggregated.muscleGroups.entrySet()) {
            GroupAggregate group = entry.getValue();
            CompositeScore score = calculateGroupCompositeScore(group);

            MuscleGroupProgressDto dto = new MuscleGroupProgressDto();
            dto.setMuscleGroup(entry.getKey());
            dto.setTotalVolume(round(group.totalVolume));
            dto.setTotalSets(group.totalSets);
            dto.setTotalReps(group.totalReps);
            dto.setAverageIntensity(round(group.averageIntensity()));
            dto.setPeakEstimatedOneRepMax(round(group.peakEstimatedOneRepMax));
            dto.setProgressPercent(round(score.relativeProgressPercent));
            dto.setCompositeScore(round(score.absoluteScore));
            dto.setNormalizedScore(maxVolume == 0 ? 0 : round((group.totalVolume / maxVolume) * 100));
            result.add(dto);
        }

        result.sort(Comparator.comparing(MuscleGroupProgressDto::getMuscleGroup));
        return result;
    }

    private List<ExerciseOptionDto> buildExerciseOptions(AggregatedMetrics aggregated) {
        return aggregated.exerciseOptions.values().stream()
                .sorted(Comparator.comparing(ExerciseOptionDto::getExerciseName))
                .toList();
    }

    private List<TimelinePointDto> buildTimeline(List<SessionMetrics> sessions) {
        Map<LocalDate, TimelineAccumulator> accumulators = new LinkedHashMap<>();

        for (SessionMetrics session : sessions) {
            TimelineAccumulator accumulator = accumulators.computeIfAbsent(session.date, ignored -> new TimelineAccumulator());
            accumulator.overallVolume += session.totalVolume;
            for (Map.Entry<String, GroupAggregate> entry : session.groupMetrics.entrySet()) {
                accumulator.muscleGroupVolumes.merge(entry.getKey(), entry.getValue().totalVolume, Double::sum);
            }
        }

        List<TimelinePointDto> timeline = new ArrayList<>();
        for (Map.Entry<LocalDate, TimelineAccumulator> entry : accumulators.entrySet()) {
            TimelinePointDto point = new TimelinePointDto();
            point.setDate(entry.getKey().toString());
            point.setOverallVolume(round(entry.getValue().overallVolume));

            Map<String, Double> groupMap = new LinkedHashMap<>();
            for (Map.Entry<String, Double> groupEntry : entry.getValue().muscleGroupVolumes.entrySet()) {
                groupMap.put(groupEntry.getKey(), round(groupEntry.getValue()));
            }
            point.setMuscleGroupVolumes(groupMap);
            timeline.add(point);
        }
        return timeline;
    }

    private List<SessionMetrics> buildSessions(List<Workout> workouts) {
        List<SessionMetrics> sessions = new ArrayList<>();
        Map<Long, Double> exerciseHistoricalPr = new HashMap<>();

        for (Workout workout : workouts) {
            SessionMetrics session = new SessionMetrics();
            session.date = workout.getWorkoutDate() == null ? LocalDate.now() : workout.getWorkoutDate().toLocalDate();

            for (WorkoutExercise workoutExercise : workout.getWorkoutExercises()) {
                Exercise exercise = workoutExercise.getExercise();
                if (exercise == null) {
                    continue;
                }

                String muscleGroup = normalizeMuscleGroup(exercise.getMuscleGroup());
                ExerciseAggregate exerciseAggregate = session.exerciseMetrics.computeIfAbsent(
                        exercise.getId(),
                        ignored -> new ExerciseAggregate(exercise.getId(), exercise.getName(), muscleGroup)
                );
                GroupAggregate groupAggregate = session.groupMetrics.computeIfAbsent(
                        muscleGroup,
                        ignored -> new GroupAggregate(muscleGroup)
                );

                for (ExerciseSet set : workoutExercise.getSets()) {
                    int reps = safeInt(set.getReps());
                    double weight = safeDouble(set.getWeight());
                    double volume = reps * weight;
                    double estimatedOneRepMax = calculateEstimatedOneRepMax(weight, reps);

                    session.totalVolume += volume;
                    session.totalReps += reps;
                    session.totalSets += 1;
                    session.maxWeight = Math.max(session.maxWeight, weight);
                    session.peakEstimatedOneRepMax = Math.max(session.peakEstimatedOneRepMax, estimatedOneRepMax);

                    exerciseAggregate.addSet(reps, weight, volume, estimatedOneRepMax);
                    groupAggregate.addSet(reps, weight, volume, estimatedOneRepMax);
                }

                double previousPr = exerciseHistoricalPr.getOrDefault(exercise.getId(), 0.0);
                if (exerciseAggregate.peakEstimatedOneRepMax > previousPr * 1.01) {
                    exerciseAggregate.personalRecord = true;
                    session.personalRecordCount += 1;
                    exerciseHistoricalPr.put(exercise.getId(), exerciseAggregate.peakEstimatedOneRepMax);
                } else {
                    exerciseHistoricalPr.put(exercise.getId(), Math.max(previousPr, exerciseAggregate.peakEstimatedOneRepMax));
                }
            }

            sessions.add(session);
        }

        sessions.sort(Comparator.comparing(session -> session.date));
        return sessions;
    }

    private AggregatedMetrics aggregateSessions(List<SessionMetrics> sessions) {
        AggregatedMetrics aggregated = new AggregatedMetrics();

        for (SessionMetrics session : sessions) {
            aggregated.totalVolume += session.totalVolume;
            aggregated.totalReps += session.totalReps;
            aggregated.totalSets += session.totalSets;
            aggregated.maxWeight = Math.max(aggregated.maxWeight, session.maxWeight);
            aggregated.peakEstimatedOneRepMax = Math.max(aggregated.peakEstimatedOneRepMax, session.peakEstimatedOneRepMax);
            aggregated.personalRecordCount += session.personalRecordCount;

            for (Map.Entry<String, GroupAggregate> entry : session.groupMetrics.entrySet()) {
                GroupAggregate target = aggregated.muscleGroups.computeIfAbsent(
                        entry.getKey(),
                        ignored -> new GroupAggregate(entry.getKey())
                );
                target.merge(entry.getValue(), session.date);
            }

            for (Map.Entry<Long, ExerciseAggregate> entry : session.exerciseMetrics.entrySet()) {
                ExerciseAggregate target = aggregated.exercises.computeIfAbsent(
                        entry.getKey(),
                        ignored -> new ExerciseAggregate(
                                entry.getValue().exerciseId,
                                entry.getValue().exerciseName,
                                entry.getValue().muscleGroup
                        )
                );
                target.merge(entry.getValue());
                aggregated.exerciseOptions.putIfAbsent(
                        entry.getKey(),
                        new ExerciseOptionDto(entry.getKey(), entry.getValue().exerciseName, entry.getValue().muscleGroup)
                );
                aggregated.totalExerciseOccurrences += 1;
            }
        }

        return aggregated;
    }

    private CompositeScore calculateOverallCompositeScore(AggregatedMetrics aggregated, List<SessionMetrics> sessions) {
        CompositeScore score = new CompositeScore();
        if (sessions.isEmpty()) {
            return score;
        }

        List<MetricPoint> metricPoints = sessions.stream()
                .map(session -> new MetricPoint(
                        session.date,
                        session.totalVolume,
                        session.averageIntensity(),
                        session.averageDensity(),
                        session.peakEstimatedOneRepMax,
                        session.personalRecordCount
                ))
                .toList();

        TrendProfile profile = buildTrendProfile(metricPoints);
        score.volumeTrendPercent = profile.volumeTrendPercent;
        score.intensityTrendPercent = profile.intensityTrendPercent;
        score.densityTrendPercent = profile.densityTrendPercent;

        List<ProgressComponentDto> components = new ArrayList<>();
        components.add(createComponent(
                "volume",
                "Объём нагрузки",
                0.24,
                profile.baselineVolume,
                profile.currentVolume,
                profile.volumeTrendPercent,
                growthScore(profile.volumeTrendPercent, 35),
                "Отражает рост среднего тренировочного объёма между базовым и текущим окнами."
        ));
        components.add(createComponent(
                "intensity",
                "Интенсивность",
                0.18,
                profile.baselineIntensity,
                profile.currentIntensity,
                profile.intensityTrendPercent,
                growthScore(profile.intensityTrendPercent, 20),
                "Оценивает рост средней интенсивности по расчётному 1ПМ и удельной нагрузке."
        ));
        components.add(createComponent(
                "density",
                "Плотность",
                0.12,
                profile.baselineDensity,
                profile.currentDensity,
                profile.densityTrendPercent,
                growthScore(profile.densityTrendPercent, 25),
                "Показывает, насколько больше работы выполняется в одном подходе."
        ));

        double consistencyScore = calculateConsistencyScore(sessions);
        double recoveryScore = calculateRecoveryScore(sessions);
        double balanceScore = calculateBalanceScore(aggregated);
        double diversityScore = calculateDiversityScore(aggregated, sessions.size());
        double personalRecordScore = calculatePersonalRecordScore(aggregated.personalRecordCount, aggregated.totalExerciseOccurrences);

        score.consistencyScore = consistencyScore;
        score.recoveryScore = recoveryScore;
        score.balanceScore = balanceScore;
        score.diversityScore = diversityScore;
        score.personalRecordScore = personalRecordScore;

        components.add(createStaticComponent(
                "consistency",
                "Регулярность",
                0.14,
                consistencyScore,
                "Учитывает среднее число тренировок в неделю и равномерность недельного распределения."
        ));
        components.add(createStaticComponent(
                "recovery",
                "Восстановление",
                0.10,
                recoveryScore,
                "Оценивает адекватность интервалов отдыха между тренировками."
        ));
        components.add(createStaticComponent(
                "balance",
                "Баланс мышечных групп",
                0.10,
                balanceScore,
                "Показывает, насколько равномерно распределён тренировочный объём по мышечным группам."
        ));
        components.add(createStaticComponent(
                "diversity",
                "Вариативность",
                0.06,
                diversityScore,
                "Оценивает разнообразие используемых упражнений и ширину двигательного профиля."
        ));
        components.add(createStaticComponent(
                "pr",
                "Персональные рекорды",
                0.06,
                personalRecordScore,
                "Фиксирует частоту появления новых локальных рекордов силы."
        ));

        score.absoluteScore = round(weightedComponentScore(components));
        score.relativeProgressPercent = round(
                0.45 * score.volumeTrendPercent +
                0.35 * score.intensityTrendPercent +
                0.20 * score.densityTrendPercent
        );
        score.components = components;
        return score;
    }

    private CompositeScore calculateGroupCompositeScore(GroupAggregate group) {
        List<MetricPoint> points = group.timeline.entrySet().stream()
                .map(entry -> new MetricPoint(
                        entry.getKey(),
                        entry.getValue().volume,
                        entry.getValue().intensity,
                        entry.getValue().density,
                        entry.getValue().peakEstimatedOneRepMax,
                        entry.getValue().personalRecords
                ))
                .sorted(Comparator.comparing(point -> point.date))
                .toList();

        TrendProfile profile = buildTrendProfile(points);
        CompositeScore score = new CompositeScore();
        score.volumeTrendPercent = profile.volumeTrendPercent;
        score.intensityTrendPercent = profile.intensityTrendPercent;
        score.densityTrendPercent = profile.densityTrendPercent;
        score.personalRecordScore = calculatePersonalRecordScore(group.personalRecordCount, Math.max(group.trainingDaysCount, 1));
        score.absoluteScore = round(
                growthScore(profile.volumeTrendPercent, 35) * 0.45 +
                growthScore(profile.intensityTrendPercent, 20) * 0.30 +
                growthScore(profile.densityTrendPercent, 25) * 0.15 +
                score.personalRecordScore * 0.10
        );
        score.relativeProgressPercent = round(
                0.5 * score.volumeTrendPercent +
                0.3 * score.intensityTrendPercent +
                0.2 * score.densityTrendPercent
        );
        return score;
    }

    private CompositeScore calculateExerciseCompositeScore(List<MetricPoint> points) {
        CompositeScore score = new CompositeScore();
        TrendProfile profile = buildTrendProfile(points);
        score.volumeTrendPercent = profile.volumeTrendPercent;
        score.intensityTrendPercent = profile.intensityTrendPercent;
        score.densityTrendPercent = profile.densityTrendPercent;
        score.personalRecordScore = calculatePersonalRecordScore(
                (int) points.stream().filter(point -> point.personalRecords > 0).count(),
                points.size()
        );
        score.absoluteScore = round(
                growthScore(profile.volumeTrendPercent, 35) * 0.30 +
                growthScore(profile.intensityTrendPercent, 18) * 0.40 +
                growthScore(profile.densityTrendPercent, 25) * 0.15 +
                score.personalRecordScore * 0.15
        );
        score.relativeProgressPercent = round(
                0.35 * score.volumeTrendPercent +
                0.45 * score.intensityTrendPercent +
                0.20 * score.densityTrendPercent
        );
        return score;
    }

    private TrendProfile buildTrendProfile(List<MetricPoint> points) {
        TrendProfile profile = new TrendProfile();
        if (points.isEmpty()) {
            return profile;
        }

        int splitSize = Math.max(1, points.size() / 3);
        List<MetricPoint> baseline = points.subList(0, splitSize);
        List<MetricPoint> current = points.subList(points.size() - splitSize, points.size());

        profile.baselineVolume = average(pointsVolume(baseline));
        profile.currentVolume = average(pointsVolume(current));
        profile.baselineIntensity = average(pointsIntensity(baseline));
        profile.currentIntensity = average(pointsIntensity(current));
        profile.baselineDensity = average(pointsDensity(baseline));
        profile.currentDensity = average(pointsDensity(current));

        profile.volumeTrendPercent = calculateRelativeChangePercent(profile.baselineVolume, profile.currentVolume);
        profile.intensityTrendPercent = calculateRelativeChangePercent(profile.baselineIntensity, profile.currentIntensity);
        profile.densityTrendPercent = calculateRelativeChangePercent(profile.baselineDensity, profile.currentDensity);
        return profile;
    }

    private double calculateConsistencyScore(List<SessionMetrics> sessions) {
        if (sessions.isEmpty()) {
            return 0;
        }

        LocalDate first = sessions.get(0).date;
        LocalDate last = sessions.get(sessions.size() - 1).date;
        long days = Math.max(1, ChronoUnit.DAYS.between(first, last) + 1);
        double weeks = Math.max(1.0, days / 7.0);
        double actualWorkoutsPerWeek = sessions.size() / weeks;

        double frequencyAdherence = 1 - Math.min(Math.abs(actualWorkoutsPerWeek - TARGET_WORKOUTS_PER_WEEK)
                / TARGET_WORKOUTS_PER_WEEK, 1.0);

        Map<Long, Integer> weeklyBuckets = new LinkedHashMap<>();
        for (SessionMetrics session : sessions) {
            long weekIndex = ChronoUnit.DAYS.between(first, session.date) / 7;
            weeklyBuckets.merge(weekIndex, 1, Integer::sum);
        }

        List<Double> weeklyCounts = weeklyBuckets.values().stream()
                .map(Integer::doubleValue)
                .toList();
        double weeklyCv = coefficientOfVariation(weeklyCounts);
        double regularity = clamp(1 - Math.min(weeklyCv, 1), 0, 1);

        return round((0.6 * frequencyAdherence + 0.4 * regularity) * 100);
    }

    private double calculateRecoveryScore(List<SessionMetrics> sessions) {
        if (sessions.size() < 2) {
            return 50;
        }

        List<Double> intervals = new ArrayList<>();
        for (int i = 1; i < sessions.size(); i++) {
            long diff = ChronoUnit.DAYS.between(sessions.get(i - 1).date, sessions.get(i).date);
            intervals.add((double) Math.max(1, diff));
        }

        double averageInterval = average(intervals);
        double stdInterval = standardDeviation(intervals);
        double spacingScore = clamp(1 - Math.abs(averageInterval - TARGET_RECOVERY_DAYS) / TARGET_RECOVERY_DAYS, 0, 1);
        double variabilityScore = clamp(1 - Math.min(stdInterval / TARGET_RECOVERY_DAYS, 1), 0, 1);

        return round((0.65 * spacingScore + 0.35 * variabilityScore) * 100);
    }

    private double calculateBalanceScore(AggregatedMetrics aggregated) {
        if (aggregated.muscleGroups.isEmpty()) {
            return 0;
        }

        double totalVolume = aggregated.muscleGroups.values().stream()
                .mapToDouble(group -> group.totalVolume)
                .sum();
        if (totalVolume <= 0) {
            return 0;
        }

        double entropy = 0;
        for (GroupAggregate group : aggregated.muscleGroups.values()) {
            double p = group.totalVolume / totalVolume;
            if (p > 0) {
                entropy -= p * Math.log(p);
            }
        }

        double maxEntropy = Math.log(aggregated.muscleGroups.size());
        double entropyNormalized = maxEntropy == 0 ? 1 : entropy / maxEntropy;
        double coverage = clamp(aggregated.muscleGroups.size() / 8.0, 0, 1);

        return round((0.8 * entropyNormalized + 0.2 * coverage) * 100);
    }

    private double calculateDiversityScore(AggregatedMetrics aggregated, int workoutsCount) {
        if (workoutsCount == 0) {
            return 0;
        }

        double distinctExercisesScore = 1 - Math.exp(-(double) aggregated.exercises.size() / 8.0);
        double exercisesPerWorkout = (double) aggregated.totalExerciseOccurrences / workoutsCount;
        double exercisesPerWorkoutScore = clamp(exercisesPerWorkout / 6.0, 0, 1);

        return round((0.7 * distinctExercisesScore + 0.3 * exercisesPerWorkoutScore) * 100);
    }

    private double calculatePersonalRecordScore(int prCount, int exposureCount) {
        if (exposureCount <= 0) {
            return 0;
        }

        double prRate = (double) prCount / exposureCount;
        return round(clamp(prRate / 0.35, 0, 1) * 100);
    }

    private ProgressComponentDto createComponent(
            String code,
            String title,
            double weight,
            double baselineValue,
            double currentValue,
            double trendPercent,
            double score,
            String description
    ) {
        ProgressComponentDto component = new ProgressComponentDto();
        component.setCode(code);
        component.setTitle(title);
        component.setWeight(weight);
        component.setBaselineValue(round(baselineValue));
        component.setCurrentValue(round(currentValue));
        component.setTrendPercent(round(trendPercent));
        component.setScore(round(score));
        component.setDescription(description);
        return component;
    }

    private ProgressComponentDto createStaticComponent(
            String code,
            String title,
            double weight,
            double score,
            String description
    ) {
        ProgressComponentDto component = new ProgressComponentDto();
        component.setCode(code);
        component.setTitle(title);
        component.setWeight(weight);
        component.setScore(round(score));
        component.setDescription(description);
        return component;
    }

    private double weightedComponentScore(List<ProgressComponentDto> components) {
        double result = 0;
        for (ProgressComponentDto component : components) {
            result += component.getScore() * component.getWeight();
        }
        return result;
    }

    private double growthScore(double percentGrowth, double sensitivity) {
        double normalized = Math.tanh(percentGrowth / sensitivity);
        return clamp(50 + 50 * normalized, 0, 100);
    }

    private double calculateRelativeChangePercent(double baseline, double current) {
        if (Math.abs(baseline) < EPSILON) {
            return current > 0 ? 100 : 0;
        }
        return round(((current - baseline) / baseline) * 100);
    }

    private double calculateEstimatedOneRepMax(double weight, int reps) {
        if (weight <= 0) {
            return 0;
        }
        return weight * (1 + reps / 30.0);
    }

    private List<Double> pointsVolume(List<MetricPoint> points) {
        return points.stream().map(point -> point.volume).toList();
    }

    private List<Double> pointsIntensity(List<MetricPoint> points) {
        return points.stream().map(point -> point.intensity).toList();
    }

    private List<Double> pointsDensity(List<MetricPoint> points) {
        return points.stream().map(point -> point.density).toList();
    }

    private int normalizeMonths(int months) {
        return months <= 0 ? 1 : months;
    }

    private String normalizeMuscleGroup(String muscleGroup) {
        if (muscleGroup == null || muscleGroup.isBlank()) {
            return "Other";
        }
        return muscleGroup;
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private double safeDouble(Double value) {
        return value == null ? 0 : value;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private double average(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return 0;
        }

        double sum = 0;
        for (Double value : values) {
            sum += value == null ? 0 : value;
        }
        return sum / values.size();
    }

    private double coefficientOfVariation(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return 0;
        }
        double mean = average(values);
        if (Math.abs(mean) < EPSILON) {
            return 0;
        }
        return standardDeviation(values) / mean;
    }

    private double standardDeviation(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return 0;
        }
        double mean = average(values);
        double sum = 0;
        for (Double value : values) {
            double v = value == null ? 0 : value;
            sum += Math.pow(v - mean, 2);
        }
        return Math.sqrt(sum / values.size());
    }

    private static class CompositeScore {
        private double volumeTrendPercent;
        private double intensityTrendPercent;
        private double densityTrendPercent;
        private double consistencyScore;
        private double recoveryScore;
        private double balanceScore;
        private double diversityScore;
        private double personalRecordScore;
        private double absoluteScore;
        private double relativeProgressPercent;
        private List<ProgressComponentDto> components = new ArrayList<>();
    }

    private static class TrendProfile {
        private double baselineVolume;
        private double currentVolume;
        private double baselineIntensity;
        private double currentIntensity;
        private double baselineDensity;
        private double currentDensity;
        private double volumeTrendPercent;
        private double intensityTrendPercent;
        private double densityTrendPercent;
    }

    private static class TimelineAccumulator {
        private double overallVolume;
        private final Map<String, Double> muscleGroupVolumes = new LinkedHashMap<>();
    }

    private static class MetricPoint {
        private final LocalDate date;
        private final double volume;
        private final double intensity;
        private final double density;
        private final double peakEstimatedOneRepMax;
        private final int personalRecords;

        private MetricPoint(
                LocalDate date,
                double volume,
                double intensity,
                double density,
                double peakEstimatedOneRepMax,
                int personalRecords
        ) {
            this.date = date;
            this.volume = volume;
            this.intensity = intensity;
            this.density = density;
            this.peakEstimatedOneRepMax = peakEstimatedOneRepMax;
            this.personalRecords = personalRecords;
        }

        private double totalRepsProxy() {
            return intensity <= 0 ? 0 : volume / intensity;
        }
    }

    private static class AggregatedMetrics {
        private double totalVolume;
        private int totalReps;
        private int totalSets;
        private double maxWeight;
        private double peakEstimatedOneRepMax;
        private int personalRecordCount;
        private int totalExerciseOccurrences;
        private final Map<String, GroupAggregate> muscleGroups = new LinkedHashMap<>();
        private final Map<Long, ExerciseAggregate> exercises = new LinkedHashMap<>();
        private final Map<Long, ExerciseOptionDto> exerciseOptions = new LinkedHashMap<>();

        private double averageSessionVolume() {
            int sessionCount = Math.max(1, maxTimelineSize());
            return totalVolume / sessionCount;
        }

        private double averageIntensity() {
            return totalReps == 0 ? 0 : totalVolume / totalReps;
        }

        private double averageDensity() {
            return totalSets == 0 ? 0 : totalVolume / totalSets;
        }

        private int maxTimelineSize() {
            return exercises.values().stream()
                    .mapToInt(exercise -> Math.max(1, exercise.sessionCount))
                    .max()
                    .orElse(1);
        }
    }

    private static class SessionMetrics {
        private LocalDate date;
        private double totalVolume;
        private int totalReps;
        private int totalSets;
        private double maxWeight;
        private double peakEstimatedOneRepMax;
        private int personalRecordCount;
        private final Map<String, GroupAggregate> groupMetrics = new LinkedHashMap<>();
        private final Map<Long, ExerciseAggregate> exerciseMetrics = new LinkedHashMap<>();

        private double averageIntensity() {
            return totalReps == 0 ? 0 : totalVolume / totalReps;
        }

        private double averageDensity() {
            return totalSets == 0 ? 0 : totalVolume / totalSets;
        }
    }

    private static class TimelineValue {
        private double volume;
        private double intensity;
        private double density;
        private double peakEstimatedOneRepMax;
        private int personalRecords;
    }

    private static class GroupAggregate {
        private final String muscleGroup;
        private double totalVolume;
        private int totalReps;
        private int totalSets;
        private double peakEstimatedOneRepMax;
        private int personalRecordCount;
        private int trainingDaysCount;
        private final Map<LocalDate, TimelineValue> timeline = new LinkedHashMap<>();

        private GroupAggregate(String muscleGroup) {
            this.muscleGroup = muscleGroup;
        }

        private void addSet(int reps, double weight, double volume, double estimatedOneRepMax) {
            totalVolume += volume;
            totalReps += reps;
            totalSets += 1;
            peakEstimatedOneRepMax = Math.max(peakEstimatedOneRepMax, estimatedOneRepMax);
        }

        private void merge(GroupAggregate other, LocalDate date) {
            totalVolume += other.totalVolume;
            totalReps += other.totalReps;
            totalSets += other.totalSets;
            peakEstimatedOneRepMax = Math.max(peakEstimatedOneRepMax, other.peakEstimatedOneRepMax);
            personalRecordCount += other.personalRecordCount;
            trainingDaysCount += 1;

            TimelineValue value = timeline.computeIfAbsent(date, ignored -> new TimelineValue());
            value.volume += other.totalVolume;
            value.intensity = other.averageIntensity();
            value.density = other.averageDensity();
            value.peakEstimatedOneRepMax = Math.max(value.peakEstimatedOneRepMax, other.peakEstimatedOneRepMax);
            value.personalRecords += other.personalRecordCount;
        }

        private double averageIntensity() {
            return totalReps == 0 ? 0 : totalVolume / totalReps;
        }

        private double averageDensity() {
            return totalSets == 0 ? 0 : totalVolume / totalSets;
        }
    }

    private static class ExerciseAggregate {
        private final Long exerciseId;
        private final String exerciseName;
        private final String muscleGroup;
        private double totalVolume;
        private int totalReps;
        private int totalSets;
        private double maxWeight;
        private double peakEstimatedOneRepMax;
        private boolean personalRecord;
        private int sessionCount;

        private ExerciseAggregate(Long exerciseId, String exerciseName, String muscleGroup) {
            this.exerciseId = exerciseId;
            this.exerciseName = exerciseName;
            this.muscleGroup = muscleGroup;
        }

        private void addSet(int reps, double weight, double volume, double estimatedOneRepMax) {
            totalVolume += volume;
            totalReps += reps;
            totalSets += 1;
            maxWeight = Math.max(maxWeight, weight);
            peakEstimatedOneRepMax = Math.max(peakEstimatedOneRepMax, estimatedOneRepMax);
        }

        private void merge(ExerciseAggregate other) {
            totalVolume += other.totalVolume;
            totalReps += other.totalReps;
            totalSets += other.totalSets;
            maxWeight = Math.max(maxWeight, other.maxWeight);
            peakEstimatedOneRepMax = Math.max(peakEstimatedOneRepMax, other.peakEstimatedOneRepMax);
            sessionCount += 1;
            if (other.personalRecord) {
                personalRecord = true;
            }
        }

        private double averageIntensity() {
            return totalReps == 0 ? 0 : totalVolume / totalReps;
        }

        private double averageDensity() {
            return totalSets == 0 ? 0 : totalVolume / totalSets;
        }
    }
}
