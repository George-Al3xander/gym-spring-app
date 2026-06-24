package io.github.George_Al3xander;

import io.github.George_Al3xander.config.MainConfig;
import io.github.George_Al3xander.model.User;
import io.github.George_Al3xander.service.UsernameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        try {
            AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(MainConfig.class);

            UsernameGenerator usernameService = context.getBean(UsernameGenerator.class);

            User user = new User();
            user.setFirstName("John");
            user.setLastName("Doe");

            System.out.println(usernameService.generateUsername(user));
            context.close();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }
}
