package ru.alafonin4.workoutservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.alafonin4.workoutservice.dto.AchievementDto;
import ru.alafonin4.workoutservice.dto.MuscleBalanceDto;
import ru.alafonin4.workoutservice.dto.PersonalRecordDto;
import ru.alafonin4.workoutservice.dto.PersonalizationProfileResponse;
import ru.alafonin4.workoutservice.dto.ProgramAdaptationResponse;
import ru.alafonin4.workoutservice.dto.ProgramAdaptationSuggestionDto;
import ru.alafonin4.workoutservice.dto.SmartReminderDto;
import ru.alafonin4.workoutservice.model.ExerciseSet;
import ru.alafonin4.workoutservice.model.TrainingDay;
import ru.alafonin4.workoutservice.model.TrainingDayExercise;
import ru.alafonin4.workoutservice.model.TrainingProgram;
import ru.alafonin4.workoutservice.model.Workout;
import ru.alafonin4.workoutservice.model.WorkoutExercise;
import ru.alafonin4.workoutservice.repository.TrainingProgramRepository;
import ru.alafonin4.workoutservice.repository.WorkoutRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class PersonalizationService {

    private static final List<String> BASE_MUSCLE_GROUPS = List.of(
            "Грудь",
            "Спина",
            "Ноги",
            "Плечи",
            "Бицепс",
            "Трицепс",
            "Пресс",
            "Other"
    );

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private TrainingProgramRepository trainingProgramRepository;

    public PersonalizationProfileResponse buildProfile(Long userId) {
        List<Workout> workouts = workoutRepository.findByUserIdOrderByWorkoutDateAsc(userId);
        AchievementMilestones milestones = determineAchievementMilestones(workouts);
        List<AchievementDto> achievements = buildAchievements(workouts, milestones);
        List<PersonalRecordDto> personalRecords = buildPersonalRecords(workouts);
        List<MuscleBalanceDto> muscleBalance = buildMuscleBalance(workouts);
        int recoveryScore = calculateRecoveryScore(workouts);
        List<SmartReminderDto> smartReminders = buildSmartReminders(workouts, recoveryScore, muscleBalance);

        PersonalizationProfileResponse response = new PersonalizationProfileResponse();
        response.setUserId(userId);
        response.setAchievements(achievements);
        response.setPersonalRecords(personalRecords);
        response.setMuscleBalance(muscleBalance);
        response.setRecoveryScore(recoveryScore);
        response.setRecoveryStatus(buildRecoveryStatus(recoveryScore));
        response.setSmartReminders(smartReminders);
        response.setUnlockedAchievementsCount((int) achievements.stream().filter(AchievementDto::isUnlocked).count());
        response.setTotalAchievementsCount(achievements.size());
        response.setProfileMessage(buildProfileMessage(workouts, achievements, recoveryScore, smartReminders));
        return response;
    }

    public ProgramAdaptationResponse buildProgramAdaptation(Long userId, Long programId) {
        TrainingProgram program = trainingProgramRepository.findById(programId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Training program not found with id " + programId));

        if (!Objects.equals(program.getUserId(), userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This training program does not belong to the selected user.");
        }

        List<Workout> workouts = workoutRepository.findByUserIdOrderByWorkoutDateAsc(userId);
        Map<Long, List<ExercisePerformanceSnapshot>> performanceByExercise = buildExercisePerformanceMap(workouts);
        Integer daysSinceLastWorkout = workouts.isEmpty()
                ? null
                : (int) ChronoUnit.DAYS.between(latestWorkoutDate(workouts), LocalDate.now());

        List<ProgramAdaptationSuggestionDto> suggestions = new ArrayList<>();

        for (TrainingDay day : safeList(program.getTrainingDays())) {
            for (TrainingDayExercise exercise : safeList(day.getExercises())) {
                ProgramAdaptationSuggestionDto suggestion = buildSuggestion(
                        day,
                        exercise,
                        performanceByExercise.getOrDefault(exercise.getExerciseId(), List.of()),
                        daysSinceLastWorkout
                );
                if (suggestion != null) {
                    suggestions.add(suggestion);
                }
            }
        }

        ProgramAdaptationResponse response = new ProgramAdaptationResponse();
        response.setUserId(userId);
        response.setProgramId(program.getId());
        response.setProgramName(program.getName());
        response.setDaysSinceLastWorkout(daysSinceLastWorkout);
        response.setReadinessMessage(buildReadinessMessage(daysSinceLastWorkout, suggestions));
        response.setSuggestions(suggestions);
        return response;
    }

    private List<AchievementDto> buildAchievements(List<Workout> workouts, AchievementMilestones milestones) {
        WorkoutStats stats = buildWorkoutStats(workouts);
        List<AchievementDto> achievements = new ArrayList<>();

        achievements.add(toAchievement(
                "FIRST_WORKOUT",
                "Первая тренировка",
                "Сделайте первую тренировку и откройте путь к новым рекордам.",
                "Старт",
                stats.workoutsCount,
                1,
                "трен.",
                milestones.firstWorkoutDate
        ));
        achievements.add(toAchievement(
                "CONSISTENCY_7",
                "Ритм недели",
                "Проведите 7 тренировок в сумме и закрепите привычку.",
                "Регулярность",
                stats.workoutsCount,
                7,
                "трен.",
                milestones.consistency7Date
        ));
        achievements.add(toAchievement(
                "MONTHLY_MACHINE",
                "Машина месяца",
                "Сделайте 8 тренировок за один месяц.",
                "Регулярность",
                stats.currentMonthBestWorkouts,
                8,
                "трен.",
                milestones.monthlyMachineDate
        ));
        achievements.add(toAchievement(
                "WEEKLY_WARRIOR",
                "Сильная неделя",
                "Закройте 4 тренировки за одну неделю.",
                "Регулярность",
                stats.bestWeekWorkouts,
                4,
                "трен.",
                milestones.weeklyWarriorDate
        ));
        achievements.add(toAchievement(
                "VOLUME_10K",
                "Объём 10K",
                "Накопите 10 000 кг общего тренировочного объёма.",
                "Нагрузка",
                stats.totalVolume,
                10_000,
                "кг",
                milestones.volume10kDate
        ));
        achievements.add(toAchievement(
                "DIVERSITY_8",
                "Исследователь упражнений",
                "Используйте 8 разных упражнений в тренировках.",
                "Разнообразие",
                stats.distinctExercises,
                8,
                "упр.",
                milestones.diversity8Date
        ));
        achievements.add(toAchievement(
                "STRENGTH_80",
                "Силовой рубеж",
                "Поднимите 80 кг в одном подходе.",
                "Сила",
                stats.bestWeight,
                80,
                "кг",
                milestones.strength80Date
        ));
        achievements.add(toAchievement(
                "REPS_20",
                "Выносливость 20",
                "Сделайте 20 повторений в одном подходе.",
                "Выносливость",
                stats.maxReps,
                20,
                "повт.",
                milestones.reps20Date
        ));

        return achievements;
    }

    private AchievementMilestones determineAchievementMilestones(List<Workout> workouts) {
        AchievementMilestones milestones = new AchievementMilestones();
        Map<String, Integer> monthCounts = new HashMap<>();
        Map<String, Integer> weekCounts = new HashMap<>();
        Set<Long> distinctExercises = new HashSet<>();
        double cumulativeVolume = 0;
        int workoutCount = 0;
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (Workout workout : workouts) {
            LocalDate workoutDate = getWorkoutDate(workout).toLocalDate();
            workoutCount += 1;

            if (milestones.firstWorkoutDate == null) {
                milestones.firstWorkoutDate = workoutDate;
            }
            if (workoutCount >= 7 && milestones.consistency7Date == null) {
                milestones.consistency7Date = workoutDate;
            }

            String monthKey = workoutDate.getYear() + "-" + workoutDate.getMonthValue();
            int monthCount = monthCounts.merge(monthKey, 1, Integer::sum);
            if (monthCount >= 8 && milestones.monthlyMachineDate == null) {
                milestones.monthlyMachineDate = workoutDate;
            }

            String weekKey = workoutDate.getYear() + "-" + workoutDate.get(weekFields.weekOfWeekBasedYear());
            int weekCount = weekCounts.merge(weekKey, 1, Integer::sum);
            if (weekCount >= 4 && milestones.weeklyWarriorDate == null) {
                milestones.weeklyWarriorDate = workoutDate;
            }

            for (WorkoutExercise workoutExercise : safeList(workout.getWorkoutExercises())) {
                if (workoutExercise.getExercise() != null && workoutExercise.getExercise().getId() != null) {
                    distinctExercises.add(workoutExercise.getExercise().getId());
                }
                if (distinctExercises.size() >= 8 && milestones.diversity8Date == null) {
                    milestones.diversity8Date = workoutDate;
                }

                for (ExerciseSet set : safeList(workoutExercise.getSets())) {
                    double weight = safeDouble(set.getWeight());
                    int reps = safeInt(set.getReps());
                    cumulativeVolume += weight * reps;

                    if (cumulativeVolume >= 10_000 && milestones.volume10kDate == null) {
                        milestones.volume10kDate = workoutDate;
                    }
                    if (weight >= 80 && milestones.strength80Date == null) {
                        milestones.strength80Date = workoutDate;
                    }
                    if (reps >= 20 && milestones.reps20Date == null) {
                        milestones.reps20Date = workoutDate;
                    }
                }
            }
        }

        return milestones;
    }

    private List<PersonalRecordDto> buildPersonalRecords(List<Workout> workouts) {
        SetRecord bestWeight = null;
        SetRecord maxReps = null;
        SessionRecord bestWorkoutVolume = null;
        SessionRecord bestMonthWorkoutVolume = null;

        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);

        for (Workout workout : workouts) {
            double workoutVolume = 0;

            for (WorkoutExercise workoutExercise : safeList(workout.getWorkoutExercises())) {
                String exerciseName = workoutExercise.getExercise() == null
                        ? "Упражнение"
                        : safeText(workoutExercise.getExercise().getName(), "Упражнение");

                for (ExerciseSet set : safeList(workoutExercise.getSets())) {
                    double weight = safeDouble(set.getWeight());
                    int reps = safeInt(set.getReps());
                    double volume = weight * reps;
                    workoutVolume += volume;

                    if (bestWeight == null || weight > bestWeight.value) {
                        bestWeight = new SetRecord(exerciseName, weight, getWorkoutDate(workout).toLocalDate());
                    }
                    if (maxReps == null || reps > maxReps.reps || (reps == maxReps.reps && volume > maxReps.volume)) {
                        maxReps = new SetRecord(exerciseName, reps, volume, getWorkoutDate(workout).toLocalDate());
                    }
                }
            }

            LocalDate workoutDate = getWorkoutDate(workout).toLocalDate();
            if (bestWorkoutVolume == null || workoutVolume > bestWorkoutVolume.volume) {
                bestWorkoutVolume = new SessionRecord(safeText(workout.getName(), "Тренировка"), workoutVolume, workoutDate);
            }
            if (!workoutDate.isBefore(firstDayOfMonth) && (bestMonthWorkoutVolume == null || workoutVolume > bestMonthWorkoutVolume.volume)) {
                bestMonthWorkoutVolume = new SessionRecord(safeText(workout.getName(), "Тренировка"), workoutVolume, workoutDate);
            }
        }

        List<PersonalRecordDto> records = new ArrayList<>();
        if (bestWeight != null) {
            records.add(toRecord(
                    "BEST_WEIGHT",
                    "Лучший вес",
                    bestWeight.exerciseName,
                    bestWeight.value,
                    "кг",
                    bestWeight.date,
                    "Максимальный вес в одном подходе"
            ));
        }
        if (maxReps != null) {
            records.add(toRecord(
                    "MAX_REPS",
                    "Максимум повторений",
                    maxReps.exerciseName,
                    maxReps.reps,
                    "повт.",
                    maxReps.date,
                    "Лучший результат по повторениям за один подход"
            ));
        }
        if (bestWorkoutVolume != null) {
            records.add(toRecord(
                    "BEST_VOLUME",
                    "Лучший объём",
                    bestWorkoutVolume.name,
                    bestWorkoutVolume.volume,
                    "кг",
                    bestWorkoutVolume.date,
                    "Самая объёмная тренировка за всё время"
            ));
        }
        if (bestMonthWorkoutVolume != null) {
            records.add(toRecord(
                    "MONTH_RECORD",
                    "Рекорд месяца",
                    bestMonthWorkoutVolume.name,
                    bestMonthWorkoutVolume.volume,
                    "кг",
                    bestMonthWorkoutVolume.date,
                    "Лучшая тренировка по объёму в текущем месяце"
            ));
        }
        return records;
    }

    private List<MuscleBalanceDto> buildMuscleBalance(List<Workout> workouts) {
        Map<String, Double> volumeByGroup = new LinkedHashMap<>();
        for (String group : BASE_MUSCLE_GROUPS) {
            volumeByGroup.put(group, 0.0);
        }

        LocalDateTime fromDate = LocalDateTime.now().minusDays(90);
        for (Workout workout : workouts) {
            if (getWorkoutDate(workout).isBefore(fromDate)) {
                continue;
            }

            for (WorkoutExercise workoutExercise : safeList(workout.getWorkoutExercises())) {
                String muscleGroup = workoutExercise.getExercise() == null
                        ? "Other"
                        : safeText(workoutExercise.getExercise().getMuscleGroup(), "Other");
                double exerciseVolume = 0;
                for (ExerciseSet set : safeList(workoutExercise.getSets())) {
                    exerciseVolume += safeDouble(set.getWeight()) * safeInt(set.getReps());
                }
                volumeByGroup.merge(muscleGroup, exerciseVolume, Double::sum);
            }
        }

        double totalVolume = volumeByGroup.values().stream().mapToDouble(Double::doubleValue).sum();
        List<MuscleBalanceDto> items = new ArrayList<>();
        for (Map.Entry<String, Double> entry : volumeByGroup.entrySet()) {
            MuscleBalanceDto dto = new MuscleBalanceDto();
            double sharePercent = totalVolume <= 0 ? 0 : (entry.getValue() / totalVolume) * 100.0;
            dto.setMuscleGroup(entry.getKey());
            dto.setTotalVolume(round(entry.getValue()));
            dto.setSharePercent(round(sharePercent));
            dto.setStatus(resolveMuscleBalanceStatus(sharePercent, totalVolume));
            items.add(dto);
        }
        return items;
    }

    private List<SmartReminderDto> buildSmartReminders(
            List<Workout> workouts,
            int recoveryScore,
            List<MuscleBalanceDto> muscleBalance
    ) {
        List<SmartReminderDto> reminders = new ArrayList<>();
        if (workouts.isEmpty()) {
            reminders.add(toReminder(
                    "FIRST_SESSION",
                    "Пора начать",
                    "После первой завершённой тренировки система начнёт точнее подсказывать объём, восстановление и новые рекорды.",
                    "low",
                    LocalDate.now()
            ));
            return reminders;
        }

        LocalDate today = LocalDate.now();
        long daysSinceLastWorkout = ChronoUnit.DAYS.between(latestWorkoutDate(workouts), today);
        int workoutsThisWeek = countWorkoutsFrom(workouts, startOfWeek(today).atStartOfDay());
        int workoutsLastSevenDays = countWorkoutsFrom(workouts, LocalDateTime.now().minusDays(7));

        if (daysSinceLastWorkout >= 4) {
            reminders.add(toReminder(
                    "MISSED_WORKOUTS",
                    "Пропуски тренировок",
                    "Вы давно не тренировались. Снизьте стартовую нагрузку и вернитесь к программе через короткую вводную сессию.",
                    daysSinceLastWorkout >= 7 ? "high" : "medium",
                    today
            ));
        }

        if (recoveryScore < 50) {
            reminders.add(toReminder(
                    "RECOVERY_LOW",
                    "Нужна разгрузка",
                    "По истории последних тренировок лучше уменьшить объём и дать себе больше времени на восстановление.",
                    "medium",
                    today
            ));
        }

        if (workoutsThisWeek == 0 && daysSinceLastWorkout >= 2) {
            reminders.add(toReminder(
                    "CONSISTENCY_NUDGE",
                    "Вернитесь в ритм",
                    "На этой неделе ещё не было тренировок. Даже одна короткая сессия поможет сохранить регулярность.",
                    "low",
                    today
            ));
        }

        if (workoutsLastSevenDays >= 5 && daysSinceLastWorkout <= 1) {
            reminders.add(toReminder(
                    "FATIGUE_ALERT",
                    "Высокая нагрузка",
                    "Последняя неделя была очень плотной. Полезно добавить день отдыха или облегчённую тренировку.",
                    "medium",
                    today
            ));
        }

        MuscleBalanceDto weakestGroup = muscleBalance.stream()
                .filter(item -> item.getSharePercent() > 0 || !"dominant".equals(item.getStatus()))
                .min((left, right) -> Double.compare(left.getSharePercent(), right.getSharePercent()))
                .orElse(null);
        MuscleBalanceDto dominantGroup = muscleBalance.stream()
                .max((left, right) -> Double.compare(left.getSharePercent(), right.getSharePercent()))
                .orElse(null);

        if (weakestGroup != null && dominantGroup != null
                && dominantGroup.getSharePercent() >= 28
                && weakestGroup.getSharePercent() <= 8) {
            reminders.add(toReminder(
                    "MUSCLE_BALANCE",
                    "Стоит выровнять нагрузку",
                    "Сейчас акцент заметно смещён в сторону группы \"" + dominantGroup.getMuscleGroup()
                            + "\". Добавьте больше работы на \"" + weakestGroup.getMuscleGroup() + "\".",
                    "low",
                    today
            ));
        }

        return reminders;
    }

    private String buildProfileMessage(
            List<Workout> workouts,
            List<AchievementDto> achievements,
            int recoveryScore,
            List<SmartReminderDto> smartReminders
    ) {
        if (workouts.isEmpty()) {
            return "Начните первую тренировку, чтобы открыть достижения, рекорды и персональные рекомендации.";
        }

        if (!smartReminders.isEmpty()) {
            return smartReminders.get(0).getMessage();
        }

        long unlocked = achievements.stream().filter(AchievementDto::isUnlocked).count();
        if (recoveryScore >= 80 && unlocked >= 5) {
            return "Вы хорошо восстановились и держите сильную базу достижений. Это удачный момент для прогрессии по ключевым упражнениям.";
        }
        if (recoveryScore <= 55) {
            return "Лучше сохранить технику и не гнаться за лишним объёмом. Спокойная тренировка сейчас даст больше пользы.";
        }

        return "Прогресс идёт ровно: сохраняйте регулярность, и система продолжит открывать новые достижения и подсказывать изменения нагрузки.";
    }

    private int calculateRecoveryScore(List<Workout> workouts) {
        if (workouts.isEmpty()) {
            return 60;
        }

        LocalDate today = LocalDate.now();
        long daysSinceLastWorkout = ChronoUnit.DAYS.between(latestWorkoutDate(workouts), today);
        int recent7 = countWorkoutsFrom(workouts, LocalDateTime.now().minusDays(7));
        int recent14 = countWorkoutsFrom(workouts, LocalDateTime.now().minusDays(14));

        int score;
        if (daysSinceLastWorkout == 0) {
            score = 55;
        } else if (daysSinceLastWorkout == 1) {
            score = 70;
        } else if (daysSinceLastWorkout == 2) {
            score = 84;
        } else if (daysSinceLastWorkout == 3) {
            score = 78;
        } else if (daysSinceLastWorkout == 4) {
            score = 70;
        } else if (daysSinceLastWorkout <= 6) {
            score = 62;
        } else if (daysSinceLastWorkout <= 9) {
            score = 50;
        } else {
            score = 38;
        }

        if (recent7 >= 5) {
            score -= 12;
        } else if (recent7 >= 3) {
            score -= 5;
        }

        if (recent14 == 0) {
            score = Math.min(score, 45);
        }

        return Math.max(20, Math.min(95, score));
    }

    private String buildRecoveryStatus(int recoveryScore) {
        if (recoveryScore >= 80) {
            return "Готов к интенсивной тренировке";
        }
        if (recoveryScore >= 65) {
            return "Хорошее восстановление";
        }
        if (recoveryScore >= 50) {
            return "Лучше держать умеренную нагрузку";
        }
        return "Нужна разгрузка и спокойный возврат в ритм";
    }

    public ProgramAdaptationSuggestionDto buildSuggestion(
            TrainingDay day,
            TrainingDayExercise exercise,
            List<ExercisePerformanceSnapshot> history,
            Integer daysSinceLastWorkout
    ) {
        int currentSets = safeInt(exercise.getRecommendedSets());
        int currentReps = safeInt(exercise.getRecommendedReps());
        double currentWeight = safeDouble(exercise.getRecommendedWeight());
        String exerciseName = safeText(exercise.getExerciseName(), "Упражнение");
        String dayIdentifier = safeText(day.getDayIdentifier(), "Тренировочный день");

        if (daysSinceLastWorkout != null && daysSinceLastWorkout >= 10) {
            ProgramAdaptationSuggestionDto suggestion = baseSuggestion(dayIdentifier, exercise, exerciseName);
            suggestion.setRecommendationType("DELOAD_RETURN");
            suggestion.setTitle("Снизить нагрузку после паузы");
            suggestion.setReason("Последняя тренировка была давно. Лучше вернуться мягко и восстановить ритм без перегруза.");
            suggestion.setCurrentRecommendedWeight(currentWeight);
            suggestion.setSuggestedWeight(round(safeDecreaseWeight(currentWeight, 0.10)));
            suggestion.setCurrentRecommendedSets(currentSets);
            suggestion.setSuggestedSets(Math.max(1, currentSets > 0 ? currentSets - 1 : 2));
            suggestion.setCurrentRecommendedReps(currentReps);
            suggestion.setSuggestedReps(currentReps > 0 ? currentReps : 10);
            return suggestion;
        }

        if (history.size() >= 3 && shouldIncreaseWeight(history, currentReps)) {
            ProgramAdaptationSuggestionDto suggestion = baseSuggestion(dayIdentifier, exercise, exerciseName);
            suggestion.setRecommendationType(currentWeight > 0 ? "INCREASE_WEIGHT" : "INCREASE_REPS");
            suggestion.setTitle(currentWeight > 0 ? "Увеличить рабочий вес" : "Повысить целевые повторения");
            suggestion.setReason("Последние тренировки показывают стабильное выполнение верхней границы повторений.");
            suggestion.setCurrentRecommendedWeight(currentWeight);
            suggestion.setSuggestedWeight(currentWeight > 0 ? round(currentWeight + calculateWeightStep(currentWeight)) : currentWeight);
            suggestion.setCurrentRecommendedSets(currentSets);
            suggestion.setSuggestedSets(currentSets > 0 ? currentSets : 3);
            suggestion.setCurrentRecommendedReps(currentReps);
            suggestion.setSuggestedReps(currentWeight > 0 ? currentReps : Math.max(12, currentReps + 2));
            return suggestion;
        }

        if (history.size() >= 3 && shouldReduceLoad(history)) {
            ProgramAdaptationSuggestionDto suggestion = baseSuggestion(dayIdentifier, exercise, exerciseName);
            suggestion.setRecommendationType("REDUCE_LOAD");
            suggestion.setTitle("Снизить нагрузку и восстановить технику");
            suggestion.setReason("Несколько тренировок подряд показывают падение результата. Полезно сделать короткий шаг назад.");
            suggestion.setCurrentRecommendedWeight(currentWeight);
            suggestion.setSuggestedWeight(round(safeDecreaseWeight(currentWeight, 0.08)));
            suggestion.setCurrentRecommendedSets(currentSets);
            suggestion.setSuggestedSets(Math.max(1, currentSets > 0 ? currentSets - 1 : 2));
            suggestion.setCurrentRecommendedReps(currentReps);
            suggestion.setSuggestedReps(currentReps > 0 ? currentReps : 8);
            return suggestion;
        }

        return null;
    }

    private ProgramAdaptationSuggestionDto baseSuggestion(
            String dayIdentifier,
            TrainingDayExercise exercise,
            String exerciseName
    ) {
        ProgramAdaptationSuggestionDto suggestion = new ProgramAdaptationSuggestionDto();
        suggestion.setDayIdentifier(dayIdentifier);
        suggestion.setExerciseId(exercise.getExerciseId());
        suggestion.setExerciseName(exerciseName);
        return suggestion;
    }

    private boolean shouldIncreaseWeight(List<ExercisePerformanceSnapshot> history, int currentReps) {
        List<ExercisePerformanceSnapshot> recent = getRecentSnapshots(history, 3);
        int repThreshold = Math.max(12, currentReps > 0 ? currentReps : 10);
        return recent.stream().allMatch(snapshot -> snapshot.maxReps >= repThreshold || snapshot.averageReps >= repThreshold);
    }

    private boolean shouldReduceLoad(List<ExercisePerformanceSnapshot> history) {
        List<ExercisePerformanceSnapshot> recent = getRecentSnapshots(history, 3);
        if (recent.size() < 3) {
            return false;
        }

        ExercisePerformanceSnapshot first = recent.get(0);
        ExercisePerformanceSnapshot second = recent.get(1);
        ExercisePerformanceSnapshot third = recent.get(2);

        return second.totalVolume < first.totalVolume
                && third.totalVolume < second.totalVolume
                && third.averageReps <= second.averageReps;
    }

    private List<ExercisePerformanceSnapshot> getRecentSnapshots(List<ExercisePerformanceSnapshot> history, int limit) {
        int fromIndex = Math.max(0, history.size() - limit);
        return history.subList(fromIndex, history.size());
    }

    private String buildReadinessMessage(Integer daysSinceLastWorkout, List<ProgramAdaptationSuggestionDto> suggestions) {
        if (daysSinceLastWorkout == null) {
            return "Пока нет завершённых тренировок. После первых занятий система начнёт давать точные рекомендации.";
        }
        if (daysSinceLastWorkout >= 10) {
            return "После паузы лучше снизить стартовую нагрузку и вернуть ритм постепенно.";
        }
        if (suggestions.isEmpty()) {
            return "Сейчас программа выглядит сбалансированной. Можно продолжать текущий план без изменений.";
        }
        return "Найдены точки роста: система предлагает скорректировать нагрузку по отдельным упражнениям.";
    }

    private Map<Long, List<ExercisePerformanceSnapshot>> buildExercisePerformanceMap(List<Workout> workouts) {
        Map<Long, List<ExercisePerformanceSnapshot>> result = new HashMap<>();

        for (Workout workout : workouts) {
            LocalDate date = getWorkoutDate(workout).toLocalDate();

            for (WorkoutExercise workoutExercise : safeList(workout.getWorkoutExercises())) {
                if (workoutExercise.getExercise() == null || workoutExercise.getExercise().getId() == null) {
                    continue;
                }

                ExercisePerformanceSnapshot snapshot = new ExercisePerformanceSnapshot();
                snapshot.date = date;
                snapshot.exerciseId = workoutExercise.getExercise().getId();
                snapshot.exerciseName = safeText(workoutExercise.getExercise().getName(), "Упражнение");

                for (ExerciseSet set : safeList(workoutExercise.getSets())) {
                    int reps = safeInt(set.getReps());
                    double weight = safeDouble(set.getWeight());
                    snapshot.totalVolume += reps * weight;
                    snapshot.totalReps += reps;
                    snapshot.totalSets += 1;
                    snapshot.maxWeight = Math.max(snapshot.maxWeight, weight);
                    snapshot.maxReps = Math.max(snapshot.maxReps, reps);
                }

                snapshot.averageReps = snapshot.totalSets == 0 ? 0 : (double) snapshot.totalReps / snapshot.totalSets;
                result.computeIfAbsent(snapshot.exerciseId, ignored -> new ArrayList<>()).add(snapshot);
            }
        }

        return result;
    }

    private WorkoutStats buildWorkoutStats(List<Workout> workouts) {
        WorkoutStats stats = new WorkoutStats();
        stats.workoutsCount = workouts.size();

        Map<String, Integer> weeklyCounts = new HashMap<>();
        Map<String, Integer> monthlyCounts = new HashMap<>();
        Set<Long> exerciseIds = new HashSet<>();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        for (Workout workout : workouts) {
            LocalDate workoutDate = getWorkoutDate(workout).toLocalDate();
            String weekKey = workoutDate.getYear() + "-" + workoutDate.get(weekFields.weekOfWeekBasedYear());
            String monthKey = workoutDate.getYear() + "-" + workoutDate.getMonthValue();
            weeklyCounts.merge(weekKey, 1, Integer::sum);
            monthlyCounts.merge(monthKey, 1, Integer::sum);

            for (WorkoutExercise workoutExercise : safeList(workout.getWorkoutExercises())) {
                if (workoutExercise.getExercise() != null && workoutExercise.getExercise().getId() != null) {
                    exerciseIds.add(workoutExercise.getExercise().getId());
                }
                for (ExerciseSet set : safeList(workoutExercise.getSets())) {
                    double weight = safeDouble(set.getWeight());
                    int reps = safeInt(set.getReps());
                    stats.totalVolume += weight * reps;
                    stats.bestWeight = Math.max(stats.bestWeight, weight);
                    stats.maxReps = Math.max(stats.maxReps, reps);
                }
            }
        }

        stats.bestWeekWorkouts = weeklyCounts.values().stream().max(Integer::compareTo).orElse(0);
        stats.currentMonthBestWorkouts = monthlyCounts.values().stream().max(Integer::compareTo).orElse(0);
        stats.distinctExercises = exerciseIds.size();
        return stats;
    }

    private AchievementDto toAchievement(
            String code,
            String title,
            String description,
            String category,
            double currentValue,
            double targetValue,
            String unit,
            LocalDate awardedAt
    ) {
        AchievementDto dto = new AchievementDto();
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

    private PersonalRecordDto toRecord(
            String code,
            String title,
            String exerciseName,
            double value,
            String unit,
            LocalDate date,
            String subtitle
    ) {
        PersonalRecordDto dto = new PersonalRecordDto();
        dto.setCode(code);
        dto.setTitle(title);
        dto.setExerciseName(exerciseName);
        dto.setValue(round(value));
        dto.setUnit(unit);
        dto.setDate(date == null ? null : date.toString());
        dto.setSubtitle(subtitle);
        return dto;
    }

    private SmartReminderDto toReminder(
            String code,
            String title,
            String message,
            String severity,
            LocalDate createdAt
    ) {
        SmartReminderDto dto = new SmartReminderDto();
        dto.setCode(code);
        dto.setTitle(title);
        dto.setMessage(message);
        dto.setSeverity(severity);
        dto.setCreatedAt(createdAt == null ? null : createdAt.toString());
        return dto;
    }

    private String resolveMuscleBalanceStatus(double sharePercent, double totalVolume) {
        if (totalVolume <= 0) {
            return "neutral";
        }
        if (sharePercent >= 28) {
            return "dominant";
        }
        if (sharePercent <= 8) {
            return "undertrained";
        }
        return "balanced";
    }

    private LocalDate latestWorkoutDate(List<Workout> workouts) {
        if (workouts.isEmpty()) {
            return LocalDate.now();
        }
        return getWorkoutDate(workouts.get(workouts.size() - 1)).toLocalDate();
    }

    private int countWorkoutsFrom(List<Workout> workouts, LocalDateTime fromDate) {
        int count = 0;
        for (Workout workout : workouts) {
            if (!getWorkoutDate(workout).isBefore(fromDate)) {
                count += 1;
            }
        }
        return count;
    }

    private LocalDate startOfWeek(LocalDate date) {
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        while (date.getDayOfWeek() != firstDayOfWeek) {
            date = date.minusDays(1);
        }
        return date;
    }

    private LocalDateTime getWorkoutDate(Workout workout) {
        return workout.getWorkoutDate() == null ? LocalDateTime.MIN.plusYears(2000) : workout.getWorkoutDate();
    }

    private double calculateWeightStep(double currentWeight) {
        double rawStep = Math.max(2.5, currentWeight * 0.05);
        return Math.max(0.5, Math.round(rawStep * 2.0) / 2.0);
    }

    private double safeDecreaseWeight(double currentWeight, double percent) {
        if (currentWeight <= 0) {
            return 0;
        }
        return currentWeight * (1 - percent);
    }

    private <T> List<T> safeList(List<T> items) {
        return items == null ? List.of() : items;
    }

    private String safeText(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private int safeInt(Integer value) {
        return value == null ? 0 : value;
    }

    private double safeDouble(Double value) {
        return value == null ? 0 : value;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static class WorkoutStats {
        private int workoutsCount;
        private int currentMonthBestWorkouts;
        private int bestWeekWorkouts;
        private int distinctExercises;
        private double totalVolume;
        private double bestWeight;
        private int maxReps;
    }

    private static class AchievementMilestones {
        private LocalDate firstWorkoutDate;
        private LocalDate consistency7Date;
        private LocalDate monthlyMachineDate;
        private LocalDate weeklyWarriorDate;
        private LocalDate volume10kDate;
        private LocalDate diversity8Date;
        private LocalDate strength80Date;
        private LocalDate reps20Date;
    }

    public static class ExercisePerformanceSnapshot {
        private LocalDate date;
        private Long exerciseId;
        private String exerciseName;
        private double totalVolume;
        private int totalReps;
        private int totalSets;
        private int maxReps;
        private double maxWeight;
        private double averageReps;
    }

    private static class SetRecord {
        private final String exerciseName;
        private final double value;
        private final int reps;
        private final double volume;
        private final LocalDate date;

        private SetRecord(String exerciseName, double value, LocalDate date) {
            this.exerciseName = exerciseName;
            this.value = value;
            this.reps = 0;
            this.volume = 0;
            this.date = date;
        }

        private SetRecord(String exerciseName, int reps, double volume, LocalDate date) {
            this.exerciseName = exerciseName;
            this.value = 0;
            this.reps = reps;
            this.volume = volume;
            this.date = date;
        }
    }

    private static class SessionRecord {
        private final String name;
        private final double volume;
        private final LocalDate date;

        private SessionRecord(String name, double volume, LocalDate date) {
            this.name = name;
            this.volume = volume;
            this.date = date;
        }
    }
}
