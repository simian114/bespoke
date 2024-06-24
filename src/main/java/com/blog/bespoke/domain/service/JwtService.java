package com.blog.bespoke.domain.service;

import com.blog.bespoke.domain.model.user.User;
import com.blog.bespoke.domain.model.user.role.Role;
import com.blog.bespoke.domain.model.user.role.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService {
    public static final String CLAIM_ID = "id";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_NAME = "name";
    public static final String CLAIM_NICKNAME = "nickname";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_STATUS = "status";
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_SCHEME = "Bearer";

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
                .claim(CLAIM_STATUS, user.getStatus())
                .claim(CLAIM_ROLES, user.getRolesAsString())
                // .claim(CLAIM_ROLES, user.getRoleAuthorities())
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public User parseAccessTokenToUser(String accessToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        List<String> roles = (List) claims.get(JwtService.CLAIM_ROLES);
        Set<UserRole> userRoles = roles.stream()
                .map(role -> UserRole.builder().role(Role.builder().code(Role.CODE.valueOf(role)).build()).build())
                .collect(Collectors.toSet());
        return User.builder()
                .id(claims.get(JwtService.CLAIM_ID, Long.class))
                .email(claims.get(JwtService.CLAIM_EMAIL, String.class))
                .name(claims.get(JwtService.CLAIM_NAME, String.class))
                .nickname(claims.get(JwtService.CLAIM_NICKNAME, String.class))
                .status(User.STATUS.valueOf(claims.get(JwtService.CLAIM_STATUS, String.class)))
                .roles(userRoles)
                .build();
    }

    public void checkAccessTokenValidity(String accessToken) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(accessToken);
    }
}
