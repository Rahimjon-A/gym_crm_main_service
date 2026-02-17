package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.storage.impl.TrainingStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InMemoryTrainingDAOTest {

    private TrainingDaoImpl dao;
    private TrainingStorage storage;

    @BeforeEach
    void setUp() {
        dao = new TrainingDaoImpl();
        storage = mock(TrainingStorage.class);
        dao.setStorage(storage);
    }

    @Test
    void save_shouldDelegateToStorage() {
        Training training = new Training();
        when(storage.save(training)).thenReturn(training);

        Training result = dao.save(training);

        assertEquals(training, result);
        verify(storage).save(training);
    }

    @Test
    void findById_shouldDelegateToStorage() {
        Training training = new Training();
        when(storage.findById(1L)).thenReturn(Optional.of(training));

        Optional<Training> result = dao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(training, result.get());
        verify(storage).findById(1L);
    }

    @Test
    void findAll_shouldDelegateToStorage() {
        List<Training> trainings = List.of(new Training(), new Training());
        when(storage.findAll()).thenReturn(trainings);

        List<Training> result = dao.findAll();

        assertEquals(2, result.size());
        verify(storage).findAll();
    }

    @Test
    void delete_shouldDelegateToStorage() {
        dao.delete(1L);

        verify(storage).delete(1L);
    }
}
