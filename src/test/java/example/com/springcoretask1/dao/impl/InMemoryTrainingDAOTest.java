package example.com.springcoretask1.dao.impl;

import example.com.springcoretask1.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTrainingDAOTest {

    private InMemoryTrainingDAO dao;
    private Map<Long, Training> storage;

    @BeforeEach
    void setup() {
        dao = new InMemoryTrainingDAO();
        storage = new HashMap<>();
        dao.setStorage(storage);
    }

    @Test
    void save_shouldGenerateIdWhenTrainingIsNew() {
        Training training = new Training();

        Training saved = dao.save(training);

        assertNotNull(saved.getTrainingId());
        assertEquals(1L, saved.getTrainingId());
        assertEquals(1, storage.size());
    }

    @Test
    void save_shouldKeepExistingIdWhenTrainingHasId() {
        Training training = new Training();
        training.setTrainingId(100L);

        Training saved = dao.save(training);

        assertEquals(100L, saved.getTrainingId());
        assertEquals(1, storage.size());
    }

    @Test
    void findById_shouldReturnTrainingWhenExists() {
        Training training = new Training();
        training.setTrainingId(1L);
        storage.put(1L, training);

        Optional<Training> result = dao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getTrainingId());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        Optional<Training> result = dao.findById(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllTrainings() {
        storage.put(1L, new Training());
        storage.put(2L, new Training());

        List<Training> trainings = dao.findAll();

        assertEquals(2, trainings.size());
    }

    @Test
    void findAll_shouldReturnEmptyListWhenStorageEmpty() {
        List<Training> trainings = dao.findAll();
        assertTrue(trainings.isEmpty());
    }

    @Test
    void delete_shouldRemoveTrainingWhenExists() {
        storage.put(1L, new Training());

        dao.delete(1L);

        assertFalse(storage.containsKey(1L));
    }

    @Test
    void delete_shouldDoNothingWhenIdNotExists() {
        dao.delete(999L);
        assertTrue(storage.isEmpty());
    }

    @Test
    void setStorage_shouldInitializeIdSequenceFromMaxExistingId() {
        Map<Long, Training> prefilled = new HashMap<>();
        prefilled.put(5L, new Training());
        prefilled.put(10L, new Training());

        InMemoryTrainingDAO newDao = new InMemoryTrainingDAO();
        newDao.setStorage(prefilled);

        Training training = new Training();
        Training saved = newDao.save(training);

        assertEquals(11L, saved.getTrainingId());
    }
}