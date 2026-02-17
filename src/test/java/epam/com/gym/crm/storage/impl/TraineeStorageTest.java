package epam.com.gym.crm.storage.impl;

import epam.com.gym.crm.model.Trainee;
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

class TraineeStorageTest {

    private TraineeStorage storage;

    @BeforeEach
    void setUp() {
        storage = new TraineeStorage();
        ReflectionTestUtils.setField(storage, "traineePath", "fakePath.csv");
    }

    @Test
    void init_shouldLoadTraineesFromCsv() {
        String fakePath = "fakePath.csv";

        try (MockedStatic<FileReaderUtil> fileReaderMock = Mockito.mockStatic(FileReaderUtil.class);
             MockedStatic<CsvParserUtil> csvParserMock = Mockito.mockStatic(CsvParserUtil.class)) {

            List<String> csvLines = Arrays.asList(
                    "John,Doe,john.doe,password,2000-01-01,Address1",
                    "Jane,Smith,jane.smith,password,2001-02-02,Address2"
            );

            fileReaderMock.when(() -> FileReaderUtil.readFromCsv(fakePath))
                    .thenReturn(csvLines);

            Trainee t1 = new Trainee();
            Trainee t2 = new Trainee();

            csvParserMock.when(() -> CsvParserUtil.parseTrainees(csvLines))
                    .thenReturn(Arrays.asList(t1, t2));

            storage.init();

            List<Trainee> all = storage.findAll();
            assertEquals(2, all.size());
            assertTrue(all.contains(t1));
            assertTrue(all.contains(t2));
        }
    }

    @Test
    void existsByUsername_shouldReturnTrueIfExists() {
        Trainee trainee = new Trainee();
        trainee.setUsername("john.doe");
        storage.save(trainee);

        assertTrue(storage.existsByUsername("john.doe"));
        assertTrue(storage.existsByUsername("JOHN.DOE"));
    }

    @Test
    void existsByUsername_shouldReturnFalseIfNotExists() {
        assertFalse(storage.existsByUsername("unknown.user"));
    }
}
