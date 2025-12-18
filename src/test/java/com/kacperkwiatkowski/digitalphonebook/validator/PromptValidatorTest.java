package com.kacperkwiatkowski.digitalphonebook.validator;

import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;
import com.kacperkwiatkowski.digitalphonebook.enums.PromptKeys;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PromptValidatorTest {

    private static final String NAME_KEY = PromptKeys.NAME.getValue();
    private static final String NUMBER_KEY = PromptKeys.NUMBER.getValue();

    private static final String NAME_VALUE = "Joanna";
    private static final String NUMBER_VALUE = "22222222";
    private static final String EMPTY_VALUE = "";

    private final PromptValidator promptValidator = new PromptValidator();

    @Test
    void givenNullData_whenIsDataAvailableIsCalled_thenExceptionIsThrown() {
        // GIVEN
        StructuredCommand command = StructuredCommand.builder()
                .data(null)
                .build();

        // WHEN / THEN
        assertThrows(
                InvalidPromptException.class,
                () -> promptValidator.isDataAvailable(command)
        );
    }

    @Test
    void givenNonNullData_whenIsDataAvailableIsCalled_thenNoExceptionIsThrown() {
        // GIVEN
        StructuredCommand command = StructuredCommand.builder()
                .data(Map.of(NAME_KEY, NAME_VALUE))
                .build();

        // WHEN / THEN
        assertDoesNotThrow(() -> promptValidator.isDataAvailable(command));
    }

    @Test
    void givenInvalidKey_whenAreKeysValidIsCalled_thenExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                "invalidKey", NAME_VALUE
        );

        // WHEN / THEN
        assertThrows(
                InvalidPromptException.class,
                () -> promptValidator.areKeysValid(data)
        );
    }

    @Test
    void givenValidKeys_whenAreKeysValidIsCalled_thenNoExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        // WHEN / THEN
        assertDoesNotThrow(() -> promptValidator.areKeysValid(data));
    }

    @Test
    void givenAllValuesNullOrEmpty_whenIsAnyOfTheDataValuesPresentIsCalled_thenExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, EMPTY_VALUE,
                NUMBER_KEY, EMPTY_VALUE
        );

        // WHEN / THEN
        assertThrows(
                InvalidPromptException.class,
                () -> promptValidator.isAnyOfTheDataValuesPresent(data)
        );
    }

    @Test
    void givenAtLeastOneValuePresent_whenIsAnyOfTheDataValuesPresentIsCalled_thenNoExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, EMPTY_VALUE
        );

        // WHEN / THEN
        assertDoesNotThrow(
                () -> promptValidator.isAnyOfTheDataValuesPresent(data)
        );
    }
}