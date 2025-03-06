package com.lawmon.lawmon.security;

import com.lawmon.lawmon.Entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("⚠️ JWT 토큰이 만료되었습니다.");
        } catch (MalformedJwtException e) {
            System.out.println("⚠️ JWT 토큰 형식이 잘못되었습니다.");
        } catch (SignatureException e) {
            System.out.println("⚠️ JWT 서명이 유효하지 않습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("⚠️ JWT 토큰이 비어 있거나 잘못되었습니다.");
        }
        return false;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Role extractRole(String token) {
        try {
            return Role.valueOf(extractClaim(token, claims -> claims.get("role", String.class)));
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println("⚠️ JWT 토큰에서 역할(role) 추출 실패: " + e.getMessage());
            return null;
        }
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }
}