package epam.com.gym.crm.client;

import epam.com.gym.crm.dto.request.trainer.TrainerWorkloadRequest;
import epam.com.gym.crm.fallback.TrainerWorkloadClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "gym-trainer-workload",
        fallbackFactory = TrainerWorkloadClientFallbackFactory.class)
public interface TrainerWorkloadClient {

    @PostMapping("/api/v1/workload")
    ResponseEntity<Void> addTraining(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TrainerWorkloadRequest request);

    @DeleteMapping("/api/v1/workload")
    ResponseEntity<Void> deleteTraining(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TrainerWorkloadRequest request);

}
