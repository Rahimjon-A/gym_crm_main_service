package epam.com.gym.crm.service.impl;

import epam.com.gym.crm.dao.TrainingTypeDAO;
import epam.com.gym.crm.model.TrainingType;
import epam.com.gym.crm.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceImplTest {
    private static final Long TYPE_ID = 1L;
    private static final String TYPE_NAME = "YOGA";

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @InjectMocks
    private TrainingTypeServiceImpl trainingTypeService;

    private TrainingType mockTrainingType;

    @BeforeEach
    void setUp() {
        mockTrainingType = new TrainingType();
        mockTrainingType.setId(TYPE_ID);
        mockTrainingType.setTrainingTypeName(TYPE_NAME);
    }

    @Test
    void findAll_shouldReturnListOfTrainingTypes() {
        List<TrainingType> expectedList = List.of(mockTrainingType);
        when(trainingTypeDAO.findAll()).thenReturn(expectedList);

        List<TrainingType> all = trainingTypeService.findAll();

        assertNotNull(all);
        assertEquals(1, all.size());
        assertEquals(expectedList, all);
        assertEquals(TYPE_NAME, all.get(0).getTrainingTypeName());

        verify(trainingTypeDAO, times(1)).findAll();
    }
}
