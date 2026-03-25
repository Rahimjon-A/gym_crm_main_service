package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.request.trainee.TraineeCreateRequest;
import epam.com.gym.crm.dto.request.trainee.TraineeUpdateRequest;
import epam.com.gym.crm.dto.response.trainee.TraineeResponse;
import epam.com.gym.crm.dto.response.trainee.TraineeUpdateResponse;
import epam.com.gym.crm.dto.request.trainee.TraineeTrainerUpdateListRequest;
import epam.com.gym.crm.dto.response.trainer.TrainerShortResponse;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TraineeMapper;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.common.Credentials;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
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

    private static final String METRIC_TRAINEE_TIMER = "gym.trainee.creation.time";
    private static final String METRIC_TIMER_DESC = "Time taken to save a new trainee profile";

    private GymFacade gymFacade;
    private TraineeMapper traineeMapper;
    private MeterRegistry meterRegistry;

    @Autowired
    public void setGymFacade(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @Autowired
    public void setTraineeMapper(TraineeMapper traineeMapper) {
        this.traineeMapper = traineeMapper;
    }

    @Autowired
    public void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    @Operation(summary = "Register a new Trainee profile", description = "Generates and returns username and password")
    public ResponseEntity<Credentials> registerTrainee(@Valid @RequestBody TraineeCreateRequest request) {
        Timer timer = Timer.builder(METRIC_TRAINEE_TIMER)
                .description(METRIC_TIMER_DESC)
                .register(meterRegistry);

        log.info("REST: Registering new Trainee profile for {} {}", request.getFirstName(), request.getLastName());
        request.setIsActive(true);
        return timer.record(() -> {
            Trainee trainee = gymFacade.createTrainee(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new Credentials(trainee.getUsername(), trainee.getPassword()));
        });
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get Trainee Profile", description = "Returns trainee details and their assigned trainers")
    public ResponseEntity<TraineeResponse> getTraineeProfile(@PathVariable String username) {
        log.info("REST: Fetching profile for trainee: {}", username);

        Trainee trainee = gymFacade.getTraineeByUsername(username);
        TraineeResponse response = traineeMapper.toProfileResponse(trainee);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Update Trainee profile", description = "Updates trainee and returns the updated profile with trainers")
    public ResponseEntity<TraineeUpdateResponse> updateTraineeProfile(
            @PathVariable String username,
            @Valid @RequestBody TraineeUpdateRequest updateRequest) {
        log.info("REST: Updating profile for trainee: {}", username);

        Trainee updatedTrainee = gymFacade.updateTrainee(username, updateRequest);
        TraineeUpdateResponse response = traineeMapper.toUpdateResponse(updatedTrainee);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{username}/trainers")
    @Operation(summary = "Update Trainee's Trainers", description = "Updates the assigned trainers for specific training sessions")
    public ResponseEntity<List<TrainerShortResponse>> updateTraineeTrainers(
            @PathVariable String username,
            @Valid @RequestBody TraineeTrainerUpdateListRequest request) {
        log.info("REST: Updating trainer list for trainee: {}", username);

        List<Training> updatedTrainings = gymFacade.updateTraineeTrainings(username, request.getAssignments());

        Trainee tempTrainee = new Trainee();
        tempTrainee.setTrainings(updatedTrainings);
        List<TrainerShortResponse> response = traineeMapper.extractTrainers(tempTrainee);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}/unassigned-trainers")
    @Operation(summary = "Get unassigned trainers", description = "Returns active trainers not assigned to the given trainee")
    public ResponseEntity<List<TrainerShortResponse>> getUnassignedTrainers(
            @PathVariable String username) {
        log.info("REST: Fetching unassigned trainers for trainee: {}", username);

        List<Trainer> unassignedTrainers = gymFacade.getUnassignedTrainersOfTrainee(username);
        List<TrainerShortResponse> response = traineeMapper.toTrainerShortDTOList(unassignedTrainers);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete Trainee profile", description = "Deletes trainee (hard deletes) and returns 200 OK if successful")
    public ResponseEntity<Void> deleteTraineeProfile(@PathVariable String username) {
        log.info("REST: Deleting  trainee, username: {}", username);

        gymFacade.deleteTrainee(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
