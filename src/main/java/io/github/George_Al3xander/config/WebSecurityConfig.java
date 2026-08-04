package io.github.George_Al3xander.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers(
                                        "/actuator/**",
                                        "/swagger-ui/**",
                                        "/swagger-resources/**",
                                        "/v2/api-docs/**",
                                        "/v3/api-docs/**"
                                ).permitAll()
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/trainees",
                                        "/trainers"
                                ).permitAll()
                                .anyRequest()
                                .authenticated()

                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
