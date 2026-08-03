package io.github.George_Al3xander.config;

import io.github.George_Al3xander.web.AuthHttpHeader;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
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
                        
                        Features:
                        - Spring Framework based architecture
                        - JPA/Hibernate persistence
                        - Flyway database migrations
                        - H2 development database
                        - MySQL production database
                        - Transaction management
                        - AOP exception logging
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
        )
)
@SecurityScheme(
        name = AuthHttpHeader.USERNAME,
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = AuthHttpHeader.USERNAME
)
@SecurityScheme(
        name = AuthHttpHeader.PASSWORD,
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = AuthHttpHeader.PASSWORD
)
public class SwaggerConfiguration {
}