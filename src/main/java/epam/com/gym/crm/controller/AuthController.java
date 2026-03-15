package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.trainee.TraineeDTO;
import epam.com.gym.crm.dto.trainer.TrainerDTO;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
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
@Tag(name = "Authentication API", description = "Endpoints for user registration, login, and password management")
public class AuthController {

    private GymFacade gymFacade;

    @Autowired
    public void setGymFacade(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    /**
     * 1. Trainee Registration (POST method)
     */
    @PostMapping("/trainee/registration")
    @Operation(summary = "Register a new Trainee profile", description = "Generates and returns username and password")
    public ResponseEntity<Credentials> registerTrainee(@Valid @RequestBody TraineeDTO request) {
        log.info("REST: Registering new Trainee profile for {} {}", request.getFirstName(), request.getLastName());
        request.setIsActive(true);
        Trainee trainee = gymFacade.createTrainee(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Credentials(trainee.getUsername(), trainee.getPassword()));
    }

    /**
     * 2. Trainer Registration (POST method)
     */
    @PostMapping("/trainer/registration")
    @Operation(summary = "Register a new Trainer profile", description = "Generates and returns username and password")
    public ResponseEntity<Credentials> registerTrainer(@Valid @RequestBody TrainerDTO request) {
        log.info("REST: Registering new Trainer profile for {} {}", request.getFirstName(), request.getLastName());
        request.setIsActive(true);
        Trainer trainer = gymFacade.createTrainer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Credentials(trainer.getUsername(), trainer.getPassword()));
    }

    /**
     * 3. Login (POST)
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Validates username and password, returns 200 OK if successful")
    public ResponseEntity<Void> login(@Valid @RequestBody Credentials credentials) {
        log.info("REST: Login attempt for user: {}", credentials.username());
        
        gymFacade.login(credentials);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
