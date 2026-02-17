package epam.com.gym.crm.storage;

import epam.com.gym.crm.model.BaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GenericInMemoryStorageTest {
    private GenericInMemoryStorage<TestEntity> storage;

    @BeforeEach
    void setUp() {
        storage = new GenericInMemoryStorage<>();
    }

    static class TestEntity extends BaseEntity {
    }

    @Test
    void save_shouldAssignIdWhenNewEntity() {
        TestEntity entity = new TestEntity();

        TestEntity saved = storage.save(entity);

        assertNotNull(saved.getId(), "ID should be assigned");
        assertEquals(saved, storage.findById(saved.getId()).orElse(null));
    }

    @Test
    void save_shouldKeepExistingId() {
        TestEntity entity = new TestEntity();
        entity.setId(100L);

        TestEntity saved = storage.save(entity);

        assertEquals(100L, saved.getId());
        assertEquals(saved, storage.findById(100L).orElse(null));
    }

    @Test
    void findById_shouldReturnOptionalEmptyWhenNotFound() {
        Optional<TestEntity> result = storage.findById(999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllSavedEntities() {
        TestEntity e1 = new TestEntity();
        TestEntity e2 = new TestEntity();

        storage.save(e1);
        storage.save(e2);

        List<TestEntity> all = storage.findAll();

        assertEquals(2, all.size());
        assertTrue(all.contains(e1));
        assertTrue(all.contains(e2));
    }

    @Test
    void delete_shouldRemoveEntityById() {
        TestEntity entity = new TestEntity();
        storage.save(entity);
        Long id = entity.getId();

        storage.delete(id);

        assertFalse(storage.findById(id).isPresent());
        assertEquals(0, storage.findAll().size());
    }

    @Test
    void delete_shouldDoNothingIfIdNotExist() {
        storage.delete(999L); // no exception should be thrown
        assertTrue(storage.findAll().isEmpty());
    }

    @Test
    void save_shouldIncrementIdCorrectly() {
        TestEntity e1 = new TestEntity();
        TestEntity e2 = new TestEntity();

        storage.save(e1);
        storage.save(e2);

        assertEquals(e1.getId() + 1, e2.getId());
    }
}
