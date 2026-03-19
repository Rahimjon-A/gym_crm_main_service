package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.request.trainer.TrainerCreateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerUpdateRequest;
import epam.com.gym.crm.dto.response.trainer.TrainerResponse;
import epam.com.gym.crm.dto.response.trainer.TrainerUpdateResponse;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TrainerMapper;
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
@RequestMapping("/api/v1/trainers")
@Tag(name = "Trainer API", description = "Endpoints for managing Gym Trainers")
public class TrainerController {
    private GymFacade gymFacade;
    private TrainerMapper trainerMapper;

    @Autowired
    public void setGymFacade(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @Autowired
    public void setTrainerMapper(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    @PostMapping
    @Operation(summary = "Register a new Trainer profile", description = "Generates and returns username and password")
    public ResponseEntity<Credentials> registerTrainer(@Valid @RequestBody TrainerCreateRequest request) {
        log.info("REST: Registering new Trainer profile for {} {}", request.getFirstName(), request.getLastName());
        request.setIsActive(true);
        Trainer trainer = gymFacade.createTrainer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new Credentials(trainer.getUsername(), trainer.getPassword()));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get Trainer Profile", description = "Returns trainer details and their assigned trainees")
    public ResponseEntity<TrainerResponse> getTrainerProfile(@PathVariable String username) {
        log.info("REST: Fetching profile for trainer: {}", username);
        
        Trainer trainer = gymFacade.getTrainerByUserName(username);
        TrainerResponse response = trainerMapper.toProfileResponse(trainer);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update Trainer profile", description = "Updates trainer and returns the updated profile with trainees")
    public ResponseEntity<TrainerUpdateResponse> updateTrainerProfile(
            @PathVariable String username,
            @Valid @RequestBody TrainerUpdateRequest updateRequest) {
        log.info("REST: Updating profile for trainer: {}", username);
        
        Trainer updatedTrainer = gymFacade.updateTrainer(username, updateRequest);
        TrainerUpdateResponse response = trainerMapper.toUpdateResponse(updatedTrainer);

        return ResponseEntity.ok(response);
    }
}
