package epam.com.gym.crm.config;

import epam.com.gym.crm.model.common.TrainerWorkloadMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.JacksonJsonMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class ActiveMQConfig {
    public static final String TRAINER_WORKLOAD_CLASS_NAME = "TrainerWorkloadMessage";
    public static final String PROPERTY_TYPE = "_type";

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName(PROPERTY_TYPE);
        
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put(TRAINER_WORKLOAD_CLASS_NAME, TrainerWorkloadMessage.class);
        converter.setTypeIdMappings(typeIdMappings);
        
        return converter;
    }
}
