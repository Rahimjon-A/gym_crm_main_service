package epam.com.gym.crm.facade.impl;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.dto.trainee.TraineeDTO;
import epam.com.gym.crm.dto.trainer.TrainerDTO;
import epam.com.gym.crm.dto.training.TrainingDTO;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.model.common.Credentials;
import epam.com.gym.crm.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymFacadeImplTest {
    private static final String AUTH_USER = "auth.user";
    private static final String AUTH_PASS = "authPass123";
    private static final String NEW_PASS = "newPass123";

    @Mock
    private TrainerService trainerService;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainingService trainingService;
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private UserService userService;
    @Mock
    private AuthService authService;

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

        credentials = new Credentials(AUTH_USER, AUTH_PASS);
    }

    /* ================= AUTH TESTS ================= */

    @Test
    void login_shouldDelegateToAuthService() {
        facade.login(credentials);
        verify(authService, times(1)).authenticate(credentials);
    }

    @Test
    void changePassword_shouldDelegateToUserService() {
        PasswordChangeRequest request = new PasswordChangeRequest(AUTH_USER, AUTH_PASS, NEW_PASS);
        facade.changePassword(request);
        verify(userService, times(1)).changePassword(AUTH_USER, AUTH_PASS, NEW_PASS);
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
        when(trainerService.update(AUTH_USER, dto)).thenReturn(mockTrainer);

        Trainer result = facade.updateTrainer(AUTH_USER, dto);

        assertEquals(mockTrainer, result);
        verify(trainerService, times(1)).update(AUTH_USER, dto);
    }

    @Test
    void activateTrainer_shouldDelegateToUserService() {
        facade.activateTrainer(AUTH_USER);
        verify(userService, times(1)).activateUser(AUTH_USER);
    }

    @Test
    void deactivateTrainer_shouldDelegateToUserService() {
        facade.deactivateTrainer(AUTH_USER);
        verify(userService, times(1)).deactivateUser(AUTH_USER);
    }

    @Test
    void changeTrainerPassword_shouldDelegateToUserService() {
        facade.changeTrainerPassword(credentials, NEW_PASS);
        verify(userService, times(1)).changePassword(AUTH_USER, AUTH_PASS, NEW_PASS);
    }

    @Test
    void getTrainerById_shouldDelegateToService() {
        when(trainerService.findById(1L)).thenReturn(mockTrainer);
        assertEquals(mockTrainer, facade.getTrainerById(1L));
    }

    @Test
    void getTrainerByUserName_shouldDelegateToService() {
        when(trainerService.findByUsername(AUTH_USER)).thenReturn(mockTrainer);
        assertEquals(mockTrainer, facade.getTrainerByUserName(AUTH_USER));
    }

    @Test
    void getUnassignedTrainersOfTrainee_shouldDelegateToService() {
        List<Trainer> list = List.of(mockTrainer);
        when(trainerService.getUnassignedTrainers(AUTH_USER)).thenReturn(list);

        assertEquals(list, facade.getUnassignedTrainersOfTrainee(AUTH_USER));
    }

    @Test
    void getAllTrainers_shouldDelegateToService() {
        List<Trainer> list = List.of(mockTrainer);
        when(trainerService.findAll()).thenReturn(list);
        assertEquals(list, facade.getAllTrainers());
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
        when(traineeService.update(AUTH_USER, dto)).thenReturn(mockTrainee);

        Trainee result = facade.updateTrainee(AUTH_USER, dto);

        assertEquals(mockTrainee, result);
        verify(traineeService, times(1)).update(AUTH_USER, dto);
    }

    @Test
    void activateTrainee_shouldDelegateToUserService() {
        facade.activateTrainee(AUTH_USER);
        verify(userService, times(1)).activateUser(AUTH_USER);
    }

    @Test
    void deactivateTrainee_shouldDelegateToUserService() {
        facade.deactivateTrainee(AUTH_USER);
        verify(userService, times(1)).deactivateUser(AUTH_USER);
    }

    @Test
    void changeTraineePassword_shouldDelegateToUserService() {
        facade.changeTraineePassword(credentials, NEW_PASS);
        verify(userService, times(1)).changePassword(AUTH_USER, AUTH_PASS, NEW_PASS);
    }

    @Test
    void getTraineeById_shouldDelegateToService() {
        when(traineeService.findById(2L)).thenReturn(mockTrainee);
        assertEquals(mockTrainee, facade.getTraineeById(2L));
    }

    @Test
    void getTraineeByUsername_shouldDelegateToService() {
        when(traineeService.findByUsername(AUTH_USER)).thenReturn(mockTrainee);
        assertEquals(mockTrainee, facade.getTraineeByUsername(AUTH_USER));
    }

    @Test
    void getAllTrainees_shouldDelegateToService() {
        List<Trainee> list = List.of(mockTrainee);
        when(traineeService.findAll()).thenReturn(list);
        assertEquals(list, facade.getAllTrainees());
    }

    @Test
    void deleteTrainee_shouldDelegateToService() {
        facade.deleteTrainee(AUTH_USER);
        verify(traineeService, times(1)).deleteByUsername(AUTH_USER);
    }

    /* ================= TRAINING TESTS ================= */

    @Test
    void createTraining_shouldDelegateToService() {
        TrainingDTO dto = new TrainingDTO();
        when(trainingService.create(dto)).thenReturn(mockTraining);

        Training result = facade.createTraining(dto);

        assertEquals(mockTraining, result);
        verify(trainingService, times(1)).create(dto);
    }

    @Test
    void getTraineeTrainingsByCriteria_shouldDelegateToService() {
        TraineeTrainingFilter filter = new TraineeTrainingFilter();
        List<Training> list = List.of(mockTraining);
        when(trainingService.getTraineeTrainingsByCriteria(filter)).thenReturn(list);

        assertEquals(list, facade.getTraineeTrainingsByCriteria(filter));
    }

    @Test
    void getTrainerTrainingsByCriteria_shouldDelegateToService() {
        TrainerTrainingFilter filter = new TrainerTrainingFilter();
        List<Training> list = List.of(mockTraining);
        when(trainingService.getTrainerTrainingsByCriteria(filter)).thenReturn(list);

        assertEquals(list, facade.getTrainerTrainingsByCriteria(filter));
    }

    @Test
    void getTrainingById_shouldDelegateToService() {
        when(trainingService.findById(3L)).thenReturn(mockTraining);
        assertEquals(mockTraining, facade.getTrainingById(3L));
    }

    @Test
    void getAllTrainings_shouldDelegateToService() {
        List<Training> list = List.of(mockTraining);
        when(trainingService.findAll()).thenReturn(list);
        assertEquals(list, facade.getAllTrainings());
    }

    /* ================= TRAINING TYPES TESTS ================= */

    @Test
    void getAllTrainingTypes_shouldDelegateToService() {
        TrainingType mockType = new TrainingType();
        List<TrainingType> list = List.of(mockType);

        when(trainingTypeService.findAll()).thenReturn(list);

        assertEquals(list, facade.getAllTrainingTypes());
        verify(trainingTypeService, times(1)).findAll();
    }
}
