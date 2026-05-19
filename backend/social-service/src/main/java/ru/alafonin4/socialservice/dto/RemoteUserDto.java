package ru.alafonin4.socialservice.dto;

public class RemoteUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String avatarUrl;

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
}
