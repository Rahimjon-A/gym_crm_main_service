package example.com.springcoretask1.dao.impl;

import example.com.springcoretask1.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTrainerDAOTest {

    private InMemoryTrainerDAO dao;
    private Map<Long, Trainer> storage;

    @BeforeEach
    void setup() {
        dao = new InMemoryTrainerDAO();
        storage = new HashMap<>();
        dao.setStorage(storage);
    }

    @Test
    void save_shouldGenerateIdWhenTrainerIsNew() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");

        Trainer saved = dao.save(trainer);

        assertNotNull(saved.getUserId());
        assertEquals(1L, saved.getUserId());
        assertEquals(1, storage.size());
    }

    @Test
    void save_shouldKeepExistingIdWhenTrainerHasId() {
        Trainer trainer = new Trainer();
        trainer.setUserId(50L);

        Trainer saved = dao.save(trainer);

        assertEquals(50L, saved.getUserId());
        assertEquals(1, storage.size());
    }

    @Test
    void findById_shouldReturnTrainerWhenExists() {
        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        storage.put(1L, trainer);

        Optional<Trainer> result = dao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getUserId());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        Optional<Trainer> result = dao.findById(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllTrainers() {
        storage.put(1L, new Trainer());
        storage.put(2L, new Trainer());

        List<Trainer> trainers = dao.findAll();

        assertEquals(2, trainers.size());
    }

    @Test
    void findAll_shouldReturnEmptyListWhenStorageEmpty() {
        List<Trainer> trainers = dao.findAll();
        assertTrue(trainers.isEmpty());
    }

    @Test
    void delete_shouldRemoveTrainerWhenExists() {
        storage.put(1L, new Trainer());

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
        Map<Long, Trainer> prefilled = new HashMap<>();
        prefilled.put(5L, new Trainer());
        prefilled.put(10L, new Trainer());

        InMemoryTrainerDAO newDao = new InMemoryTrainerDAO();
        newDao.setStorage(prefilled);

        Trainer trainer = new Trainer();
        Trainer saved = newDao.save(trainer);

        assertEquals(11L, saved.getUserId());
    }
}