package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.exception.ValidationException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private UserDAO<Trainee> traineeDao;
    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee validInputTrainee;
    private Trainee validTrainee;
    private Date pastDate;

    @BeforeEach
    void setUp() {
        pastDate = new Date(System.currentTimeMillis() - 10000000L);

        validInputTrainee = new Trainee();
        validInputTrainee.setFirstName("John");
        validInputTrainee.setLastName("Doe");
        validInputTrainee.setDateOfBirth(pastDate);
        validInputTrainee.setAddress("123 Main St");
        validInputTrainee.setActive(true);

        validTrainee = new Trainee();
        validTrainee.setUsername("john.doe");
        validTrainee.setPassword("abc1234567");
        validTrainee.setActive(true);
        validTrainee.setFirstName("John");
        validTrainee.setLastName("Doe");
        validTrainee.setId(1L);
        validTrainee.setDateOfBirth(pastDate);
        validTrainee.setAddress("123 Main St");
    }

    @Test
    void create_shouldSaveTrainee_whenInputIsValid() {
        when(userService.generateUsername("John", "Doe")).thenReturn("john.doe");
        when(userService.generatePassword()).thenReturn("abc1234567");
        when(traineeDao.create(any(Trainee.class))).thenAnswer(i -> i.getArgument(0));

        Trainee result = traineeService.create(validInputTrainee);

        assertNotNull(result);
        assertEquals("john.doe", result.getUsername());
        assertEquals("abc1234567", result.getPassword());
        assertTrue(result.isActive());
        verify(traineeDao, times(1)).create(any(Trainee.class));
    }

    @Test
    void create_shouldThrowException_whenInputIsNull() {
        assertThrows(ValidationException.class, () -> traineeService.create(null));
        verifyNoInteractions(traineeDao);
    }

    @Test
    void create_shouldThrowException_whenFirstNameIsBlank() {
        validInputTrainee.setFirstName("   ");
        assertThrows(ValidationException.class, () -> traineeService.create(validInputTrainee));
    }

    @Test
    void create_shouldThrowException_whenDateOfBirthIsInFuture() {
        validInputTrainee.setDateOfBirth(new Date(System.currentTimeMillis() + 10000000L));
        assertThrows(ValidationException.class, () -> traineeService.create(validInputTrainee));
    }

    @Test
    void update_shouldUpdateFields_whenValid() {
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(validTrainee));
        when(traineeDao.update(any(Trainee.class))).thenAnswer(i -> i.getArgument(0));

        Trainee updateInput = new Trainee();
        updateInput.setFirstName("Jane");
        updateInput.setLastName("Smith");
        updateInput.setAddress("456 New Ave");
        updateInput.setActive(true);

        Trainee result = traineeService.update("john.doe", updateInput);

        assertEquals("Jane", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("456 New Ave", result.getAddress());
        verify(traineeDao, times(1)).update(validTrainee);
    }

    @Test
    void update_shouldThrowException_whenTraineeNotFound() {
        when(traineeDao.findByUsername("unknown.user")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> traineeService.update("unknown.user", validInputTrainee));
    }

    @Test
    void findByUsername_shouldReturnTrainee() {
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(validTrainee));
        assertEquals(validTrainee, traineeService.findByUsername("john.doe"));
    }

    @Test
    void findById_shouldReturnTrainee() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(validTrainee));
        assertEquals(validTrainee, traineeService.findById(1L));
    }

    @Test
    void deleteByUsername_shouldFindAndHardDelete() {
        when(traineeDao.findByUsername("john.doe")).thenReturn(Optional.of(validTrainee));

        traineeService.deleteByUsername("john.doe");

        verify(traineeDao, times(1)).findByUsername("john.doe");
        verify(traineeDao, times(1)).delete(1L);
    }

    @Test
    void findAll_shouldDelegateToDao() {
        List<Trainee> mockList = List.of(validTrainee);
        when(traineeDao.findAll()).thenReturn(mockList);

        assertEquals(mockList, traineeService.findAll());
        verify(traineeDao, times(1)).findAll();
    }
}
