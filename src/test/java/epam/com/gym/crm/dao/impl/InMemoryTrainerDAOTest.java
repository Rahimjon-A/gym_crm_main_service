package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.storage.impl.TrainerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InMemoryTrainerDAOTest {

    private TrainerDaoImpl dao;
    private TrainerStorage storage;

    @BeforeEach
    void setUp() {
        dao = new TrainerDaoImpl();
        storage = mock(TrainerStorage.class);
        dao.setStorage(storage);
    }

    @Test
    void save_shouldDelegateToStorage() {
        Trainer trainer = new Trainer();
        when(storage.create(trainer)).thenReturn(trainer);

        Trainer result = dao.create(trainer);

        assertEquals(trainer, result);
        verify(storage).create(trainer);
    }

    @Test
    void findById_shouldDelegateToStorage() {
        Trainer trainer = new Trainer();
        when(storage.findById(1L)).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = dao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(trainer, result.get());
        verify(storage).findById(1L);
    }

    @Test
    void findAll_shouldDelegateToStorage() {
        List<Trainer> trainers = List.of(new Trainer(), new Trainer());
        when(storage.findAll()).thenReturn(trainers);

        List<Trainer> result = dao.findAll();

        assertEquals(2, result.size());
        verify(storage).findAll();
    }

    @Test
    void delete_shouldDelegateToStorage() {
        dao.delete(1L);

        verify(storage).delete(1L);
    }

    @Test
    void existsByUsername_shouldDelegateToStorage() {
        when(storage.existsByUsername("trainer.one")).thenReturn(true);

        boolean result = dao.existsByUsername("trainer.one");

        assertTrue(result);
        verify(storage).existsByUsername("trainer.one");
    }
}
