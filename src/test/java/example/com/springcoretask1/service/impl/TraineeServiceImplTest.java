package example.com.springcoretask1.service.impl;

import example.com.springcoretask1.dao.TraineeDAO;
import example.com.springcoretask1.dto.TraineeDTO;
import example.com.springcoretask1.exception.EntityNotFoundException;
import example.com.springcoretask1.model.Trainee;
import example.com.springcoretask1.util.CredentialsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private TraineeDAO traineeDao;

    @Mock
    private CredentialsUtil credentialsUtil;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private TraineeDTO dto;
    private Trainee trainee;

    @BeforeEach
    void setup() {
        dto = new TraineeDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setAddress("NY");

        trainee = new Trainee();
        trainee.setUserId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setDateOfBirth(dto.getDateOfBirth());
        trainee.setAddress("NY");
        trainee.setUsername("John.Doe");
        trainee.setPassword("1234567890");
    }

    @Test
    void create_shouldGenerateCredentialsAndSave() {
        when(credentialsUtil.generateUsername("John", "Doe"))
                .thenReturn("John.Doe");
        when(credentialsUtil.generatePassword())
                .thenReturn("1234567890");
        when(traineeDao.save(any(Trainee.class)))
                .thenReturn(trainee);

        Trainee result = traineeService.create(dto);

        assertNotNull(result);
        assertEquals("John.Doe", result.getUsername());
        verify(credentialsUtil).generateUsername("John", "Doe");
        verify(credentialsUtil).generatePassword();
        verify(traineeDao).save(any(Trainee.class));
    }

    @Test
    void create_shouldPropagateExceptionIfDaoFails() {
        when(credentialsUtil.generateUsername(any(), any()))
                .thenReturn("John.Doe");
        when(credentialsUtil.generatePassword())
                .thenReturn("1234567890");
        when(traineeDao.save(any()))
                .thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class,
                () -> traineeService.create(dto));
    }

    @Test
    void update_shouldUpdateNonNameFieldsWithoutChangingUsername() {
        when(traineeDao.findById(1L))
                .thenReturn(Optional.of(trainee));
        when(traineeDao.save(any()))
                .thenReturn(trainee);

        TraineeDTO updateDto = new TraineeDTO();
        updateDto.setAddress("LA");

        Trainee result = traineeService.update(1L, updateDto);

        assertEquals("LA", result.getAddress());
        verify(credentialsUtil, never()).generateUsername(any(), any());
        verify(traineeDao).save(trainee);
    }

    @Test
    void update_shouldChangeUsernameWhenNameChanges() {
        when(traineeDao.findById(1L))
                .thenReturn(Optional.of(trainee));

        TraineeDTO updateDto = new TraineeDTO();
        updateDto.setFirstName("Mike");

        when(credentialsUtil.generateUsername("Mike", "Doe"))
                .thenReturn("Mike.Doe");
        when(traineeDao.save(any()))
                .thenReturn(trainee);

        Trainee result = traineeService.update(1L, updateDto);

        assertEquals("Mike", result.getFirstName());
        verify(credentialsUtil).generateUsername("Mike", "Doe");
        verify(traineeDao).save(trainee);
    }

    @Test
    void update_shouldThrowExceptionWhenTraineeNotFound() {
        when(traineeDao.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> traineeService.update(1L, dto));
    }

    @Test
    void findById_shouldReturnTrainee() {
        when(traineeDao.findById(1L))
                .thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getUserId());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        when(traineeDao.findById(1L))
                .thenReturn(Optional.empty());

        Optional<Trainee> result = traineeService.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnList() {
        when(traineeDao.findAll())
                .thenReturn(List.of(trainee));

        List<Trainee> result = traineeService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findAll_shouldReturnEmptyList() {
        when(traineeDao.findAll())
                .thenReturn(Collections.emptyList());

        List<Trainee> result = traineeService.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void delete_shouldRemoveTraineeWhenExists() {
        when(traineeDao.findById(1L))
                .thenReturn(Optional.of(trainee));

        traineeService.delete(1L);

        verify(traineeDao).delete(1L);
    }

    @Test
    void delete_shouldThrowExceptionWhenNotFound() {
        when(traineeDao.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> traineeService.delete(1L));
    }
}