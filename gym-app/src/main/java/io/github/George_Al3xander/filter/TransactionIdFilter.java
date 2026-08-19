package io.github.George_Al3xander.filter;

import org.slf4j.MDC;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

public class TransactionIdFilter implements Filter {

    private static final String TRANSACTION_ID =
            "transactionId";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {


        String transactionId =
                UUID.randomUUID().toString();

        MDC.put(
                TRANSACTION_ID,
                transactionId
        );

        if (response instanceof HttpServletResponse) {

            ((HttpServletResponse) response)
                    .setHeader(
                            "X-Transaction-ID",
                            transactionId
                    );
        }

        try {

            chain.doFilter(
                    request,
                    response
            );

        } finally {

            MDC.remove(
                    TRANSACTION_ID
            );
        }
    }
}