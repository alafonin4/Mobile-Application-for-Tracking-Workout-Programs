package ru.alafonin4.authserver.pojo;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordRequest {
    @NotBlank(message = "Current password cannot be empty.")
    private String currentPassword;

    @NotBlank(message = "New password cannot be empty.")
    private String newPassword;

    /**
     * Returns the current password.
     * @return resulting text value
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * Updates the current password.
     * @param currentPassword new current password
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * Returns the new password.
     * @return resulting text value
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Updates the new password.
     * @param newPassword new new password
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
