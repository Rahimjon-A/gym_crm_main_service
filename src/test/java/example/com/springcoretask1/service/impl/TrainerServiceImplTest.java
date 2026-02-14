package example.com.springcoretask1.service.impl;

import example.com.springcoretask1.dao.TrainerDAO;
import example.com.springcoretask1.dto.TrainerDTO;
import example.com.springcoretask1.exception.EntityNotFoundException;
import example.com.springcoretask1.model.Trainer;
import example.com.springcoretask1.model.TrainingType;
import example.com.springcoretask1.util.CredentialsUtil;
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
    private TrainerDAO trainerDao;

    @Mock
    private CredentialsUtil credentialsUtil;

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
        trainer.setUserId(1L);
        trainer.setFirstName("John");
        trainer.setLastName("Smith");
        trainer.setSpecialization(TrainingType.GYM);
        trainer.setUsername("John.Smith");
        trainer.setPassword("1234567890");
    }

    @Test
    void create_shouldCreateTrainerSuccessfully() {
        when(credentialsUtil.generateUsername("John", "Smith"))
                .thenReturn("John.Smith");
        when(credentialsUtil.generatePassword())
                .thenReturn("1234567890");
        when(trainerDao.save(any(Trainer.class)))
                .thenReturn(trainer);

        Trainer result = trainerService.create(trainerDTO);

        assertNotNull(result);
        assertEquals("John.Smith", result.getUsername());
        verify(trainerDao, times(1)).save(any(Trainer.class));
    }

    @Test
    void create_shouldFailWhenDaoThrowsException() {
        when(credentialsUtil.generateUsername(any(), any()))
                .thenReturn("John.Smith");
        when(credentialsUtil.generatePassword())
                .thenReturn("1234567890");
        when(trainerDao.save(any()))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class,
                () -> trainerService.create(trainerDTO));
    }

    @Test
    void update_shouldUpdateTrainerSuccessfully() {
        when(trainerDao.findById(1L))
                .thenReturn(Optional.of(trainer));
        when(trainerDao.save(any()))
                .thenReturn(trainer);

        TrainerDTO updateDTO = new TrainerDTO();
        updateDTO.setFirstName("Mike");

        when(credentialsUtil.generateUsername("Mike", "Smith"))
                .thenReturn("Mike.Smith");

        Trainer result = trainerService.update(1L, updateDTO);

        assertEquals("Mike", result.getFirstName());
        verify(trainerDao).save(trainer);
    }

    @Test
    void update_shouldThrowExceptionWhenTrainerNotFound() {
        when(trainerDao.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> trainerService.update(1L, trainerDTO));
    }

    @Test
    void findById_shouldReturnTrainer() {
        when(trainerDao.findById(1L))
                .thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getUserId());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        when(trainerDao.findById(1L))
                .thenReturn(Optional.empty());

        Optional<Trainer> result = trainerService.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnListOfTrainers() {
        when(trainerDao.findAll())
                .thenReturn(List.of(trainer));

        List<Trainer> result = trainerService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findAll_shouldReturnEmptyList() {
        when(trainerDao.findAll())
                .thenReturn(Collections.emptyList());

        List<Trainer> result = trainerService.findAll();

        assertTrue(result.isEmpty());
    }
}