package ru.outofmemory.zelixbackend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import ru.outofmemory.zelixbackend.entities.UserEntity;

import javax.crypto.SecretKey;

import java.util.Date;

@Service
public class JwtService {

    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24;

    private SecretKey getSigningKey() {
        String SECRET = "a8520900d3b97015cff1607d20b57b78c59600563f20dff4e4648cecb1b566f6";
        byte[] keyBytes = SECRET.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserEntity userEntity, boolean rememberMe) {

        Date now = new Date();
        int multiplier = rememberMe ? 14 : 1;
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION  * multiplier);
        return Jwts.builder()
                .subject(userEntity.getUsername())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}