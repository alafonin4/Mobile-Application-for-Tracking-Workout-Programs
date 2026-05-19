package ru.alafonin4.authserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_token", columnDefinition = "TEXT")
    private String sessionToken;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    /**
     * Returns the session token.
     * @return resulting text value
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Updates the session token.
     * @param sessionToken persisted session token
     */
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * Returns the user.
     * @return result of the operation
     */
    public User getUser() {
        return user;
    }

    /**
     * Updates the user.
     * @param user user being processed
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the id.
     * @return result of the operation
     */
    public Long getId() {
        return id;
    }
}
