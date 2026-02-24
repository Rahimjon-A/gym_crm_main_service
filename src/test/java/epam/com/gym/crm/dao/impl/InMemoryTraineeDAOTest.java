package epam.com.gym.crm.dao.impl;

import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.storage.impl.TraineeStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InMemoryTraineeDAOTest {

    private TraineeDaoImpl dao;
    private TraineeStorage storage;

    @BeforeEach
    void setUp() {
        dao = new TraineeDaoImpl();
        storage = mock(TraineeStorage.class);
        dao.setStorage(storage);
    }

    @Test
    void save_shouldDelegateToStorage() {
        Trainee trainee = new Trainee();
        when(storage.create(trainee)).thenReturn(trainee);

        Trainee result = dao.create(trainee);

        assertEquals(trainee, result);
        verify(storage, times(1)).create(trainee);
    }

    @Test
    void findById_shouldDelegateToStorage() {
        Trainee trainee = new Trainee();
        when(storage.findById(1L)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = dao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
        verify(storage).findById(1L);
    }

    @Test
    void findAll_shouldDelegateToStorage() {
        List<Trainee> trainees = List.of(new Trainee(), new Trainee());
        when(storage.findAll()).thenReturn(trainees);

        List<Trainee> result = dao.findAll();

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
        when(storage.existsByUsername("john.doe")).thenReturn(true);

        boolean result = dao.existsByUsername("john.doe");

        assertTrue(result);
        verify(storage).existsByUsername("john.doe");
    }
}
