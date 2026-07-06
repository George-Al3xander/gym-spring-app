package io.github.George_Al3xander;

import io.github.George_Al3xander.config.MainConfig;
import io.github.George_Al3xander.dto.CredentialsDTO;
import io.github.George_Al3xander.facade.GymFacade;
import io.github.George_Al3xander.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(MainConfig.class);

            GymFacade gymFacade = context.getBean(GymFacade.class);

            Trainee user = new Trainee(
                    null,
                    "John",
                    "Doe",
                    null,
                    null,
                    true,
                    LocalDate.now().minusYears(35),
                    "Cool St."
            );
            gymFacade.createTrainee(user);

            String username = user.getUsername();
            String password = user.getPassword();

            System.out.println(username + ": " + password);

            gymFacade.deleteTrainee(new CredentialsDTO(username, password), username);
            
            context.close();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
