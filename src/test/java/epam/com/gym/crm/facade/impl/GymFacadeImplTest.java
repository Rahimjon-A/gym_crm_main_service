package epam.com.gym.crm.facade.impl;

import epam.com.gym.crm.dao.filter.TraineeTrainingFilter;
import epam.com.gym.crm.dao.filter.TrainerTrainingFilter;
import epam.com.gym.crm.dto.request.PasswordChangeRequest;
import epam.com.gym.crm.dto.request.trainee.TraineeCreateRequest;
import epam.com.gym.crm.dto.request.trainee.TraineeUpdateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerCreateRequest;
import epam.com.gym.crm.dto.request.trainer.TrainerUpdateRequest;
import epam.com.gym.crm.dto.request.training.TrainingCreateRequest;
import epam.com.gym.crm.mapper.TraineeMapper;
import epam.com.gym.crm.mapper.TrainerMapper;
import epam.com.gym.crm.mapper.TrainingMapper;
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

    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private TrainingMapper trainingMapper;

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

    /* ================= AUTH & USER TESTS ================= */

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

    @Test
    void activateUser_shouldDelegateToUserService() {
        facade.activateUser(AUTH_USER);
        verify(userService, times(1)).activateUser(AUTH_USER);
    }

    @Test
    void deactivateUser_shouldDelegateToUserService() {
        facade.deactivateUser(AUTH_USER);
        verify(userService, times(1)).deactivateUser(AUTH_USER);
    }

    /* ================= TRAINER TESTS ================= */

    @Test
    void createTrainer_shouldDelegateToService() {
        TrainerCreateRequest dto = new TrainerCreateRequest();

        when(trainerMapper.toEntity(dto)).thenReturn(mockTrainer);
        when(trainerService.create(mockTrainer)).thenReturn(mockTrainer);

        Trainer result = facade.createTrainer(dto);

        assertEquals(mockTrainer, result);
        verify(trainerMapper, times(1)).toEntity(dto);
        verify(trainerService, times(1)).create(mockTrainer);
    }

    @Test
    void updateTrainer_shouldDelegateToService() {
        TrainerUpdateRequest dto = new TrainerUpdateRequest();

        when(trainerMapper.toEntity(dto)).thenReturn(mockTrainer);
        when(trainerService.update(AUTH_USER, mockTrainer)).thenReturn(mockTrainer);

        Trainer result = facade.updateTrainer(AUTH_USER, dto);

        assertEquals(mockTrainer, result);
        verify(trainerMapper, times(1)).toEntity(dto);
        verify(trainerService, times(1)).update(AUTH_USER, mockTrainer);
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
        TraineeCreateRequest dto = new TraineeCreateRequest();

        // FIX: Mock the mapper translating the DTO to an entity
        when(traineeMapper.toEntity(dto)).thenReturn(mockTrainee);
        when(traineeService.create(mockTrainee)).thenReturn(mockTrainee);

        Trainee result = facade.createTrainee(dto);

        assertEquals(mockTrainee, result);
        verify(traineeMapper, times(1)).toEntity(dto);
        verify(traineeService, times(1)).create(mockTrainee);
    }

    @Test
    void updateTrainee_shouldDelegateToService() {
        TraineeUpdateRequest dto = new TraineeUpdateRequest();

        when(traineeMapper.toEntity(dto)).thenReturn(mockTrainee);
        when(traineeService.update(AUTH_USER, mockTrainee)).thenReturn(mockTrainee);

        Trainee result = facade.updateTrainee(AUTH_USER, dto);

        assertEquals(mockTrainee, result);
        verify(traineeMapper, times(1)).toEntity(dto);
        verify(traineeService, times(1)).update(AUTH_USER, mockTrainee);
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
        TrainingCreateRequest dto = new TrainingCreateRequest();

        when(trainingMapper.toEntity(dto)).thenReturn(mockTraining);
        when(trainingService.create(mockTraining)).thenReturn(mockTraining);

        Training result = facade.createTraining(dto);

        assertEquals(mockTraining, result);
        verify(trainingMapper, times(1)).toEntity(dto);
        verify(trainingService, times(1)).create(mockTraining);
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