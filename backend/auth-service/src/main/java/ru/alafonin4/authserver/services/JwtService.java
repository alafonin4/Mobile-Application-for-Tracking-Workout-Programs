package ru.alafonin4.authserver.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.alafonin4.authserver.exceptions.InvalidTokenException;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String SECRET_KEY = "6D5A7134743777217A25432A462D4A614E645267556B586E3272357538782F41";
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * GenerateToken.
     * @param userDetails Spring Security user details
     * @return resulting text value
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * GenerateToken.
     * @param extraClaims extra claims
     * @param userDetails Spring Security user details
     * @return resulting text value
     */
    public String generateToken(
            Map<String, Objects> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * IsTokenValid.
     * @param token JWT token value
     * @param userDetails Spring Security user details
     * @return true when the condition is satisfied; otherwise false
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractEmail(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * IsTokenExpired.
     * @param token JWT token value
     * @return true when the condition is satisfied; otherwise false
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration.
     * @param token JWT token value
     * @return result of the operation
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts the email.
     * @param token JWT token value
     * @return resulting text value
     */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the claim.
     * @param token JWT token value
     * @param claimsResolver claim mapping function
     * @return result of the operation
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts the all claims.
     * @param token JWT token value
     * @return result of the operation
     */
    private Claims extractAllClaims(String token) {
        return Jwts.
                parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Returns the sign in key.
     * @return result of the operation
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * IsValidToken.
     * @param authHeader authorization header value containing the bearer token
     * @return true when the condition is satisfied; otherwise false
     */
    public boolean isValidToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String jwtToken = authHeader.substring(7);

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(jwtToken);

            // должен вернуть email
            String email = extractEmail(jwtToken);
            var userDetails = userDetailsService.loadUserByUsername(email);

            return isTokenValid(jwtToken, userDetails);
        } catch (Exception ignored) {
            throw new InvalidTokenException("Invalid token");
        }
    }
}
