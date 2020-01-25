package com.kurbatov.todoapp.security.jwt;

import com.kurbatov.todoapp.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    public static final String USERNAME_JWT_PAYLOAD_KEY = "username";
    private static final String ROLES_JWT_PAYLOAD_KEY = "roles";

    @Value("${app.auth.jwtSecret}")
    private String jwtSecret;

    @Value("${app.auth.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put(USERNAME_JWT_PAYLOAD_KEY, userDetails.getUsername());

        List<String> userRoles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put(ROLES_JWT_PAYLOAD_KEY, userRoles);

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public Long getUserIdFromJwt(String jwtToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwtToken)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public String getUsernameFromJwt(String jwtToken) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwtToken)
                .getBody();
        return (String) claims.get(USERNAME_JWT_PAYLOAD_KEY);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            LOGGER.error("Invalid JWT token", ex);
        } catch (ExpiredJwtException ex) {
            LOGGER.error("Expired JWT token", ex);
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("Unsupported JWT token", ex);
        } catch (IllegalArgumentException ex) {
            LOGGER.error("JWT claims string is empty.", ex);
        } catch (Throwable ex) {
            LOGGER.error("Some exception related to jwt token", ex);
        }
        return false;
    }
}