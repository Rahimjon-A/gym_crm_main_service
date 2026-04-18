package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.exception.AuthenticationException;
import epam.com.gym.crm.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    public static final String SERVICE_TOKEN_CLAIM = "isServiceToken";
    private static final String SERVICE_USERNAME = "gym-crm-main";
    private static final String PREFIX_BEARER    = "Bearer ";

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateServiceToken() {
        log.debug("Generating service token for inter-service call");

        UserDetails serviceUser = User
                .builder()
                .username(SERVICE_USERNAME)
                .password("")
                .authorities(List.of())
                .build();

        return PREFIX_BEARER + this.generateToken(serviceUser);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (ExpiredJwtException | SignatureException | MalformedJwtException | IllegalArgumentException e) {
            log.error("Token validation failed for user {}", userDetails.getUsername() , e);
            return false;
        }
    }

    @Override
    public boolean isServiceToken(String token) {
        try {
            Boolean isService = extractClaim(token, claims -> claims.get(SERVICE_TOKEN_CLAIM, Boolean.class));
            return isService != null && isService;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claimsResolver.apply(claims);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", token, e);
            throw new AuthenticationException("Invalid JWT token provided!");
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
