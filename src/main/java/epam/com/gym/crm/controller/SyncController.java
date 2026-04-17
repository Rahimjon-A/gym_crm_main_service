package epam.com.gym.crm.controller;

import epam.com.gym.crm.dto.response.training.TrainingWorkloadResponse;
import epam.com.gym.crm.facade.GymFacade;
import epam.com.gym.crm.mapper.TrainingMapper;
import epam.com.gym.crm.model.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/sync")
public class SyncController {

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

    @GetMapping("/trainings")
    public ResponseEntity<List<TrainingWorkloadResponse>> getAllTrainings() {
        log.info("Sync request received — returning all trainings");

        List<Training> trainings = gymFacade.getAllTrainings();
        List<TrainingWorkloadResponse> response = trainingMapper.toTrainingResponse(trainings);

        log.debug("Sync returning {} training records", response.size());
        return ResponseEntity.ok(response);
    }
}
