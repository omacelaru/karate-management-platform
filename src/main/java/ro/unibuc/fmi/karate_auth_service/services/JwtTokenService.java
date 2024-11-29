package ro.unibuc.fmi.karate_auth_service.services;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtTokenService {

    @Value("${jwt.secret-signing-key}")
    private String SECRET_SIGNING_KEY;

    public String generateToken(UserDetails userDetails) {
        log.debug("Generating token for user: {}", userDetails.getUsername());
        return generateToken(Map.of(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.debug("Generating token with extra claims for user: {}", userDetails.getUsername());
        String token = Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .signWith(getSecretSigningKey())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .compact();
        log.info("Token successfully generated for user: {}", userDetails.getUsername());
        return token;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.debug("Validating token for user: {}", userDetails.getUsername());
        final String userEmail = extractUserEmail(token);
        boolean isValid = userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token);
        if (isValid) {
            log.info("Token is valid for user: {}", userDetails.getUsername());
        } else {
            log.warn("Token validation failed for user: {}", userDetails.getUsername());
        }
        return isValid;
    }

    private boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        log.debug("Token expiration status: {}", expired ? "Expired" : "Valid");
        return expired;
    }

    private Date extractExpiration(String token) {
        log.debug("Extracting expiration date from token.");
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserEmail(String token) {
        log.debug("Extracting user email from token.");
        String userEmail = extractClaim(token, Claims::getSubject);
        log.debug("Extracted user email: {}", userEmail);
        return userEmail;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        log.debug("Extracting claims from token.");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        log.debug("Parsing token to extract all claims.");
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSecretSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Failed to extract claims from token: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    private SecretKey getSecretSigningKey() {
        log.debug("Generating secret signing key from configured secret.");
        try {
            byte[] publicSigningKeyBytes = Base64.getDecoder().decode(SECRET_SIGNING_KEY);
            return Keys.hmacShaKeyFor(publicSigningKeyBytes);
        } catch (Exception e) {
            log.error("Failed to generate secret signing key: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get public signing key", e);
        }
    }
}

