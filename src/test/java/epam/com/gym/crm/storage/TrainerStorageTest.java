package epam.com.gym.crm.storage;

import epam.com.gym.crm.model.Trainer;
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

class TrainerStorageTest {

    private TrainerStorage storage;

    @BeforeEach
    void setUp() {
        storage = new TrainerStorage();
        ReflectionTestUtils.setField(storage, "trainerPath", "fakePath.csv");
    }

    @Test
    void init_shouldLoadTrainersFromCsv() {
        String fakePath = "fakePath.csv";

        try (MockedStatic<FileReaderUtil> fileReaderMock = Mockito.mockStatic(FileReaderUtil.class);
             MockedStatic<CsvParserUtil> csvParserMock = Mockito.mockStatic(CsvParserUtil.class)) {

            List<String> csvLines = Arrays.asList(
                    "1,John,Doe,john.doe,password,STRENGTH",
                    "2,Jane,Smith,jane.smith,password,CARDIO"
            );

            fileReaderMock.when(() -> FileReaderUtil.readFromCsv(fakePath))
                    .thenReturn(csvLines);

            Trainer t1 = new Trainer(); t1.setId(1L);
            Trainer t2 = new Trainer(); t2.setId(2L);

            csvParserMock.when(() -> CsvParserUtil.parseTrainers(csvLines))
                    .thenReturn(Arrays.asList(t1, t2));

            storage.init();

            List<Trainer> all = storage.findAll();
            assertEquals(2, all.size());
            assertTrue(all.contains(t1));
            assertTrue(all.contains(t2));
        }
    }

    @Test
    void existsByUsername_shouldReturnTrueIfExists() {
        Trainer trainer = new Trainer();
        trainer.setUsername("trainer.one");
        storage.save(trainer);

        assertTrue(storage.existsByUsername("trainer.one"));
        assertTrue(storage.existsByUsername("TRAINER.ONE"));
    }

    @Test
    void existsByUsername_shouldReturnFalseIfNotExists() {
        assertFalse(storage.existsByUsername("unknown.user"));
    }
}
