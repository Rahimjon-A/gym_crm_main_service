package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.response.TrainingTypeResponse;
import epam.com.gym.crm.facade.GymFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/training-types")
@Tag(name = "Training Type API", description = "Endpoints for retrieving Gym Training Types")
public class TrainingTypeController {

    private GymFacade gymFacade;

    @Autowired
    public void setGymFacade(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @GetMapping
    @Operation(summary = "Get all Training Types", description = "Returns a list of all available training types in the gym")
    public ResponseEntity<List<TrainingTypeResponse>> getAllTrainingTypes() {
        log.info("REST: Fetching all training types");

        List<TrainingTypeResponse> response = gymFacade.getAllTrainingTypes().stream()
                .map(type -> new TrainingTypeResponse(
                        type.getId(),
                        type.getTrainingTypeName()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }
}
