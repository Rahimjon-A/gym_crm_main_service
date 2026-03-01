package epam.com.gym.crm.facade.impl;

import epam.com.gym.crm.dto.*;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.service.AuthService;
import epam.com.gym.crm.service.TraineeService;
import epam.com.gym.crm.service.TrainerService;
import epam.com.gym.crm.service.TrainingService;
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
    private AuthService authService;

    @InjectMocks
    private GymFacadeImpl facade;

    private static final String AUTH_USER = "auth.user";
    private static final String AUTH_PASS = "authPass123";

    private Trainer mockTrainer;
    private Trainee mockTrainee;
    private Training mockTraining;

    @BeforeEach
    void setUp() {
        mockTrainer = new Trainer();
        mockTrainer.setId(1L);

        mockTrainee = new Trainee();
        mockTrainee.setId(2L);

        mockTraining = new Training();
        mockTraining.setId(3L);
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

        Trainer result = facade.updateTrainer(AUTH_USER, AUTH_PASS, 1L, dto);

        assertEquals(mockTrainer, result);
        verify(trainerService, times(1)).update(1L, dto);
    }

    @Test
    void activateTrainer_shouldDelegateToAuthService() {
        facade.activateTrainer(AUTH_USER, AUTH_PASS);
        verify(authService, times(1)).activateUser(AUTH_USER);
    }

    @Test
    void deactivateTrainer_shouldDelegateToAuthService() {
        facade.deactivateTrainer(AUTH_USER, AUTH_PASS);
        verify(authService, times(1)).deactivateUser(AUTH_USER);
    }

    @Test
    void changeTrainerPassword_shouldDelegateToAuthService() {
        facade.changeTrainerPassword(AUTH_USER, AUTH_PASS, "newPass123");
        verify(authService, times(1)).changePassword(AUTH_USER, AUTH_PASS, "newPass123");
    }

    @Test
    void getTrainerById_shouldDelegateToService() {
        when(trainerService.findById(1L)).thenReturn(mockTrainer);
        assertEquals(mockTrainer, facade.getTrainerById(AUTH_USER, AUTH_PASS, 1L));
    }

    @Test
    void getTrainerByUserName_shouldDelegateToService() {
        when(trainerService.findByUsername("target.user")).thenReturn(mockTrainer);
        assertEquals(mockTrainer, facade.getTrainerByUserName("target.user", AUTH_PASS));
    }

    @Test
    void getAllTrainers_shouldDelegateToService() {
        List<Trainer> list = List.of(mockTrainer);
        when(trainerService.findAll()).thenReturn(list);
        assertEquals(list, facade.getAllTrainers(AUTH_USER, AUTH_PASS));
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

        Trainee result = facade.updateTrainee(AUTH_USER, AUTH_PASS, 2L, dto);

        assertEquals(mockTrainee, result);
        verify(traineeService, times(1)).update(2L, dto);
    }

    @Test
    void updateTraineeTrainings_shouldDelegateToService() {
        Map<Long, Long> mappings = Map.of(10L, 20L);
        facade.updateTraineeTrainings(AUTH_USER, AUTH_PASS, 2L, mappings);
        verify(traineeService, times(1)).updateTraineeTrainings(2L, mappings);
    }

    @Test
    void activateTrainee_shouldDelegateToAuthService() {
        facade.activateTrainee(AUTH_USER, AUTH_PASS);
        verify(authService, times(1)).activateUser(AUTH_USER);
    }

    @Test
    void deactivateTrainee_shouldDelegateToAuthService() {
        facade.deactivateTrainee(AUTH_USER, AUTH_PASS);
        verify(authService, times(1)).deactivateUser(AUTH_USER);
    }

    @Test
    void changeTraineePassword_shouldDelegateToAuthService() {
        facade.changeTraineePassword(AUTH_USER, AUTH_PASS, "newPass123");
        verify(authService, times(1)).changePassword(AUTH_USER, AUTH_PASS, "newPass123");
    }

    @Test
    void getTraineeById_shouldDelegateToService() {
        when(traineeService.findById(2L)).thenReturn(mockTrainee);
        assertEquals(mockTrainee, facade.getTraineeById(AUTH_USER, AUTH_PASS, 2L));
    }

    @Test
    void getTraineeByUsername_shouldDelegateToService() {
        when(traineeService.findByUsername("target.trainee")).thenReturn(mockTrainee);
        assertEquals(mockTrainee, facade.getTraineeByUsername("target.trainee", AUTH_PASS));
    }

    @Test
    void getUnassignedTrainersOfTrainee_shouldDelegateToService() {
        List<Trainer> list = List.of(mockTrainer);
        when(traineeService.getUnassignedTrainers("target.trainee")).thenReturn(list);

        assertEquals(list, facade.getUnassignedTrainersOfTrainee("target.trainee", AUTH_PASS));
    }

    @Test
    void getAllTrainees_shouldDelegateToService() {
        List<Trainee> list = List.of(mockTrainee);
        when(traineeService.findAll()).thenReturn(list);
        assertEquals(list, facade.getAllTrainees(AUTH_USER, AUTH_PASS));
    }

    @Test
    void deleteTrainee_shouldDelegateToService() {
        facade.deleteTrainee("target.trainee", AUTH_PASS);
        verify(traineeService, times(1)).deleteByUsername("target.trainee");
    }

    /* ================= TRAINING TESTS ================= */

    @Test
    void createTraining_shouldDelegateToService() {
        TrainingDTO dto = new TrainingDTO();
        when(trainingService.create(dto)).thenReturn(mockTraining);

        Training result = facade.createTraining(AUTH_USER, AUTH_PASS, dto);

        assertEquals(mockTraining, result);
        verify(trainingService, times(1)).create(dto);
    }

    @Test
    void getTraineeTrainingsByCriteria_shouldDelegateToService() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        List<Training> list = List.of(mockTraining);
        when(trainingService.getTraineeTrainingsByCriteria(filter)).thenReturn(list);

        assertEquals(list, facade.getTraineeTrainingsByCriteria(AUTH_USER, AUTH_PASS, filter));
    }

    @Test
    void getTrainerTrainingsByCriteria_shouldDelegateToService() {
        TrainerTrainingFilter filter = new TrainerTrainingFilter();
        List<Training> list = List.of(mockTraining);
        when(trainingService.getTrainerTrainingsByCriteria(filter)).thenReturn(list);

        assertEquals(list, facade.getTrainerTrainingsByCriteria(AUTH_USER, AUTH_PASS, filter));
    }

    @Test
    void getTrainingById_shouldDelegateToService() {
        when(trainingService.findById(3L)).thenReturn(mockTraining);
        assertEquals(mockTraining, facade.getTrainingById(AUTH_USER, AUTH_PASS, 3L));
    }

    @Test
    void getAllTrainings_shouldDelegateToService() {
        List<Training> list = List.of(mockTraining);
        when(trainingService.findAll()).thenReturn(list);
        assertEquals(list, facade.getAllTrainings(AUTH_USER, AUTH_PASS));
    }
}
