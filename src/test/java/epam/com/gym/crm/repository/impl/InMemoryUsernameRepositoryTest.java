package epam.com.gym.crm.repository.impl;

import epam.com.gym.crm.dao.TraineeDAO;
import epam.com.gym.crm.dao.TrainerDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InMemoryUsernameRepositoryTest {

    private InMemoryUsernameRepository repository;
    private TrainerDAO trainerDAO;
    private TraineeDAO traineeDAO;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUsernameRepository();

        trainerDAO = mock(TrainerDAO.class);
        traineeDAO = mock(TraineeDAO.class);

        repository.setTrainerDAO(trainerDAO);
        repository.setTraineeDAO(traineeDAO);
    }

    @Test
    void exists_shouldReturnTrue_whenTrainerHasUsername() {
        when(trainerDAO.existsByUsername("john.doe")).thenReturn(true);
        when(traineeDAO.existsByUsername("john.doe")).thenReturn(false);

        boolean result = repository.exists("john.doe");

        assertTrue(result);
    }

    @Test
    void exists_shouldReturnTrue_whenTraineeHasUsername() {
        when(trainerDAO.existsByUsername("john.doe")).thenReturn(false);
        when(traineeDAO.existsByUsername("john.doe")).thenReturn(true);

        boolean result = repository.exists("john.doe");

        assertTrue(result);
    }

    @Test
    void exists_shouldReturnFalse_whenUsernameNotFound() {
        when(trainerDAO.existsByUsername("john.doe")).thenReturn(false);
        when(traineeDAO.existsByUsername("john.doe")).thenReturn(false);

        boolean result = repository.exists("john.doe");

        assertFalse(result);
    }

    @Test
    void exists_shouldReturnFalse_whenUsernameNull() {
        boolean result = repository.exists(null);
        assertFalse(result);
    }
}
