package ru.alafonin4.authserver.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class GetResponse {
    private String token;

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
}
