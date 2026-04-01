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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String PREFIX_BEARER = "Bearer ";

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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String authHeader = request.getHeader(HEADER_AUTHORIZATION);

        log.debug("JwtAuthFilter processing: {} {}", request.getMethod(), path);

        if (authHeader == null || !authHeader.startsWith(PREFIX_BEARER)) {
            log.debug("No Bearer token found for path: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(PREFIX_BEARER.length());

        try {
            if (tokenBlacklistService.isBlacklisted(jwt)) {
                log.warn("Rejected blacklisted token for path: {}", path);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            String username = jwtService.extractUsername(jwt);
            log.debug("JWT token found for username: {}", username);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("JWT authentication successful for user: {}", username);
                } else {
                    log.warn("JWT token invalid or expired for user: {}", username);
                }
            }
        } catch (Exception e) {
            log.error("JWT processing failed for path: {} — {}", path, e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}
