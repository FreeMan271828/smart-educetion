package org.nuist.util;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.nuist.entity.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.nuist.factory.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Long expireTime = 252000L;

    private static final Long refreshExpireTime = 86400000L;

    @Value("${jwts.secretKey}")
    private String secret;

    private SecretKey secretKey;

    @PostConstruct
    private SecretKey secretKey() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        return secretKey;
    }

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

    /**
     * 检查token是否过期
     * @param token JWT token
     * @return true := 已过期, false := 未过期
     */
    public boolean isExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * 检查JWT token是否有效
     * @param token JWT token
     * @return true := 有效, false := 无效
     */
    public boolean validateToken(String token) {
        try {
            String username = extractUsername(token);
        } catch (JwtException e) {
            return false;
        }
        return !isExpired(token);
    }

}
