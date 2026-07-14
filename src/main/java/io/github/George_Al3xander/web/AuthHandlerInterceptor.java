package io.github.George_Al3xander.web;

import io.github.George_Al3xander.dto.CredentialsDTO;
import io.github.George_Al3xander.exception.BadCredentialsException;
import io.github.George_Al3xander.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthHandlerInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        String method = request.getMethod();

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
        CredentialsDTO credentials = new CredentialsDTO(
                request.getHeader(AuthHttpHeader.USERNAME),
                request.getHeader(AuthHttpHeader.PASSWORD)
        );

        return authenticationService.authenticate(credentials);
    }
}
