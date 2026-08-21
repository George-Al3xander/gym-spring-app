package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dto.workload.ActionType;
import io.github.George_Al3xander.dto.workload.WorkloadRequest;
import io.github.George_Al3xander.model.MonthWorkload;
import io.github.George_Al3xander.model.TrainerWorkload;
import io.github.George_Al3xander.model.YearWorkload;
import io.github.George_Al3xander.repository.TrainerWorkloadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TrainerWorkloadServiceImpl.class)
class TrainerWorkloadServiceImplTestIT {

    @Autowired
    private TrainerWorkloadRepository trainerWorkloadRepository;

    @Autowired
    private TrainerWorkloadServiceImpl trainerWorkloadService;

    @BeforeEach
    void setUp() {
        trainerWorkloadRepository.deleteAll();
    }

    @Test
    void givenNewTrainer_whenAddingTraining_thenCreateTrainerYearAndMonth() {
        WorkloadRequest request = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                8,
                ActionType.ADD
        );

        TrainerWorkload result =
                trainerWorkloadService.handleTraining(request);

        assertNotNull(result);
        assertEquals("john.doe", result.getTrainerUsername());
        assertEquals("John", result.getTrainerFirstName());
        assertEquals("Doe", result.getTrainerLastName());
        assertEquals(true, result.isTrainerStatus());

        assertEquals(1, result.getYears().size());

        YearWorkload year = result.getYears().get(0);

        assertEquals(2026, year.getYear());
        assertEquals(1, year.getMonths().size());

        MonthWorkload month = year.getMonths().get(0);

