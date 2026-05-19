package ru.alafonin4.workoutservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.workoutservice.model.TrainingProgram;

import java.util.List;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Long> {
    /**
     * Returns training programs created by the supplied user ordered by most recent identifier.
     *
     * @param userId identifier of the user
     * @return training programs for the supplied user
     */
    List<TrainingProgram> findByUserIdOrderByIdDesc(Long userId);
}
