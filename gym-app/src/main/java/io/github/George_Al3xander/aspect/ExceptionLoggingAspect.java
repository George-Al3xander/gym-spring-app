package io.github.George_Al3xander.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(ExceptionLoggingAspect.class);

    @AfterThrowing(
            pointcut = "execution(* io.github.George_Al3xander.service.impl.*.*(..))",
            throwing = "ex"
    )
    public void logException(Exception ex) {
        String message = ex.getMessage();

        if (message == null || message.isEmpty()) {
            message = "Something went wrong!";
        }

        log.warn(message, ex);
    }
}