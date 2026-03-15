package epam.com.gym.crm.controller;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.training.TraineeTrainingResponseDTO;
import epam.com.gym.crm.dto.training.TrainerTrainingResponseDTO;
import epam.com.gym.crm.dto.training.TrainingDTO;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TrainingMapper;
import epam.com.gym.crm.model.Training;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/trainings")
@Tag(name = "Training API", description = "Endpoints for managing Gym Trainings")
public class TrainingController {

    private GymFacade gymFacade;
    private TrainingMapper trainingMapper;

    @Autowired
    public void setGymFacade(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @Autowired
    public void setTrainingMapper(TrainingMapper trainingMapper) {
        this.trainingMapper = trainingMapper;
    }

    /**
     * Add Training (POST method)
     */
    @PostMapping
    @Operation(summary = "Add Training", description = "Records a new training session between a trainee and trainer")
    public ResponseEntity<Void> addTraining(@Valid @RequestBody TrainingDTO request) {
        log.info("REST: Adding training '{}' for Trainee: {} and Trainer: {}", 
                request.getTrainingName(), request.getTraineeUsername(), request.getTrainerUsername());

        gymFacade.createTraining(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Get Trainee Trainings List (GET method)
     */
    @GetMapping("/trainee/{username}")
    @Operation(summary = "Get Trainee Trainings", description = "Returns a list of trainings for a specific trainee based on optional filters")
    public ResponseEntity<List<TraineeTrainingResponseDTO>> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodTo,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType) {
        log.info("REST: Fetching trainings for Trainee: {}, Filters - from: {}, to: {}, trainer: {}, type: {}",
                username, periodFrom, periodTo, trainerName, trainingType);

        TraineeTrainingFilter filter = new TraineeTrainingFilter(username, periodFrom, periodTo, trainingType, trainerName);
        List<Training> trainings = gymFacade.getTraineeTrainingsByCriteria(filter);
        List<TraineeTrainingResponseDTO> response = trainingMapper.mapTraineeTrainings(trainings);

        return ResponseEntity.ok(response);
    }

    /**
     * Get Trainer Trainings List (GET method)
     */
    @GetMapping("/trainer/{username}")
    @Operation(summary = "Get Trainer Trainings", description = "Returns a list of trainings for a specific trainer based on optional filters")
    public ResponseEntity<List<TrainerTrainingResponseDTO>> getTrainerTrainings(
            @PathVariable String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date periodTo,
            @RequestParam(required = false) String traineeName) {
        log.info("REST: Fetching trainings for Trainer: {}, Filters - from: {}, to: {}, trainee: {}",
                username, periodFrom, periodTo, traineeName);

        TrainerTrainingFilter filter = new TrainerTrainingFilter(username, periodFrom, periodTo, traineeName);
        List<Training> trainings = gymFacade.getTrainerTrainingsByCriteria(filter);
        List<TrainerTrainingResponseDTO> response = trainingMapper.mapTrainerTrainings(trainings);

        return ResponseEntity.ok(response);
    }
}
