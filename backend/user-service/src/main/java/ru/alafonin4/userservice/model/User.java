package ru.alafonin4.userservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User {
    @Id
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name= "bio", length = 500)
    private String bio;

    @Column
    private Double bodyWeight;

    @Column(name = "fitness_goal", length = 40)
    private String fitnessGoal;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

    /**
     * LocalDateTime.now.
     * @return result of the operation
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Returns the first name.
     * @return resulting text value
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Updates the first name.
     * @param firstName new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name.
     * @return resulting text value
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Updates the last name.
     * @param lastName new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the bio.
     * @return resulting text value
     */
    public String getBio() {
        return bio;
    }

    /**
     * Updates the bio.
     * @param bio new bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Returns the body weight.
     * @return calculated numeric value
     */
    public Double getBodyWeight() {
        return bodyWeight;
    }

    /**
     * Updates the body weight.
     * @param bodyWeight new body weight
     */
    public void setBodyWeight(Double bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    /**
     * Returns the fitness goal.
     * @return resulting text value
     */
    public String getFitnessGoal() {
        return fitnessGoal;
    }

    /**
     * Updates the fitness goal.
     * @param fitnessGoal new fitness goal
     */
    public void setFitnessGoal(String fitnessGoal) {
        this.fitnessGoal = fitnessGoal;
    }

    /**
     * Returns the email.
     * @return resulting text value
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the email.
     * @param email new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the avatar url.
     * @return resulting text value
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Updates the avatar url.
     * @param avatarUrl new avatar url
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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
}

