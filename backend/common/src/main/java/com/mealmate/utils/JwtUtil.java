package com.mealmate.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
    /**
     * Generates a JWT
     * Uses HMAC-SHA algorithm with a fixed secret key
     *
     * @param secretKey jwt secret key
     * @param ttlMillis jwt expiration time in milliseconds
     * @param claims    information to be set
     * @return the generated JWT
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        // Generate the JWT's expiration date
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder()
                .claims(claims)
                .signWith(key)
                .expiration(exp);

        return builder.compact();
    }

    /**
     * Decrypts the token
     *
     * @param secretKey jwt secret key. This key must be kept securely on the server and should not be exposed, otherwise the signature can be forged. If integrating with multiple clients, it is recommended to modify this method accordingly.
     * @param token     encrypted token
     * @return the claims extracted from the token
     */
    public static Claims parseJWT(String secretKey, String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token).getPayload();
        return claims;
    }

}