        assertEquals(8, month.getMonth());
        assertEquals(8, month.getTrainingSummaryDuration());
    }

    @Test
    void givenNewTrainer_whenAddingTraining_thenPersistTrainer() {
        WorkloadRequest request = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                8,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(request);

        TrainerWorkload persisted =
                trainerWorkloadRepository
                        .findByTrainerUsername("john.doe")
                        .orElseThrow();

        assertEquals("john.doe", persisted.getTrainerUsername());
        assertEquals("John", persisted.getTrainerFirstName());
        assertEquals("Doe", persisted.getTrainerLastName());
        assertEquals(true, persisted.isTrainerStatus());
    }

    @Test
    void givenExistingTrainer_whenAddingTraining_thenUpdateTrainerMetadata() {
        WorkloadRequest firstRequest = createRequest(
                "john.doe",
                "Old",
                "Name",
                false,
                LocalDate.of(2026, 8, 10),
                5,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(firstRequest);

        WorkloadRequest secondRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 15),
                3,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(secondRequest);

        TrainerWorkload persisted =
                trainerWorkloadRepository
                        .findByTrainerUsername("john.doe")
                        .orElseThrow();

        assertEquals("John", persisted.getTrainerFirstName());
        assertEquals("Doe", persisted.getTrainerLastName());
        assertEquals(true, persisted.isTrainerStatus());
    }

    @Test
    void givenExistingMonth_whenAddingTraining_thenIncreaseDuration() {
        WorkloadRequest firstRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                5,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(firstRequest);

        WorkloadRequest secondRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 20),
                3,
                ActionType.ADD
        );

        TrainerWorkload result =
                trainerWorkloadService.handleTraining(secondRequest);

        MonthWorkload month =
                findMonthWorkload(result, 2026, 8);

        assertEquals(8, month.getTrainingSummaryDuration());
    }

    @Test
    void givenExistingYearWithoutMonth_whenAddingTraining_thenCreateNewMonth() {
        WorkloadRequest augustRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                5,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(augustRequest);

        WorkloadRequest septemberRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 9, 10),
                7,
                ActionType.ADD
        );

        TrainerWorkload result =
                trainerWorkloadService.handleTraining(septemberRequest);

        assertEquals(2, result.getYears().get(0).getMonths().size());

        assertEquals(
                5,
                findMonthWorkload(result, 2026, 8)
                        .getTrainingSummaryDuration()
        );

        assertEquals(
                7,
                findMonthWorkload(result, 2026, 9)
                        .getTrainingSummaryDuration()
        );
    }

    @Test
    void givenExistingTrainer_whenAddingTrainingForDifferentYear_thenCreateSeparateYear() {
        WorkloadRequest request2026 = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                5,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(request2026);

        WorkloadRequest request2027 = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2027, 8, 10),
                7,
                ActionType.ADD
        );

        TrainerWorkload result =
                trainerWorkloadService.handleTraining(request2027);

        assertEquals(2, result.getYears().size());

        YearWorkload year2026 =
                findYearWorkload(result, 2026);

        YearWorkload year2027 =
                findYearWorkload(result, 2027);

        assertEquals(
                5,
                findMonthWorkload(year2026, 8)
                        .getTrainingSummaryDuration()
        );

        assertEquals(
                7,
                findMonthWorkload(year2027, 8)
                        .getTrainingSummaryDuration()
        );
    }

    @Test
    void givenDifferentTrainers_whenAddingTraining_thenPersistIndependentWorkloads() {
        WorkloadRequest johnRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                5,
                ActionType.ADD
        );

        WorkloadRequest janeRequest = createRequest(
                "jane.doe",
                "Jane",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                8,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(johnRequest);
        trainerWorkloadService.handleTraining(janeRequest);

        TrainerWorkload john =
                trainerWorkloadRepository
                        .findByTrainerUsername("john.doe")
                        .orElseThrow();

        TrainerWorkload jane =
                trainerWorkloadRepository
                        .findByTrainerUsername("jane.doe")
                        .orElseThrow();

        assertEquals(
                5,
                findMonthWorkload(john, 2026, 8)
                        .getTrainingSummaryDuration()
        );

        assertEquals(
                8,
                findMonthWorkload(jane, 2026, 8)
                        .getTrainingSummaryDuration()
        );
    }

    @Test
    void givenExistingTraining_whenDeletingTraining_thenDecreaseDuration() {
        WorkloadRequest addRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                10,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(addRequest);

        WorkloadRequest deleteRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                4,
                ActionType.DELETE
        );

        TrainerWorkload result =
                trainerWorkloadService.handleTraining(deleteRequest);

        MonthWorkload month =
                findMonthWorkload(result, 2026, 8);

        assertEquals(6, month.getTrainingSummaryDuration());
    }

    @Test
    void givenExistingTrainingWithExactDuration_whenDeletingTraining_thenSetDurationToZero() {
        WorkloadRequest addRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                10,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(addRequest);

        WorkloadRequest deleteRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                10,
                ActionType.DELETE
        );

        TrainerWorkload result =
                trainerWorkloadService.handleTraining(deleteRequest);

        MonthWorkload month =
                findMonthWorkload(result, 2026, 8);

        assertEquals(0, month.getTrainingSummaryDuration());
    }

    @Test
    void givenExistingTrainingWithInsufficientDuration_whenDeletingTraining_thenThrowIllegalArgumentException() {
        WorkloadRequest addRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                5,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(addRequest);

        WorkloadRequest deleteRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                6,
                ActionType.DELETE
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trainerWorkloadService.handleTraining(deleteRequest)
        );

        assertEquals(
                "Cannot delete more training duration than currently recorded",
                exception.getMessage()
        );

        TrainerWorkload persisted =
                trainerWorkloadRepository
                        .findByTrainerUsername("john.doe")
                        .orElseThrow();

        assertEquals(
                5,
                findMonthWorkload(persisted, 2026, 8)
                        .getTrainingSummaryDuration()
        );
    }

    @Test
    void givenNonExistingTrainer_whenDeletingTraining_thenThrowIllegalArgumentException() {
        WorkloadRequest request = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                5,
                ActionType.DELETE
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trainerWorkloadService.handleTraining(request)
        );

        assertEquals(
                "Cannot delete training for a trainer that does not exist",
                exception.getMessage()
        );

        assertEquals(
                0,
                trainerWorkloadRepository.count()
        );
    }

    @Test
    void givenNonExistingYear_whenDeletingTraining_thenThrowIllegalArgumentException() {
        WorkloadRequest addRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                5,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(addRequest);

        WorkloadRequest deleteRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2027, 8, 10),
                5,
                ActionType.DELETE
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trainerWorkloadService.handleTraining(deleteRequest)
        );

        assertEquals(
                "Cannot delete training for a year that does not exist",
                exception.getMessage()
        );

        TrainerWorkload persisted =
                trainerWorkloadRepository
                        .findByTrainerUsername("john.doe")
                        .orElseThrow();

        assertEquals(1, persisted.getYears().size());
        assertEquals(2026, persisted.getYears().get(0).getYear());
    }

    @Test
    void givenNonExistingMonth_whenDeletingTraining_thenThrowIllegalArgumentException() {
        WorkloadRequest addRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                5,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(addRequest);

        WorkloadRequest deleteRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 9, 10),
                5,
                ActionType.DELETE
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> trainerWorkloadService.handleTraining(deleteRequest)
        );

        assertEquals(
                "Cannot delete training for a month that does not exist",
                exception.getMessage()
        );

        TrainerWorkload persisted =
                trainerWorkloadRepository
                        .findByTrainerUsername("john.doe")
                        .orElseThrow();

        assertEquals(
                5,
                findMonthWorkload(persisted, 2026, 8)
                        .getTrainingSummaryDuration()
        );
    }

    @Test
    void givenZeroDuration_whenAddingTraining_thenKeepDurationUnchanged() {
        WorkloadRequest firstRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                10,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(firstRequest);

        WorkloadRequest zeroRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                0,
                ActionType.ADD
        );

        TrainerWorkload result =
                trainerWorkloadService.handleTraining(zeroRequest);

        assertEquals(
                10,
                findMonthWorkload(result, 2026, 8)
                        .getTrainingSummaryDuration()
        );
    }

    @Test
    void givenZeroDuration_whenDeletingTraining_thenKeepDurationUnchanged() {
        WorkloadRequest firstRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                10,
                ActionType.ADD
        );

        trainerWorkloadService.handleTraining(firstRequest);

        WorkloadRequest zeroRequest = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                0,
                ActionType.DELETE
        );

        TrainerWorkload result =
                trainerWorkloadService.handleTraining(zeroRequest);

        assertEquals(
                10,
                findMonthWorkload(result, 2026, 8)
                        .getTrainingSummaryDuration()
        );
    }

    @Test
    void givenNegativeDuration_whenAddingTraining_thenThrowIllegalArgumentException() {
        WorkloadRequest request = createRequest(
                "john.doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2026, 8, 10),
                -1,
                ActionType.ADD
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> trainerWorkloadService.handleTraining(request)
        );

        assertEquals(
                0,
                trainerWorkloadRepository.count()
        );
    }

    private WorkloadRequest createRequest(
            String username,
            String firstName,
            String lastName,
            boolean active,
            LocalDate date,
            int duration,
            ActionType actionType
    ) {
        WorkloadRequest request = new WorkloadRequest();

        request.setTrainerUsername(username);
        request.setTrainerFirstName(firstName);
        request.setTrainerLastName(lastName);
        request.setActive(active);
        request.setTrainingDate(date);
        request.setTrainingDuration(duration);
        request.setActionType(actionType);

        return request;
    }

    private YearWorkload findYearWorkload(
            TrainerWorkload workload,
            int year
    ) {
        return workload.getYears()
                .stream()
                .filter(y -> y.getYear() == year)
                .findFirst()
                .orElseThrow();
    }

    private MonthWorkload findMonthWorkload(
            TrainerWorkload workload,
            int year,
            int month
    ) {
        return findMonthWorkload(
                findYearWorkload(workload, year),
                month
        );
    }

    private MonthWorkload findMonthWorkload(
            YearWorkload year,
            int month
    ) {
        return year.getMonths()
                .stream()
                .filter(m -> m.getMonth() == month)
                .findFirst()
                .orElseThrow();
    }
}
