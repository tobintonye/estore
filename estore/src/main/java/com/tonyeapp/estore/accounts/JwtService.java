package com.tonyeapp.estore.accounts;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.security.Key;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

@Component
public class JwtService {
    @Value("${jwt.access}")
    private String accessSecret;

    @Value("${jwt.refresh}")
    private String refreshSecret;   

    private final long ACCESS_EXPIRATION = 1000 * 60 * 15; // 15 mins
    
    private final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7 days

    public String generateAccessToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email, ACCESS_EXPIRATION, getAccessKey());
    }

    public String generateRefreshToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, email, REFRESH_EXPIRATION, getRefreshKey());
    }

    private String createToken(Map<String, Object> claims, String email, long expiration, Key key) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getAccessKey() {
        byte[] keyBytes = Decoders.BASE64.decode(accessSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getRefreshKey() {
        byte[] keyBytes = Decoders.BASE64.decode(refreshSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getSubject, isRefreshToken);
    }

    public Date extractExpiration(String token, boolean isRefreshToken) {
        return extractClaim(token, Claims::getExpiration, isRefreshToken );
    }

     public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, boolean isRefreshToken) {
        final Claims claims = extractAllClaims(token, isRefreshToken);
        return claimsResolver.apply(claims);
    }

     private Claims extractAllClaims(String token, boolean isRefreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(isRefreshToken ? getRefreshKey() : getAccessKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

     private Boolean isTokenExpired(String token,  boolean isRefreshToken) {
        return extractExpiration(token, isRefreshToken).before(new Date());
    }

    public Boolean validateAccessToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token, false);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token, false));
    }

    public boolean validateRefreshToken(String token) {
        return !isTokenExpired(token, true);
    }
}
