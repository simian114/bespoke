package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    public static final String CLAIM_ID = "id";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_NAME = "name";
    public static final String CLAIM_NICKNAME = "nickname";

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Value("${JWT_SECRET_KEY}")
    private String jwtSecretKey;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(jwtSecretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAccessToken(User user) {
        Date date = new Date();
        Date expiredDate = new Date(date.getTime() + 1000 * 60 * 60 * 24);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(expiredDate)
                .setIssuedAt(date)
                .claim(CLAIM_ID, user.getId())
                .claim(CLAIM_EMAIL, user.getEmail())
                .claim(CLAIM_NAME, user.getName())
                .claim(CLAIM_NICKNAME, user.getNickname())
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public User parseAccessTokenToUser(String accessToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        return User.builder()
                .id(claims.get(JwtService.CLAIM_ID, Long.class))
                .email(claims.get(JwtService.CLAIM_EMAIL, String.class))
                .name(claims.get(JwtService.CLAIM_NAME, String.class))
                .nickname(claims.get(JwtService.CLAIM_NICKNAME, String.class))
                .build();
    }
}
