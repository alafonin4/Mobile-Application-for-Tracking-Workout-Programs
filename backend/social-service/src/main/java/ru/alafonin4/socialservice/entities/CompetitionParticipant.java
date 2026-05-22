package ru.alafonin4.socialservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import ru.alafonin4.socialservice.enums.CompetitionParticipantStatus;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "competition_participants",
        indexes = {
                @Index(name = "idx_competition_participant_competition_user", columnList = "competition_id,userId"),
                @Index(name = "idx_competition_participant_user_status", columnList = "userId,status")
        }
)
public class CompetitionParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private CompetitionParticipantStatus status;

    private LocalDateTime invitedAt;

    private LocalDateTime respondedAt;

    private LocalDateTime completedAt;

    /**
     * Returns the id.
     * @return result of the operation
     */
    public Long getId() {
        return id;
    }

    /**
     * Updates the id.
     * @param id identifier of the target record
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the competition.
     * @return result of the operation
     */
    public Competition getCompetition() {
        return competition;
    }

    /**
     * Updates the competition.
     * @param competition new competition
     */
    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    /**
     * Returns the identifier of the user.
     * @return result of the operation
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Updates the identifier of the user.
     * @param userId identifier of the user
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Returns the status.
     * @return result of the operation
     */
    public CompetitionParticipantStatus getStatus() {
        return status;
    }

    /**
     * Updates the status.
     * @param status new status
     */
    public void setStatus(CompetitionParticipantStatus status) {
        this.status = status;
    }

    /**
     * Returns the invited at.
     * @return result of the operation
     */
    public LocalDateTime getInvitedAt() {
        return invitedAt;
    }

    /**
     * Updates the invited at.
     * @param invitedAt new invited at
     */
    public void setInvitedAt(LocalDateTime invitedAt) {
        this.invitedAt = invitedAt;
    }

    /**
     * Returns the responded at.
     * @return result of the operation
     */
    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    /**
     * Updates the responded at.
     * @param respondedAt new responded at
     */
    public void setRespondedAt(LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }

    /**
     * Returns the completed at.
     * @return result of the operation
     */
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    /**
     * Updates the completed at.
     * @param completedAt new completed at
     */
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
