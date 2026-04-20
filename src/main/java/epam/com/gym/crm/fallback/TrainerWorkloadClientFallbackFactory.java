package epam.com.gym.crm.fallback;

import epam.com.gym.crm.client.TrainerWorkloadClient;
import epam.com.gym.crm.dto.request.trainer.TrainerWorkloadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainerWorkloadClientFallbackFactory implements FallbackFactory<TrainerWorkloadClient> {

    @Override
    public TrainerWorkloadClient create(Throwable cause) {
        return new TrainerWorkloadClient() {

            @Override
            public ResponseEntity<Void> addTraining(String transactionId, TrainerWorkloadRequest request) {
                log.error("Fallback: addTraining failed for trainer: {} — {}",
                        request.getUsername(), cause.getMessage(), cause);
                return ResponseEntity.ok().build();
            }

            @Override
            public ResponseEntity<Void> deleteTraining(String transactionId, TrainerWorkloadRequest request) {
                log.error("Fallback: deleteTraining failed for trainer: {} — {}",
                        request.getUsername(), cause.getMessage(), cause);
                return ResponseEntity.ok().build();
            }
        };
    }
}
