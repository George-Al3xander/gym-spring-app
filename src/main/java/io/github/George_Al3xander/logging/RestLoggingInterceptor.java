package io.github.George_Al3xander.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestLoggingInterceptor
        implements HandlerInterceptor {

    private static final Logger log =
            LoggerFactory.getLogger(
                    RestLoggingInterceptor.class
            );

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        log.info(
                "REST REQUEST method={} uri={}",
                request.getMethod(),
                request.getRequestURI()
        );

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {


        if (ex != null) {

            log.error(
                    "REST ERROR uri={} message={}",
                    request.getRequestURI(),
                    ex.getMessage()
            );

        }

        log.info(
                "REST RESPONSE uri={} status={}",
                request.getRequestURI(),
                response.getStatus()
        );
    }

}