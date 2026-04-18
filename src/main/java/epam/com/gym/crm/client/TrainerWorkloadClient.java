package epam.com.gym.crm.client;

import epam.com.gym.crm.dto.request.trainer.TrainerWorkloadRequest;
import epam.com.gym.crm.fallback.TrainerWorkloadClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gym-trainer-workload",
        fallbackFactory = TrainerWorkloadClientFallbackFactory.class
)
public interface TrainerWorkloadClient {

    @PostMapping("/api/v1/trainers/workloads")
    ResponseEntity<Void> addTraining(@RequestBody TrainerWorkloadRequest request);

    @DeleteMapping("/api/v1/trainers/workloads")
    ResponseEntity<Void> deleteTraining(@RequestBody TrainerWorkloadRequest request);

}
