package org.nuist.util;

import org.nuist.entity.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtUtil {

    private static final Long expireTime = 252000L;

    private static final Long refreshExpireTime = 86400000L;

    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    private String buildToken(String subject, long ttlMillis) {
        return Jwts.builder()
                .subject(subject)
                .expiration(new Date(System.currentTimeMillis() + ttlMillis))
                .issuedAt(new Date())
                .signWith(secretKey)
                .compact();
    }

    public TokenResponse generateToken(String username) {
        return TokenResponse.builder()
                .accessToken(buildToken(username, expireTime))
                .refreshToken(buildToken(username, refreshExpireTime))
                .build();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            String username = extractUsername(token);
        } catch (JwtException e) {
            return false;
        }
        return isExpired(token);
    }

}
