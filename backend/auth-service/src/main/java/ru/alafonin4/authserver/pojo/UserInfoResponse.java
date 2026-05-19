package ru.alafonin4.authserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.alafonin4.authserver.enums.UserRole;


@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    String email;
    UserRole role;

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
     * Returns the role.
     * @return result of the operation
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Updates the role.
     * @param role new role
     */
    public void setRole(UserRole role) {
        this.role = role;
    }
}
