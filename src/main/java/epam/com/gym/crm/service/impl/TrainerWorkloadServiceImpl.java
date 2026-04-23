package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.model.common.TrainerWorkloadMessage;
import epam.com.gym.crm.filter.TransactionLoggingFilter;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.service.TrainerWorkloadService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {
    private JmsTemplate jmsTemplate;

    @Value("${workload.queue.name}")
    private String WORKLOAD_QUEUE;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public void notifyAdd(Trainer trainer, Training training) {
        log.info("Sending workload ADD message to ActiveMQ for trainer: {}", trainer.getUsername());
        sendMessage(buildMessage(trainer, training, TrainerWorkloadMessage.ActionType.ADD));
    }

    @Override
    public void notifyDelete(Trainer trainer, Training training) {
        log.info("Sending DELETE message to queue for trainer: {}", trainer.getUsername());
        sendMessage(buildMessage(trainer, training, TrainerWorkloadMessage.ActionType.DELETE));
    }

    private void sendMessage(TrainerWorkloadMessage payload) {
        String transactionId = MDC.get(TransactionLoggingFilter.TRANSACTION_ID_KEY);

        try {
            jmsTemplate.convertAndSend(WORKLOAD_QUEUE, payload, message -> {
                if (transactionId != null) {
                    message.setStringProperty(TransactionLoggingFilter.TRANSACTION_ID_HEADER, transactionId);
                }
                return message;
            });

            log.debug("Message sent successfully for trainer: {}", payload.getUsername());

        } catch (Exception e) {
            log.error("Failed to send message to queue for trainer: {} — {}", payload.getUsername(), e.getMessage(), e);
        }
    }

    private TrainerWorkloadMessage buildMessage(Trainer trainer,
                                                Training training,
                                                TrainerWorkloadMessage.ActionType actionType) {
        return TrainerWorkloadMessage.builder()
                .username(trainer.getUsername())
                .firstName(trainer.getFirstName())
                .lastName(trainer.getLastName())
                .isActive(trainer.isActive())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .actionType(actionType)
                .build();
    }
}
