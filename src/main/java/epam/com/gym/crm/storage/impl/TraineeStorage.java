package epam.com.gym.crm.storage.impl;

import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.storage.GenericInMemoryStorage;
import epam.com.gym.crm.util.CsvParserUtil;
import epam.com.gym.crm.util.FileReaderUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TraineeStorage extends GenericInMemoryStorage<Trainee> {
    private static final Logger LOG = LoggerFactory.getLogger(TraineeStorage.class);

    @Value("${storage.trainee.path}")
    private String traineePath;

    @PostConstruct
    public void init() {
        LOG.info("Initializing TraineeStorage from {}...", traineePath);
        List<String> lines = FileReaderUtil.readFromCsv(traineePath);
        LOG.debug("Read {} lines from {}", lines.size(), traineePath);
        CsvParserUtil.parseTrainees(lines).forEach(this::create);
        LOG.info("TraineeStorage initialized, {} records saved", findAll().size());
    }

    public boolean existsByUsername(String username) {
        return this.findAll().stream()
                .anyMatch(t -> t.getUsername() != null && t.getUsername().equalsIgnoreCase(username));
    }
}
