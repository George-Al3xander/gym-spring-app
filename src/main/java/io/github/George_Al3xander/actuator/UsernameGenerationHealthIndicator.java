package io.github.George_Al3xander.actuator;

import io.github.George_Al3xander.model.User;
import io.github.George_Al3xander.service.UsernameGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernameGenerationHealthIndicator implements HealthIndicator {

    private final UsernameGenerator usernameGenerator;

    @Override
    public Health health() {

        try {
            User user = new User();
            user.setFirstName("health");
            user.setLastName("check");

            String generatedUsername =
                    usernameGenerator.generateUsername(user);

            if (isValidUsername(generatedUsername)) {

                return Health.down()
                        .withDetail(
                                "reason",
                                "Invalid username format"
                        )
                        .build();
            }

            return Health.up()
                    .withDetail(
                            "generatedExample",
                            generatedUsername
                    )
                    .build();

        } catch (Exception e) {

            return Health.down()
                    .withDetail(
                            "reason",
                            e.getMessage()
                    )
                    .build();
        }
    }

    private boolean isValidUsername(String username) {
        return username == null ||
                !username.matches("[a-z]+\\.[a-z]+\\d*");
    }
}