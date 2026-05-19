package ru.alafonin4.authserver.pojo;


public interface AuthRequest {
    /**
     * Returns the email.
     * @return resulting text value
     */
    public String getEmail();
    /**
     * Returns the password.
     * @return resulting text value
     */
    public String getPassword();
}
