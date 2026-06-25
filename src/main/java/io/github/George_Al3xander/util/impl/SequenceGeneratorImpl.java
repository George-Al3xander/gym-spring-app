package io.github.George_Al3xander.util.impl;

import io.github.George_Al3xander.util.SequenceGenerator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class SequenceGeneratorImpl implements SequenceGenerator {
    private final AtomicLong counter = new AtomicLong();

    @Override
    public long getNextSeq() {
        return counter.incrementAndGet();
    }

    @Override
    public void setCurrentSeq(long value) {
        counter.set(value);
    }
}
