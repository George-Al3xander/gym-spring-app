package io.github.George_Al3xander;

import io.github.common.App;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AppTest {

    @Test
    void mainRunsWithoutExceptions() {
        assertDoesNotThrow(() -> App.main(new String[]{}));
    }
}