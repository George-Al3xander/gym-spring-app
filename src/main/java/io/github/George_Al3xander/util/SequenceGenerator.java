package io.github.George_Al3xander.util;

public interface SequenceGenerator {
    long getNextSeq();

    void setCurrentSeq(long value);
}