package io.github.George_Al3xander.dao.impl;

import io.github.George_Al3xander.config.TestConfig;
import io.github.George_Al3xander.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
class TrainingTypeDaoImplTest {

    @Autowired
    private TrainingTypeDaoImpl dao;

    private TrainingType createTrainingType(String name) {
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName(name);
        return trainingType;
    }

    @Test
    void givenValidTrainingType_whenSave_thenTrainingTypePersistedWithId() {
        TrainingType trainingType = createTrainingType("Yoga");

        TrainingType saved = dao.save(trainingType);

        assertNotNull(saved.getId());
        assertEquals("Yoga", saved.getTrainingTypeName());

        Optional<TrainingType> found = dao.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Yoga", found.get().getTrainingTypeName());
    }

    @Test
    void givenPersistedTrainingType_whenFindById_thenReturnSameEntity() {
        TrainingType trainingType = dao.save(createTrainingType("Boxing"));

        Optional<TrainingType> found = dao.findById(trainingType.getId());

        assertTrue(found.isPresent());
        assertEquals("Boxing", found.get().getTrainingTypeName());
    }

    @Test
    void givenUnknownId_whenFindById_thenReturnEmptyOptional() {
        Optional<TrainingType> found = dao.findById(999L);

        assertTrue(found.isEmpty());
    }

    @Test
    void givenMultipleTrainingTypes_whenFindAll_thenReturnAllEntities() {
        dao.save(createTrainingType("A"));
        dao.save(createTrainingType("B"));

        List<TrainingType> result = dao.findAll();

        assertTrue(result.stream()
                .anyMatch(t -> "A".equals(t.getTrainingTypeName())));
        assertTrue(result.stream()
                .anyMatch(t -> "B".equals(t.getTrainingTypeName())));
    }

    @Test
    void givenPersistedTrainingType_whenUpdate_thenChangesArePersisted() {
        TrainingType trainingType = dao.save(createTrainingType("Old"));

        trainingType.setTrainingTypeName("Updated");

        dao.update(trainingType);

        Optional<TrainingType> updated = dao.findById(trainingType.getId());

        assertTrue(updated.isPresent());
        assertEquals("Updated", updated.get().getTrainingTypeName());
    }

    @Test
    void givenDetachedTrainingType_whenUpdate_thenEntityIsStillUpdated() {
        TrainingType trainingType = dao.save(createTrainingType("Detached"));

        Long id = trainingType.getId();

        TrainingType detachedCopy = createTrainingType("Detached Updated");
        detachedCopy.setId(id);

        dao.update(detachedCopy);

        Optional<TrainingType> updated = dao.findById(id);

        assertTrue(updated.isPresent());
        assertEquals("Detached Updated", updated.get().getTrainingTypeName());
    }

    @Test
    void givenExistingTrainingType_whenDelete_thenEntityIsRemoved() {
        TrainingType trainingType = dao.save(createTrainingType("ToDelete"));

        dao.delete(trainingType.getId());

        Optional<TrainingType> found = dao.findById(trainingType.getId());

        assertTrue(found.isEmpty());
    }

    @Test
    void givenSavedEntities_whenFindAll_thenReflectAllChanges() {
        TrainingType a = dao.save(createTrainingType("A"));
        TrainingType b = dao.save(createTrainingType("B"));

        dao.delete(a.getId());

        List<TrainingType> result = dao.findAll();

        assertTrue(result.stream()
                .noneMatch(t -> "A".equals(t.getTrainingTypeName())));
        assertTrue(result.stream()
                .anyMatch(t -> "B".equals(t.getTrainingTypeName())));
    }
}