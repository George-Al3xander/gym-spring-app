package io.github.George_Al3xander.auth;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class UsernameAuthorizationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final Counter counter;

    public UsernameAuthorizationInterceptor(JwtUtil jwtUtil, MeterRegistry meterRegistry) {
        this.jwtUtil = jwtUtil;
        counter = Counter.builder("api_username_authorization_forbidden_requests")
                .description("Number of requests rejected due to username authorization failure")
                .register(meterRegistry);
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        Map<String, String> pathVariables =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables == null || pathVariables.isEmpty()) {
            return true;
        }

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (username == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String pathUsername = pathVariables.get("username");

        if (pathUsername != null && !pathUsername.equals(jwtUtil.extractUsername(jwt))) {
            counter.increment();

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}