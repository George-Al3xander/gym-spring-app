package io.github.George_Al3xander.web;

import io.github.George_Al3xander.dto.auth.CredentialsDTO;
import io.github.George_Al3xander.exception.BadCredentialsException;
import io.github.George_Al3xander.service.AuthenticationService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthHandlerInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;
    private final MeterRegistry meterRegistry;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (uri.contains("api-docs") || uri.contains("swagger") || uri.contains("/actuator")) {
            return true;
        }

        if ("POST".equalsIgnoreCase(method)) {
            if ("/trainees".equals(uri) || "/trainers".equals(uri)) {
                return true;
            }
        }

        if (!isAuthorized(request)) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return true;
    }

    private boolean isAuthorized(HttpServletRequest request) {
        return Timer.builder("api_methods_authentication_time")
                .description("Time spent authenticating API requests")
                .register(meterRegistry)
                .record(() -> {
                    CredentialsDTO credentials = new CredentialsDTO(
                            request.getHeader(AuthHttpHeader.USERNAME),
                            request.getHeader(AuthHttpHeader.PASSWORD)
                    );

                    return authenticationService.authenticate(credentials);
                });
    }
}
