package epam.com.gym.crm.storage;

import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.util.CsvParserUtil;
import epam.com.gym.crm.util.FileReaderUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerStorage extends UserStorage<Trainer> {
    private static final Logger LOG = LoggerFactory.getLogger(TrainerStorage.class);

    @Value("${storage.trainer.path}")
    private String trainerPath;

    @PostConstruct
    public void init() {
        LOG.info("Initializing TrainerStorage from {}...", trainerPath);
        List<String> lines = FileReaderUtil.readFromCsv(trainerPath);
        LOG.debug("Read {} lines from {}", lines.size(), trainerPath);
        CsvParserUtil.parseTrainers(lines).forEach(this::save);
        LOG.info("TrainerStorage initialized, {} records saved", findAll().size());
    }
}
