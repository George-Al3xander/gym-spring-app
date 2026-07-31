package io.github.George_Al3xander;

import io.github.George_Al3xander.config.MainConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MainConfig.class);

        context.close();
    }
    
}
