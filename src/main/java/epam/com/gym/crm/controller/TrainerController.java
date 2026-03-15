package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.dto.trainer.TrainerDTO;
import epam.com.gym.crm.dto.trainer.TrainerResponseDTO;
import epam.com.gym.crm.dto.trainer.TrainerShortDTO;
import epam.com.gym.crm.dto.trainer.TrainerUpdateDTO;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TrainerMapper;
import epam.com.gym.crm.model.Trainer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * Get Trainer Profile (GET method)
     */
    @GetMapping("/{username}")
    @Operation(summary = "Get Trainer Profile", description = "Returns trainer details and their assigned trainees")
    public ResponseEntity<TrainerResponseDTO> getTrainerProfile(@PathVariable String username) {
        log.info("REST: Fetching profile for trainer: {}", username);
        
        Trainer trainer = gymFacade.getTrainerByUserName(username);
        TrainerResponseDTO response = trainerMapper.toProfileResponse(trainer);

        return ResponseEntity.ok(response);
    }

    /**
     * Update Trainer Profile (PUT method)
     */
    @PutMapping("/{username}")
    @Operation(summary = "Update Trainer profile", description = "Updates trainer and returns the updated profile with trainees")
    public ResponseEntity<TrainerUpdateDTO> updateTrainerProfile(
            @PathVariable String username,
            @Valid @RequestBody TrainerDTO updateRequest) {
        log.info("REST: Updating profile for trainer: {}", username);
        
        Trainer updatedTrainer = gymFacade.updateTrainer(username, updateRequest);
        TrainerUpdateDTO response = trainerMapper.toUpdateResponse(updatedTrainer);

        return ResponseEntity.ok(response);
    }

    /**
     * Get not assigned on trainee active trainers (GET method)
     */
    @GetMapping("/not-assigned-on/{traineeUsername}")
    @Operation(summary = "Get unassigned trainers", description = "Returns active trainers not assigned to the given trainee")
    public ResponseEntity<List<TrainerShortDTO>> getUnassignedTrainers(
            @PathVariable String traineeUsername) {
        log.info("REST: Fetching unassigned trainers for trainee: {}", traineeUsername);

        List<Trainer> unassignedTrainers = gymFacade.getUnassignedTrainersOfTrainee(traineeUsername);
        List<TrainerShortDTO> response = trainerMapper.toShortDTOList(unassignedTrainers);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{username}/status")
    @Operation(summary = "Activate/Deactivate trainers", description = "Toggles the active status of a trainer profile")
    public ResponseEntity<Void> toggleTrainerStatus(
            @PathVariable String username,
            @RequestParam boolean isActive) {
        log.info("REST: Toggling status for trainer {} to {}", username, isActive);

        if (isActive) {
            gymFacade.deactivateTrainer(username);
        } else {
            gymFacade.activateTrainer(username);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Change Login Password (PUT method)
     */
    @PutMapping("/password")
    @Operation(summary = "Change User Password", description = "Updates password if old credentials are valid")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        log.info("REST: Password change attempt for user: {}", request.username());

        gymFacade.changePassword(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
