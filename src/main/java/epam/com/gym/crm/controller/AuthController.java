package epam.com.gym.crm.controller;

import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.model.common.Credentials;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> login(@Valid @RequestBody Credentials credentials) {
        log.info("REST: Login attempt for user: {}", credentials.getUsername());
        
        gymFacade.login(credentials);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
