package ru.alafonin4.authserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long id;

    /**
     * Returns the token.
     * @return resulting text value
     */
    public String getToken() {
        return token;
    }

    /**
     * Updates the token.
     * @param token JWT token value
     */
    public void setToken(String token) {
        this.token = token;
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
