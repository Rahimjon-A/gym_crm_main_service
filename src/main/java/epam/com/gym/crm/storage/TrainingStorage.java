package epam.com.gym.crm.storage;

import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.util.CsvParserUtil;
import epam.com.gym.crm.util.FileReaderUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingStorage extends GenericInMemoryStorage<Training> {
    private static final Logger LOG = LoggerFactory.getLogger(TrainingStorage.class);

    @Value("${storage.training.path}")
    private String trainingPath;

    @PostConstruct
    public void init() {
        LOG.info("Initializing TrainingStorage from {}...", trainingPath);
        List<String> lines = FileReaderUtil.readFromCsv(trainingPath);
        LOG.debug("Read {} lines from {}", lines.size(), trainingPath);
        CsvParserUtil.parseTrainings(lines).forEach(this::save);
        LOG.info("TrainingStorage initialized, {} records saved", findAll().size());
    }

}
