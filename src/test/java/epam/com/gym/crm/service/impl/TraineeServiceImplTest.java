package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.UserDAO;
import epam.com.gym.crm.dto.TraineeDTO;
import epam.com.gym.crm.exception.EntityNotFoundException;
import epam.com.gym.crm.model.Trainee;
import epam.com.gym.crm.service.CredentialService;
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
    private UserDAO<Trainee> traineeDao;

    @Mock
    private CredentialService credentialService;

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
        trainee.setId(1L);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setDateOfBirth(dto.getDateOfBirth());
        trainee.setAddress("NY");
        trainee.setUsername("John.Doe");
        trainee.setPassword("1234567890");
    }

    @Test
    void create_shouldGenerateCredentialsAndSave() {
        when(credentialService.generateUsername("John", "Doe")).thenReturn("John.Doe");
        when(credentialService.generatePassword()).thenReturn("1234567890");
        when(traineeDao.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainee result = traineeService.create(dto);

        assertNotNull(result);
        assertEquals("John.Doe", result.getUsername());
        assertEquals("1234567890", result.getPassword());
        verify(credentialService).generateUsername("John", "Doe");
        verify(credentialService).generatePassword();
        verify(traineeDao).save(any(Trainee.class));
    }

    @Test
    void create_shouldPropagateExceptionIfDaoFails() {
        when(credentialService.generateUsername(any(), any())).thenReturn("John.Doe");
        when(credentialService.generatePassword()).thenReturn("1234567890");
        when(traineeDao.save(any())).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> traineeService.create(dto));
    }

    @Test
    void update_shouldUpdateNonNameFieldsWithoutChangingUsername() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));
        when(traineeDao.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TraineeDTO updateDto = new TraineeDTO();
        updateDto.setAddress("LA");

        Trainee result = traineeService.update(1L, updateDto);

        assertEquals("LA", result.getAddress());
        assertEquals("John.Doe", result.getUsername());
        verify(credentialService, never()).generateUsername(any(), any());
        verify(traineeDao).update(trainee);
    }

    @Test
    void update_shouldChangeUsernameWhenNameChanges() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));
        when(traineeDao.update(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(credentialService.generateUsername("Mike", "Doe")).thenReturn("Mike.Doe");

        TraineeDTO updateDto = new TraineeDTO();
        updateDto.setFirstName("Mike");

        Trainee result = traineeService.update(1L, updateDto);

        assertEquals("Mike", result.getFirstName());
        assertEquals("Mike.Doe", result.getUsername());
        verify(credentialService).generateUsername("Mike", "Doe");
        verify(traineeDao).update(trainee);
    }

    @Test
    void update_shouldThrowExceptionWhenTraineeNotFound() {
        when(traineeDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> traineeService.update(1L, dto));
    }

    @Test
    void findById_shouldReturnTrainee() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));

        Optional<Trainee> result = traineeService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        when(traineeDao.findById(1L)).thenReturn(Optional.empty());

        Optional<Trainee> result = traineeService.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnList() {
        when(traineeDao.findAll()).thenReturn(List.of(trainee));

        List<Trainee> result = traineeService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void findAll_shouldReturnEmptyList() {
        when(traineeDao.findAll()).thenReturn(Collections.emptyList());

        List<Trainee> result = traineeService.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void delete_shouldRemoveTraineeWhenExists() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));

        traineeService.delete(1L);

        verify(traineeDao).delete(1L);
    }

    @Test
    void delete_shouldThrowExceptionWhenNotFound() {
        when(traineeDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> traineeService.delete(1L));
    }
}
