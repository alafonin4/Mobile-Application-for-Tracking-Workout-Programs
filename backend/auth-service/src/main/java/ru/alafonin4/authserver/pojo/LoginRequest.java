package ru.alafonin4.authserver.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest implements AuthRequest{
    @Email(message = "Incorrect email address.")
    private String email;
    @NotBlank(message = "Password cannot be empty.")
    private String password;

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
