package epam.com.gym.crm.facade.impl;

import epam.com.gym.crm.filter.TraineeTrainingFilter;
import epam.com.gym.crm.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.*;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymFacadeImplTest {

    @Mock
    private TrainerService trainerService;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainingService trainingService;
    @Mock
    private UserService userService;

    @InjectMocks
    private GymFacadeImpl facade;

    private Trainer mockTrainer;
    private Trainee mockTrainee;
    private Training mockTraining;
    private Credentials credentials;

    @BeforeEach
    void setUp() {
        mockTrainer = new Trainer();
        mockTrainer.setId(1L);

        mockTrainee = new Trainee();
        mockTrainee.setId(2L);

        mockTraining = new Training();
        mockTraining.setId(3L);

        credentials = new Credentials("auth.user", "authPass123");
    }

    /* ================= TRAINER TESTS ================= */

    @Test
    void createTrainer_shouldDelegateToService() {
        TrainerDTO dto = new TrainerDTO();
        when(trainerService.create(dto)).thenReturn(mockTrainer);

        Trainer result = facade.createTrainer(dto);

        assertEquals(mockTrainer, result);
        verify(trainerService, times(1)).create(dto);
    }

    @Test
    void updateTrainer_shouldDelegateToService() {
        TrainerDTO dto = new TrainerDTO();
        when(trainerService.update(1L, dto)).thenReturn(mockTrainer);

        Trainer result = facade.updateTrainer(credentials, 1L, dto);

        assertEquals(mockTrainer, result);
        verify(trainerService, times(1)).update(1L, dto);
    }

    @Test
    void activateTrainer_shouldDelegateToUserService() {
        facade.activateTrainer(credentials);
        verify(userService, times(1)).activateUser(credentials.username());
    }

    @Test
    void deactivateTrainer_shouldDelegateToUserService() {
        facade.deactivateTrainer(credentials);
        verify(userService, times(1)).deactivateUser(credentials.username());
    }

    @Test
    void changeTrainerPassword_shouldDelegateToUserService() {
        facade.changeTrainerPassword(credentials, "newPass123");
        verify(userService, times(1)).changePassword("auth.user", "authPass123", "newPass123");
    }

    @Test
    void getTrainerById_shouldDelegateToService() {
        when(trainerService.findById(1L)).thenReturn(mockTrainer);
        assertEquals(mockTrainer, facade.getTrainerById(credentials, 1L));
    }

    @Test
    void getTrainerByUserName_shouldDelegateToService() {
        when(trainerService.findByUsername(credentials.username())).thenReturn(mockTrainer);
        assertEquals(mockTrainer, facade.getTrainerByUserName(credentials));
    }

    @Test
    void getAllTrainers_shouldDelegateToService() {
        List<Trainer> list = List.of(mockTrainer);
        when(trainerService.findAll()).thenReturn(list);
        assertEquals(list, facade.getAllTrainers(credentials));
    }

    /* ================= TRAINEE TESTS ================= */

    @Test
    void createTrainee_shouldDelegateToService() {
        TraineeDTO dto = new TraineeDTO();
        when(traineeService.create(dto)).thenReturn(mockTrainee);

        Trainee result = facade.createTrainee(dto);

        assertEquals(mockTrainee, result);
        verify(traineeService, times(1)).create(dto);
    }

    @Test
    void updateTrainee_shouldDelegateToService() {
        TraineeDTO dto = new TraineeDTO();
        when(traineeService.update(2L, dto)).thenReturn(mockTrainee);

        Trainee result = facade.updateTrainee(credentials, 2L, dto);

        assertEquals(mockTrainee, result);
        verify(traineeService, times(1)).update(2L, dto);
    }

    @Test
    void updateTraineeTrainings_shouldDelegateToService() {
        Map<Long, Long> mappings = Map.of(10L, 20L);
        facade.updateTraineeTrainings(credentials, 2L, mappings);
        verify(trainingService, times(1)).updateTraineeTrainings(2L, mappings);
    }

    @Test
    void activateTrainee_shouldDelegateToUserService() {
        facade.activateTrainee(credentials);
        verify(userService, times(1)).activateUser(credentials.username());
    }

    @Test
    void deactivateTrainee_shouldDelegateToUserService() {
        facade.deactivateTrainee(credentials);
        verify(userService, times(1)).deactivateUser(credentials.username());
    }

    @Test
    void changeTraineePassword_shouldDelegateToUserService() {
        facade.changeTraineePassword(credentials, "newPass123");
        verify(userService, times(1)).changePassword("auth.user", "authPass123", "newPass123");
    }

    @Test
    void getTraineeById_shouldDelegateToService() {
        when(traineeService.findById(2L)).thenReturn(mockTrainee);
        assertEquals(mockTrainee, facade.getTraineeById(credentials, 2L));
    }

    @Test
    void getTraineeByUsername_shouldDelegateToService() {
        when(traineeService.findByUsername(credentials.username())).thenReturn(mockTrainee);
        assertEquals(mockTrainee, facade.getTraineeByUsername(credentials));
    }

    @Test
    void getUnassignedTrainersOfTrainee_shouldDelegateToService() {
        List<Trainer> list = List.of(mockTrainer);
        when(trainerService.getUnassignedTrainers(credentials.username())).thenReturn(list);

        assertEquals(list, facade.getUnassignedTrainersOfTrainee(credentials));
    }

    @Test
    void getAllTrainees_shouldDelegateToService() {
        List<Trainee> list = List.of(mockTrainee);
        when(traineeService.findAll()).thenReturn(list);
        assertEquals(list, facade.getAllTrainees(credentials));
    }

    @Test
    void deleteTrainee_shouldDelegateToService() {
        facade.deleteTrainee(credentials);
        verify(traineeService, times(1)).deleteByUsername(credentials.username());
    }

    /* ================= TRAINING TESTS ================= */

    @Test
    void createTraining_shouldDelegateToService() {
        TrainingDTO dto = new TrainingDTO();
        when(trainingService.create(dto)).thenReturn(mockTraining);

        Training result = facade.createTraining(credentials, dto);

        assertEquals(mockTraining, result);
        verify(trainingService, times(1)).create(dto);
    }

    @Test
    void getTraineeTrainingsByCriteria_shouldDelegateToService() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        List<Training> list = List.of(mockTraining);
        when(trainingService.getTraineeTrainingsByCriteria(filter)).thenReturn(list);

        assertEquals(list, facade.getTraineeTrainingsByCriteria(credentials, filter));
    }

    @Test
    void getTrainerTrainingsByCriteria_shouldDelegateToService() {
        TrainerTrainingFilter filter = new TrainerTrainingFilter();
        List<Training> list = List.of(mockTraining);
        when(trainingService.getTrainerTrainingsByCriteria(filter)).thenReturn(list);

        assertEquals(list, facade.getTrainerTrainingsByCriteria(credentials, filter));
    }

    @Test
    void getTrainingById_shouldDelegateToService() {
        when(trainingService.findById(3L)).thenReturn(mockTraining);
        assertEquals(mockTraining, facade.getTrainingById(credentials, 3L));
    }

    @Test
    void getAllTrainings_shouldDelegateToService() {
        List<Training> list = List.of(mockTraining);
        when(trainingService.findAll()).thenReturn(list);
        assertEquals(list, facade.getAllTrainings(credentials));
    }
}
