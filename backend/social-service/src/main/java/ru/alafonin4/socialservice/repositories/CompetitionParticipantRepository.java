package ru.alafonin4.socialservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alafonin4.socialservice.entities.CompetitionParticipant;

import java.util.Optional;

@Repository
public interface CompetitionParticipantRepository extends JpaRepository<CompetitionParticipant, Long> {
    /**
     * Finds a participant record for a specific competition and user.
     *
     * @param competitionId identifier of the competition
     * @param userId identifier of the participant user
     * @return optional participant record for the supplied pair
     */
    Optional<CompetitionParticipant> findByCompetitionIdAndUserId(Long competitionId, Long userId);
}
