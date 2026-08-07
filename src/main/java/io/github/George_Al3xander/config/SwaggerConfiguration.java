package io.github.George_Al3xander.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gym CRM System REST API",
                version = "1.0.0",
                description = """
                        REST API for Gym CRM System.
                        
                        The application manages:
                        - Trainees
                        - Trainers
                        - Trainings
                        - User authentication
                        - Account activation/deactivation
                        - Password management
                        
                        Security:
                        - JWT Bearer authentication
                        """,
                contact = @Contact(
                        name = "George Al3xander",
                        url = "https://github.com/George_Al3xander",
                        email = "george.al3xander@example.com"
                ),
                license = @License(
                        name = "Apache License 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT authentication. Enter: Bearer {token}",
        scheme = "bearer",
        bearerFormat = "JWT",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfiguration {
}