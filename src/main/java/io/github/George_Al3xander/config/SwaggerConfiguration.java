package io.github.George_Al3xander.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.github.George_Al3xander.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Gym CRM System REST API")
                .description("""
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
                        """)
                .contact(new Contact(
                        "George Al3xander",
                        "https://github.com/George_Al3xander",
                        "george.al3xander@example.com"
                ))
                .license("Apache License 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .version("1.0.0")
                .build();
    }
}