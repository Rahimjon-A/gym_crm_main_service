package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.dto.trainee.TraineeDTO;
import epam.com.gym.crm.dto.trainee.TraineeResponseDTO;
import epam.com.gym.crm.dto.trainee.TraineeTrainerUpdateListDTO;
import epam.com.gym.crm.dto.trainee.TraineeUpdateDTO;
import epam.com.gym.crm.dto.trainer.TrainerShortDTO;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TraineeMapper;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Training;
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
@RequestMapping("/api/v1/trainees")
@Tag(name = "Trainee API", description = "Endpoints for managing Gym Trainees")
public class TraineeController {

    private GymFacade gymFacade;
    private TraineeMapper traineeMapper;

    @Autowired
    public void setGymFacade(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @Autowired
    public void setTraineeMapper(TraineeMapper traineeMapper) {
        this.traineeMapper = traineeMapper;
    }

    /**
     * 5. Get Trainee Profile (GET method)
     */
    @GetMapping("/{username}")
    @Operation(summary = "Get Trainee Profile", description = "Returns trainee details and their assigned trainers")
    public ResponseEntity<TraineeResponseDTO> getTraineeProfile(@PathVariable String username) {
        log.info("REST: Fetching profile for trainee: {}", username);

        Trainee trainee = gymFacade.getTraineeByUsername(username);
        TraineeResponseDTO response = traineeMapper.toProfileResponse(trainee);

        return ResponseEntity.ok(response);
    }

    /**
     * Update Trainee Profile (PUT method)
     */
    @PutMapping("/{username}")
    @Operation(summary = "Update Trainee profile", description = "Updates trainee and returns the updated profile with trainers")
    public ResponseEntity<TraineeUpdateDTO> updateTraineeProfile(
            @PathVariable String username,
            @Valid @RequestBody TraineeDTO updateRequest) {
        log.info("REST: Updating profile for trainee: {}", username);

        Trainee updatedTrainee = gymFacade.updateTrainee(username, updateRequest);
        TraineeUpdateDTO response = traineeMapper.toUpdateResponse(updatedTrainee);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}/trainers")
    @Operation(summary = "Update Trainee's Trainers", description = "Updates the assigned trainers for specific training sessions")
    public ResponseEntity<List<TrainerShortDTO>> updateTraineeTrainers(
            @PathVariable String username,
            @Valid @RequestBody TraineeTrainerUpdateListDTO request) {
        log.info("REST: Updating trainer list for trainee: {}", username);

        List<Training> updatedTrainings = gymFacade.updateTraineeTrainings(username, request.getAssignments());

        Trainee tempTrainee = new Trainee();
        tempTrainee.setTrainings(updatedTrainings);
        List<TrainerShortDTO> response = traineeMapper.extractTrainers(tempTrainee);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete Trainee profile", description = "Deletes trainee (hard deletes) and returns 200 OK if successful")
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable String username) {
        log.info("REST: Deleting  trainee, username: {}", username);

        gymFacade.deleteTrainee(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Update Trainee Status (PATCH method)
     */
    @PatchMapping("/{username}/status")
    @Operation(summary = "Activate or Deactivate Trainee", description = "Toggles the active status of a trainee profile")
    public ResponseEntity<Void> toggleTraineeStatus(
            @PathVariable String username,
            @RequestParam boolean isActive) {
        log.info("REST: Toggling status for trainee {} to {}", username, isActive);

        if (isActive) {
            gymFacade.activateTrainee(username);
        } else {
            gymFacade.deactivateTrainee(username);
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
