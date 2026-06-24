package io.github.George_Al3xander.util;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class PasswordGeneratorTest {

    private static final String VALID_CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "!@#$%^&*";


    @Test
    void givenLengthTen_whenGeneratePassword_thenReturnsPasswordWithLengthTen() {
        String password = PasswordGenerator.generatePassword(10);

        assertEquals(10, password.length());
    }


    @Test
    void givenValidLength_whenGeneratePassword_thenContainsOnlyAllowedCharacters() {
        String password = PasswordGenerator.generatePassword(10);

        assertTrue(
                password.chars()
                        .allMatch(character ->
                                VALID_CHARACTERS.indexOf(character) >= 0)
        );
    }


    @Test
    void givenSameLength_whenGeneratePasswordTwice_thenReturnsDifferentPasswords() {
        String firstPassword = PasswordGenerator.generatePassword(10);
        String secondPassword = PasswordGenerator.generatePassword(10);

        assertNotEquals(firstPassword, secondPassword);
    }


    @Test
    void givenLengthOne_whenGeneratePassword_thenReturnsSingleAllowedCharacter() {
        String password = PasswordGenerator.generatePassword(1);

        assertEquals(1, password.length());

        assertTrue(
                VALID_CHARACTERS.contains(password)
        );
    }


    @Test
    void givenLengthZero_whenGeneratePassword_thenReturnsEmptyPassword() {
        String password = PasswordGenerator.generatePassword(0);

        assertNotNull(password);
        assertTrue(password.isEmpty());
    }


    @Test
    void givenLargeLength_whenGeneratePassword_thenReturnsPasswordWithRequestedLength() {
        String password = PasswordGenerator.generatePassword(1000);

        assertEquals(1000, password.length());
    }


    @Test
    void givenNegativeLength_whenGeneratePassword_thenThrowsException() {
        assertThrows(
                NegativeArraySizeException.class,
                () -> PasswordGenerator.generatePassword(-1)
        );
    }


    @Test
    void givenMultipleGenerations_whenGeneratePassword_thenAlwaysReturnsRequestedLength() {
        IntStream.range(0, 100)
                .forEach(index -> {
                    String password = PasswordGenerator.generatePassword(10);

                    assertEquals(10, password.length());
                });
    }
}