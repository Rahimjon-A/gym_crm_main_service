package epam.com.gym.crm.filter;

import epam.com.gym.crm.service.JwtService;
import epam.com.gym.crm.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String PREFIX_BEARER = "Bearer ";
    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth",
            "/api/v1/trainees",
            "/api/v1/trainers",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/**",
            "/favicon.ico",
            "/api/v1/sync/**"
    };

    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setTokenBlacklistService(TokenBlacklistService tokenBlacklistService) {
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return Arrays.stream(AUTH_WHITELIST)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JwtAuthFilter processing: {} {}", request.getMethod(), request.getRequestURI());

        String jwt = extractToken(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (isTokenBlacklisted(jwt, request, response)) return;
            authenticateIfRequired(jwt, request);
        } catch (Exception e) {
            log.error("JWT processing failed for path: {} — {}", request.getRequestURI(), e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HEADER_AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(PREFIX_BEARER)) {
            log.error("No Bearer token found for path: {}", request.getRequestURI());
            return null;
        }

        return authHeader.substring(PREFIX_BEARER.length());
    }

    private boolean isTokenBlacklisted(String jwt, HttpServletRequest request, HttpServletResponse response) {
        if (tokenBlacklistService.isBlacklisted(jwt)) {
            log.warn("Rejected blacklisted token for path: {}", request.getRequestURI());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return true;
        }

        return false;
    }

    private void authenticateIfRequired(String jwt, HttpServletRequest request) {
        String username = jwtService.extractUsername(jwt);
        log.debug("JWT token found for username: {}", username);

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        if (jwtService.isServiceToken(jwt)) {
            UserDetails serviceDetails = User.builder()
                    .username(username)
                    .password("")
                    .authorities(List.of())
                    .build();

            setAuthentication(serviceDetails, request);
            log.debug("System Service authenticated: {}", username);
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtService.isTokenValid(jwt, userDetails)) {
            setAuthentication(userDetails, request);
            log.debug("JWT authentication successful for user: {}", username);
        } else {
            log.warn("JWT token invalid or expired for user: {}", username);
        }
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
