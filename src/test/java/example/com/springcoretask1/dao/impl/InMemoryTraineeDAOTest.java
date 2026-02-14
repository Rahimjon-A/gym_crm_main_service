package example.com.springcoretask1.dao.impl;

import example.com.springcoretask1.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class InMemoryTraineeDAOTest {

    private InMemoryTraineeDAO dao;
    private Map<Long, Trainee> storage;

    @BeforeEach
    void setup() {
        dao = new InMemoryTraineeDAO();
        storage = new HashMap<>();
        dao.setStorage(storage);
    }

    @Test
    void save_shouldGenerateIdWhenNewTrainee() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");

        Trainee saved = dao.save(trainee);

        assertNotNull(saved.getUserId());
        assertEquals(1L, saved.getUserId());
        assertEquals(1, storage.size());
    }

    @Test
    void save_shouldNotChangeIdWhenExistingTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUserId(100L);
        trainee.setFirstName("Mike");

        Trainee saved = dao.save(trainee);

        assertEquals(100L, saved.getUserId());
        assertEquals(1, storage.size());
    }

    @Test
    void findById_shouldReturnTraineeWhenExists() {
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        storage.put(1L, trainee);

        Optional<Trainee> result = dao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getUserId());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        Optional<Trainee> result = dao.findById(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllTrainees() {
        storage.put(1L, new Trainee());
        storage.put(2L, new Trainee());

        List<Trainee> all = dao.findAll();

        assertEquals(2, all.size());
    }

    @Test
    void findAll_shouldReturnEmptyListWhenStorageEmpty() {
        List<Trainee> all = dao.findAll();
        assertTrue(all.isEmpty());
    }

    @Test
    void delete_shouldRemoveExistingTrainee() {
        storage.put(1L, new Trainee());

        dao.delete(1L);

        assertFalse(storage.containsKey(1L));
    }

    @Test
    void delete_shouldDoNothingWhenIdNotExists() {
        dao.delete(999L);
        assertTrue(storage.isEmpty());
    }

    @Test
    void setStorage_shouldInitializeIdSequenceFromExistingMaxId() {
        Map<Long, Trainee> prefilled = new HashMap<>();
        prefilled.put(5L, new Trainee());
        prefilled.put(10L, new Trainee());

        InMemoryTraineeDAO newDao = new InMemoryTraineeDAO();
        newDao.setStorage(prefilled);

        Trainee trainee = new Trainee();
        Trainee saved = newDao.save(trainee);

        assertEquals(11L, saved.getUserId());
    }
}