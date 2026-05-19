package ru.alafonin4.authserver.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest implements AuthRequest{
    @NotBlank(message = "Firstname cannot be empty.")
    private String firstName;
    @NotBlank(message = "Lastname cannot be empty.")
    private String lastName;
    @Email(message = "Incorrect email address.")
    private String email;
    @NotBlank(message = "Password cannot be empty.")
    private String password;

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
    @Override
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
     * Returns the password.
     * @return resulting text value
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Updates the password.
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
