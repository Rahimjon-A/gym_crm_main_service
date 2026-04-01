package epam.com.gym.crm.controller;

import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.model.common.Credentials;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication API", description = "Endpoints for user login")
public class AuthController {

    private GymFacade gymFacade;

    @Autowired
    public void setGymFacade(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostMapping
    @Operation(summary = "Login user", description = "Validates username and password, returns 200 OK if successful")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody Credentials credentials) {
        log.info("REST: Login attempt for user: {}", credentials.getUsername());

        String token = gymFacade.login(credentials);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @DeleteMapping
    @Operation(summary = "Logout user", description = "Invalidates the JWT token")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            log.info("REST: Logout request for user: {}", username);
            gymFacade.logout(token);
        }

        return ResponseEntity.ok().build();
    }

}
