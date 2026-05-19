package ru.alafonin4.userservice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private Long id;
    private String firstName;

    private String lastName;

    private String bio;

    private Double bodyWeight;

    private String fitnessGoal;

    private String email;

    private String avatarUrl;

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
}
