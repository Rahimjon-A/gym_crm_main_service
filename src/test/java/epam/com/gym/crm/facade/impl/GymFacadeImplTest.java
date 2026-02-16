package epam.com.gym.crm.facade.impl;

import epam.com.gym.crm.dto.TrainerDTO;
import epam.com.gym.crm.dto.TraineeDTO;
import epam.com.gym.crm.dto.TrainingDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.model.Trainer;
import epam.com.gym.crm.model.Training;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.service.TrainerService;
import epam.com.gym.crm.service.TraineeService;
import epam.com.gym.crm.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymFacadeImplTest {

    @Mock
    private TrainerService trainerService;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private GymFacadeImpl facade;

    @Test
    void createTrainer_shouldDelegateToService_andReturnResult() {
        TrainerDTO dto = new TrainerDTO("John", "Smith", TrainingType.CARDIO);
        Trainer trainer = new Trainer();
        trainer.setId(1L);

        when(trainerService.create(dto)).thenReturn(trainer);

        Trainer result = facade.createTrainer(dto);

        assertThat(result).isEqualTo(trainer);
        verify(trainerService, times(1)).create(dto);
    }

    @Test
    void updateTrainer_shouldDelegateCorrectly() {
        TrainerDTO dto = new TrainerDTO("New", "Name", TrainingType.YOGA);
        Trainer updated = new Trainer();
        updated.setId(5L);

        when(trainerService.update(5L, dto)).thenReturn(updated);

        Trainer result = facade.updateTrainer(5L, dto);

        assertThat(result.getId()).isEqualTo(5L);
        verify(trainerService).update(5L, dto);
    }

    @Test
    void getTrainerById_shouldReturnOptionalFromService() {
        Trainer trainer = new Trainer();
        trainer.setId(10L);

        when(trainerService.findById(10L)).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = facade.getTrainerById(10L);

        assertThat(result).isPresent();
        verify(trainerService).findById(10L);
    }

    @Test
    void getAllTrainers_shouldReturnListFromService() {
        when(trainerService.findAll()).thenReturn(Collections.emptyList());

        List<Trainer> allTrainers = facade.getAllTrainers();

        assertThat(allTrainers).isEmpty();
        verify(trainerService).findAll();
    }





    @Test
    void createTrainee_shouldDelegateToService() {
        TraineeDTO dto = new TraineeDTO();
        dto.setFirstName("Alice");
        dto.setLastName( "Brown");
        dto.setDateOfBirth(LocalDate.of(2000,1,1));
        dto.setAddress("NY");

        Trainee trainee = new Trainee();
        trainee.setId(3L);

        when(traineeService.create(dto)).thenReturn(trainee);

        Trainee result = facade.createTrainee(dto);

        assertThat(result.getId()).isEqualTo(3L);
        verify(traineeService).create(dto);
    }

    @Test
    void updateTrainee_shouldDelegateCorrectly() {
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName( "Doe");
        traineeDTO.setDateOfBirth( LocalDate.of(2010, 5, 25));
        traineeDTO.setAddress("Tashkent");
        Trainee trainee = new Trainee();
        trainee.setId(5L);

        when(traineeService.update(5L, traineeDTO)).thenReturn(trainee);

        Trainee result = facade.updateTrainee(5L, traineeDTO);

        assertThat(result.getId()).isEqualTo(5L);
        verify(traineeService).update(5L, traineeDTO);
    }

    @Test
    void updateTrainee_shouldHandleNotFound() {
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setFirstName("John");
        traineeDTO.setLastName( "Doe");
        traineeDTO.setDateOfBirth( LocalDate.of(2010, 5, 25));
        traineeDTO.setAddress("Tashkent");
        Long nonExistentId = 999L;

        when(traineeService.update(nonExistentId, traineeDTO))
                .thenThrow(new EntityNotFoundException("Trainee not found with id: " + nonExistentId));

        assertThatThrownBy(() -> facade.updateTrainee(nonExistentId, traineeDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Trainee not found with id: 999");
    }

    @Test
    void getTraineeById_shouldReturnOptionalFromService() {
        Trainee trainee = new Trainee();
        trainee.setId(10L);

        when(traineeService.findById(10L)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = facade.getTraineeById(10L);

        assertThat(result).isPresent();
        verify(traineeService).findById(10L);
    }

    @Test
    void deleteTrainee_shouldCallServiceDelete() {
        doNothing().when(traineeService).delete(7L);

        facade.deleteTrainee(7L);

        verify(traineeService, times(1)).delete(7L);
    }

    @Test
    void gelAllTrainees_shouldReturnListFromService() {
        when(traineeService.findAll()).thenReturn(Collections.emptyList());

        List<Trainee> allTrainees = facade.getAllTrainees();

        assertThat(allTrainees).isEmpty();
        verify(traineeService).findAll();
    }



    @Test
    void createTraining_shouldDelegateAndReturnResult() {
        TrainingDTO dto = new TrainingDTO(
                1L,
                2L,
                "Morning",
                TrainingType.GYM,
                LocalDate.now(),
                60.0
        );

        Training training = new Training();
        training.setId(100L);

        when(trainingService.create(dto)).thenReturn(training);

        Training result = facade.createTraining(dto);

        assertThat(result.getId()).isEqualTo(100L);
        verify(trainingService).create(dto);
    }

    @Test
    void getTrainingById_shouldReturnOptionalFromService() {
        Training training = new Training();
        training.setId(10L);

        when(trainingService.findById(10L)).thenReturn(Optional.of(training));

        Optional<Training> optionalTraining = facade.getTrainingById(10L);

        assertThat(optionalTraining).isPresent();
        verify(trainingService).findById(10L);
    }

    @Test
    void getAllTrainings_shouldReturnListFromService() {
        when(trainingService.findAll()).thenReturn(Collections.emptyList());

        List<Training> allTrainings = facade.getAllTrainings();

        assertThat(allTrainings).isEmpty();
        verify(trainingService).findAll();
    }

}