package ru.alafonin4.socialservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.alafonin4.socialservice.entities.Competition;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

    @Query("""
            select distinct c
            from Competition c
            left join fetch c.participants
            where c.id = :competitionId
            """)
    Optional<Competition> findDetailedById(@Param("competitionId") Long competitionId);

    @Query("""
            select distinct c
            from Competition c
            left join fetch c.participants
            where c.id in (
                select participant.competition.id
                from CompetitionParticipant participant
                where participant.userId = :userId
            )
            order by c.createdAt desc
            """)
    List<Competition> findDetailedByUserId(@Param("userId") Long userId);
}
