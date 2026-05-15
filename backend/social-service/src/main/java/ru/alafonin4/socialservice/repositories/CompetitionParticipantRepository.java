package ru.alafonin4.socialservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.socialservice.entities.CompetitionParticipant;

import java.util.Optional;

@Repository
public interface CompetitionParticipantRepository extends JpaRepository<CompetitionParticipant, Long> {
    Optional<CompetitionParticipant> findByCompetitionIdAndUserId(Long competitionId, Long userId);
}
