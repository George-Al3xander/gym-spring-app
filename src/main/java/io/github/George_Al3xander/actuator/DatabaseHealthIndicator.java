package io.github.George_Al3xander.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    @Override
    public Health health() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(1000)) {
                return Health.up()
                        .withDetail("driverName", conn.getMetaData().getDriverName())
                        .withDetail("productName", conn.getMetaData().getDatabaseProductName())
                        .withDetail("productVersion", conn.getMetaData().getDatabaseProductVersion())
                        .build();
            }
        } catch (SQLException ex) {
            return Health.down()
                    .withException(ex)
                    .withDetail("error", "Connection failed: " + ex.getMessage())
                    .build();
        }
        return Health.unknown().build();
    }
}