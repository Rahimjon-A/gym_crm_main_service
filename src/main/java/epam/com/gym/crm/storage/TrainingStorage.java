package epam.com.gym.crm.storage;

import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.util.CsvParserUtil;
import epam.com.gym.crm.util.FileReaderUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TrainingStorage extends GenericInMemoryStorage<Training> {
    @Value("${storage.training.path}")
    private String trainingPath;

    @PostConstruct
    public void init() {
        List<String> lines = FileReaderUtil.readFromCsv(trainingPath);
        CsvParserUtil.parseTrainings(lines).forEach(this::save);
    }

}
