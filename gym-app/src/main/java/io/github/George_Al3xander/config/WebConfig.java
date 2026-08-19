package io.github.George_Al3xander.config;

import io.github.George_Al3xander.auth.UsernameAuthorizationInterceptor;
import io.github.George_Al3xander.logging.RestLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("io.github.George_Al3xander.controller")
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    private final RestLoggingInterceptor restLoggingInterceptor;
    private final UsernameAuthorizationInterceptor usernameAuthorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restLoggingInterceptor);
        registry.addInterceptor(usernameAuthorizationInterceptor)
                .addPathPatterns("/trainees/**")
                .addPathPatterns("/trainers/**");
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("Content-Type", "Authorization")
                        .allowCredentials(true);
            }
        };
    }
}