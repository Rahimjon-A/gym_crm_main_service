package epam.com.gym.crm.storage;

import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.util.CsvParserUtil;
import epam.com.gym.crm.util.FileReaderUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TraineeStorage extends UserStorage<Trainee> {
    @Value("${storage.trainee.path}")
    private String traineePath;

    @PostConstruct
    public void init() {
        List<String> lines = FileReaderUtil.readFromCsv(traineePath);
        CsvParserUtil.parseTrainees(lines).forEach(this::save);
    }

}
