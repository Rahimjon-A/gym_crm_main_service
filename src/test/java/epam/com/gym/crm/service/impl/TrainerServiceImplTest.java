package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.dto.TrainerDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.service.CredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    private UserDAO<Trainer> trainerDao;

    @Mock
    private CredentialService credentialService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private TrainerDTO trainerDTO;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainerDTO = new TrainerDTO();
        trainerDTO.setFirstName("John");
        trainerDTO.setLastName("Smith");
        trainerDTO.setSpecialization(TrainingType.GYM);

        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setFirstName("John");
        trainer.setLastName("Smith");
        trainer.setSpecialization(TrainingType.GYM);
        trainer.setUsername("John.Smith");
        trainer.setPassword("1234567890");
    }

    @Test
    void create_shouldCreateTrainerSuccessfully() {
        when(credentialService.generateUsername("John", "Smith")).thenReturn("John.Smith");
        when(credentialService.generatePassword()).thenReturn("1234567890");
        when(trainerDao.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainer result = trainerService.create(trainerDTO);

        assertNotNull(result);
        assertEquals("John.Smith", result.getUsername());
        assertEquals("1234567890", result.getPassword());
        verify(credentialService).generateUsername("John", "Smith");
        verify(credentialService).generatePassword();
        verify(trainerDao).save(any(Trainer.class));
    }

    @Test
    void create_shouldFailWhenDaoThrowsException() {
        when(credentialService.generateUsername(any(), any())).thenReturn("John.Smith");
        when(credentialService.generatePassword()).thenReturn("1234567890");
        when(trainerDao.save(any())).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> trainerService.create(trainerDTO));
    }

    @Test
    void update_shouldUpdateTrainerSuccessfully() {
        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(credentialService.generateUsername("Mike", "Smith")).thenReturn("Mike.Smith");

        TrainerDTO updateDTO = new TrainerDTO();
        updateDTO.setFirstName("Mike");

        Trainer result = trainerService.update(1L, updateDTO);

        assertEquals("Mike", result.getFirstName());
        assertEquals("Mike.Smith", result.getUsername());
        verify(credentialService).generateUsername("Mike", "Smith");
        verify(trainerDao).update(trainer);
    }

    @Test
    void update_shouldUpdateSpecializationWithoutChangingUsername() {
        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TrainerDTO updateDTO = new TrainerDTO();
        updateDTO.setSpecialization(TrainingType.CARDIO);

        Trainer result = trainerService.update(1L, updateDTO);

        assertEquals(TrainingType.CARDIO, result.getSpecialization());
        assertEquals("John.Smith", result.getUsername());
        verify(credentialService, never()).generateUsername(any(), any());
        verify(trainerDao).update(trainer);
    }

    @Test
    void update_shouldThrowExceptionWhenTrainerNotFound() {
        when(trainerDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> trainerService.update(1L, trainerDTO));
    }

    @Test
    void findById_shouldReturnTrainer() {
        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        when(trainerDao.findById(1L)).thenReturn(Optional.empty());

        Optional<Trainer> result = trainerService.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnListOfTrainers() {
        when(trainerDao.findAll()).thenReturn(List.of(trainer));

        List<Trainer> result = trainerService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findAll_shouldReturnEmptyList() {
        when(trainerDao.findAll()).thenReturn(Collections.emptyList());

        List<Trainer> result = trainerService.findAll();

        assertTrue(result.isEmpty());
    }
}
