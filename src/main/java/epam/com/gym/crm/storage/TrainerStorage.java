package epam.com.gym.crm.storage;

import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.util.CsvParserUtil;
import epam.com.gym.crm.util.FileReaderUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainerStorage extends UserStorage<Trainer> {
    @Value("${storage.trainer.path}")
    private String trainerPath;

    @PostConstruct
    public void init() {
        List<String> lines = FileReaderUtil.readFromCsv(trainerPath);
        CsvParserUtil.parseTrainers(lines).forEach(this::save);
    }
}
