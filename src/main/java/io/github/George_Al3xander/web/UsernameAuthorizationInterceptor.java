package io.github.George_Al3xander.web;

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
    private final Counter counter;

    public UsernameAuthorizationInterceptor(MeterRegistry meterRegistry) {
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

        if (pathVariables == null) {
            return true;
        }

        String username = pathVariables.get("username");
        String authUsername = request.getHeader(AuthHttpHeader.USERNAME);

        if (username != null && !username.equals(authUsername)) {
            counter.increment();

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}