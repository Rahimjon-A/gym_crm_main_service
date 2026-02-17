package epam.com.gym.crm.storage;

import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.util.CsvParserUtil;
import epam.com.gym.crm.util.FileReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainingStorageTest {

    private TrainingStorage storage;

    @BeforeEach
    void setUp() {
        storage = new TrainingStorage();
        ReflectionTestUtils.setField(storage, "trainingPath", "fakePath.csv");
    }

    @Test
    void init_shouldLoadTrainingsFromCsv() {
        String fakePath = "fakePath.csv";

        try (MockedStatic<FileReaderUtil> fileReaderMock = Mockito.mockStatic(FileReaderUtil.class);
             MockedStatic<CsvParserUtil> csvParserMock = Mockito.mockStatic(CsvParserUtil.class)) {

            List<String> csvLines = Arrays.asList(
                    "1,1,2,LegDay,YOGA,2026-02-17,60.0",
                    "2,2,1,Cardio,CARDIO,2026-02-18,45.0"
            );

            fileReaderMock.when(() -> FileReaderUtil.readFromCsv(fakePath))
                    .thenReturn(csvLines);

            Training tr1 = new Training(); tr1.setId(1L);
            Training tr2 = new Training(); tr2.setId(2L);

            csvParserMock.when(() -> CsvParserUtil.parseTrainings(csvLines))
                    .thenReturn(Arrays.asList(tr1, tr2));

            storage.init();

            List<Training> all = storage.findAll();
            assertEquals(2, all.size());
            assertTrue(all.contains(tr1));
            assertTrue(all.contains(tr2));
        }
    }
}
