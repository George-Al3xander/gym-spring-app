package io.github.George_Al3xander.service.impl;

import io.github.George_Al3xander.dto.workload.ActionType;
import io.github.George_Al3xander.dto.workload.WorkloadRequest;
import io.github.George_Al3xander.model.MonthWorkload;
import io.github.George_Al3xander.model.TrainerWorkload;
import io.github.George_Al3xander.model.YearWorkload;
import io.github.George_Al3xander.repository.TrainerWorkloadRepository;
import io.github.George_Al3xander.service.TrainerWorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

    private final TrainerWorkloadRepository trainerWorkloadRepository;

    @Override
    public TrainerWorkload handleTraining(WorkloadRequest request) {
        TrainerWorkload trainerWorkload = findTrainerWorkloadByRequest(request);

        YearWorkload yearWorkload =
                findYearWorkloadByTrainerAndYear(
                        trainerWorkload,
                        request.getTrainingDate().getYear(),
                        request.getActionType()
                );

        MonthWorkload monthWorkload =
                findMonthWorkloadByYearAndMonth(
                        yearWorkload,
                        request.getTrainingDate().getMonthValue(),
                        request.getActionType()
                );

        int delta = switch (request.getActionType()) {
            case ADD -> request.getTrainingDuration();
            case DELETE -> -request.getTrainingDuration();
        };

        int newDuration =
                monthWorkload.getTrainingSummaryDuration() + delta;

        if (newDuration < 0) {
            throw new IllegalArgumentException(
                    "Cannot delete more training duration than currently recorded"
            );
        }

        monthWorkload.setTrainingSummaryDuration(newDuration);

        return trainerWorkloadRepository.save(trainerWorkload);
    }

    private TrainerWorkload findTrainerWorkloadByRequest(
            WorkloadRequest request
    ) {
        return trainerWorkloadRepository
                .findByTrainerUsername(request.getTrainerUsername())
                .map(trainer -> {
                    trainer.setTrainerFirstName(request.getTrainerFirstName());
                    trainer.setTrainerLastName(request.getTrainerLastName());
                    trainer.setTrainerStatus(request.getActive());
                    return trainer;
                })
                .orElseGet(() -> {
                    if (request.getActionType() == ActionType.DELETE) {
                        throw new IllegalArgumentException(
                                "Cannot delete training for a trainer that does not exist"
                        );
                    }

                    TrainerWorkload trainer = new TrainerWorkload();
                    trainer.setTrainerUsername(request.getTrainerUsername());
                    trainer.setTrainerFirstName(request.getTrainerFirstName());
                    trainer.setTrainerLastName(request.getTrainerLastName());
                    trainer.setTrainerStatus(request.getActive());

                    return trainer;
                });
    }

    private YearWorkload findYearWorkloadByTrainerAndYear(
            TrainerWorkload trainerWorkload,
            int targetYear,
            ActionType actionType
    ) {
        return trainerWorkload.getYears().stream()
                .filter(year -> year.getYear() == targetYear)
                .findFirst()
                .orElseGet(() -> {
                    if (actionType == ActionType.DELETE) {
                        throw new IllegalArgumentException(
                                "Cannot delete training for a year that does not exist"
                        );
                    }

                    YearWorkload year = new YearWorkload();
                    year.setYear(targetYear);
                    year.setTrainerWorkload(trainerWorkload);
                    trainerWorkload.getYears().add(year);

                    return year;
                });
    }

    private MonthWorkload findMonthWorkloadByYearAndMonth(
            YearWorkload yearWorkload,
            int targetMonth,
            ActionType actionType
    ) {
        return yearWorkload.getMonths().stream()
                .filter(month -> month.getMonth() == targetMonth)
                .findFirst()
                .orElseGet(() -> {
                    if (actionType == ActionType.DELETE) {
                        throw new IllegalArgumentException(
                                "Cannot delete training for a month that does not exist"
                        );
                    }

                    MonthWorkload month = new MonthWorkload();
                    month.setMonth(targetMonth);
                    month.setTrainingSummaryDuration(0);
                    month.setYearWorkload(yearWorkload);
                    yearWorkload.getMonths().add(month);

                    return month;
                });
    }
}
