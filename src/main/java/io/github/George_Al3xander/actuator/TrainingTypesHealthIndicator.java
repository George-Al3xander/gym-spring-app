package io.github.George_Al3xander.actuator;

import io.github.George_Al3xander.dao.TrainingTypeDao;
import io.github.George_Al3xander.model.TrainingType;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrainingTypesHealthIndicator implements HealthIndicator {

    private static final int EXPECTED_TRAINING_TYPE_COUNT = 4;

    private final TrainingTypeDao trainingTypeDao;

    @Override
    public @Nullable Health health() {
        List<TrainingType> trainingTypes = trainingTypeDao.findAll();

        if (trainingTypes.size() == EXPECTED_TRAINING_TYPE_COUNT) {
            return Health.up()
                    .withDetail("expectedCount", EXPECTED_TRAINING_TYPE_COUNT)
                    .withDetail("actualCount", trainingTypes.size())
                    .withDetail("description",
                            "The TrainingTypes table contains a predefined, immutable set of values " +
                                    "and cannot be modified through the application.")
                    .build();
        }

        return Health.down()
                .withDetail("reason",
                        "The TrainingTypes table does not contain the expected predefined data.")
                .withDetail("expectedCount", EXPECTED_TRAINING_TYPE_COUNT)
                .withDetail("actualCount", trainingTypes.size())
                .withDetail("description",
                        "The application expects the TrainingTypes table to contain a fixed, " +
                                "immutable set of values. Any missing or additional records indicate " +
                                "that the database is in an unexpected state.")
                .build();
    }
}