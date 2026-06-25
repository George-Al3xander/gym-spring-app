package io.github.George_Al3xander.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "io.github.George_Al3xander")
@PropertySource("classpath:application.properties")
public class MainConfig {
}
