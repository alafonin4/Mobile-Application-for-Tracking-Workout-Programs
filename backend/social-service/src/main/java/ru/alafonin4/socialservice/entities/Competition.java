package ru.alafonin4.socialservice.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import ru.alafonin4.socialservice.enums.CompetitionGoalType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "competitions",
        indexes = {
                @Index(name = "idx_competition_creator_created", columnList = "creatorId,createdAt")
        }
)
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long creatorId;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private CompetitionGoalType goalType;

    private Double targetValue;

    private Long exerciseId;

    @Column(length = 150)
    private String exerciseName;

    private Integer periodMonths;

    private LocalDateTime createdAt;

    /**
     * ArrayList<>.
     * @return result of the operation
     */
    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompetitionParticipant> participants = new ArrayList<>();

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
     * Returns the identifier of the creator.
     * @return result of the operation
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * Updates the identifier of the creator.
     * @param creatorId new identifier of the creator
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * Returns the title.
     * @return resulting text value
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates the title.
     * @param title human-readable title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the description.
     * @return resulting text value
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description.
     * @param description human-readable description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the goal type.
     * @return result of the operation
     */
    public CompetitionGoalType getGoalType() {
        return goalType;
    }

    /**
     * Updates the goal type.
     * @param goalType new goal type
     */
    public void setGoalType(CompetitionGoalType goalType) {
        this.goalType = goalType;
    }

    /**
     * Returns the target value.
     * @return calculated numeric value
     */
    public Double getTargetValue() {
        return targetValue;
    }

    /**
     * Updates the target value.
     * @param targetValue target metric value
     */
    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }

    /**
     * Returns the identifier of the exercise.
     * @return result of the operation
     */
    public Long getExerciseId() {
        return exerciseId;
    }

    /**
     * Updates the identifier of the exercise.
     * @param exerciseId identifier of the exercise
     */
    public void setExerciseId(Long exerciseId) {
        this.exerciseId = exerciseId;
    }

    /**
     * Returns the exercise name.
     * @return resulting text value
     */
    public String getExerciseName() {
        return exerciseName;
    }

    /**
     * Updates the exercise name.
     * @param exerciseName new exercise name
     */
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    /**
     * Returns the period months.
     * @return calculated numeric value
     */
    public Integer getPeriodMonths() {
        return periodMonths;
    }

    /**
     * Updates the period months.
     * @param periodMonths new period months
     */
    public void setPeriodMonths(Integer periodMonths) {
        this.periodMonths = periodMonths;
    }

    /**
     * Returns the created at.
     * @return result of the operation
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Updates the created at.
     * @param createdAt creation timestamp
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the participants.
     * @return prepared list with the requested data
     */
    public List<CompetitionParticipant> getParticipants() {
        return participants;
    }

    /**
     * Updates the participants.
     * @param participants new participants
     */
    public void setParticipants(List<CompetitionParticipant> participants) {
        this.participants = participants;
    }
}
