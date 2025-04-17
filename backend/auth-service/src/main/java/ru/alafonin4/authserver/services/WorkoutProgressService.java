package ru.alafonin4.authserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alafonin4.authserver.entities.WorkoutProgress;
import ru.alafonin4.authserver.pojo.ProgressByDate;
import ru.alafonin4.authserver.repositories.WorkoutProgressRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutProgressService {

    private final WorkoutProgressRepository workoutProgressRepository;

    public List<ProgressByDate> getProgressByPeriod(String email, int months) {
        LocalDate endDate = LocalDate.now();
        System.out.println(endDate);
        LocalDate startDate = endDate.minusMonths(months);

        List<WorkoutProgress> progressList = workoutProgressRepository.findByEmailAndDateTimeBetween(
                email, startDate.atStartOfDay(), endDate.atTime(23, 59, 59)
        );
        for (var i :
                progressList) {
            System.out.println("item " + i.getProgress() + " " + i.getEmail());
        }

        Map<LocalDate, Double> progressByDate = progressList.stream()
                .collect(Collectors.groupingBy(progress -> progress.getDateTime().toLocalDate(),
                        Collectors.averagingDouble(WorkoutProgress::getProgress)));

        return progressByDate.entrySet().stream()
                .map(entry -> new ProgressByDate(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

}