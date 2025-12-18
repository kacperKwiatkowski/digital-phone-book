package com.kacperkwiatkowski.digitalphonebook.validator;

import com.kacperkwiatkowski.digitalphonebook.enums.PromptKeys;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import com.kacperkwiatkowski.digitalphonebook.repository.RecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordValidatorTest {

    private static final String NAME_KEY = PromptKeys.NAME.getValue();
    private static final String NUMBER_KEY = PromptKeys.NUMBER.getValue();

    private static final String NAME_VALUE = "Joanna";
    private static final String NUMBER_VALUE = "22222222";
    private static final String EMPTY_VALUE = "";

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordValidator recordValidator;

    @Test
    void givenAllValuesNullOrEmpty_whenIsOneOfTheDataValuesPresentIsCalled_thenExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, EMPTY_VALUE,
                NUMBER_KEY, EMPTY_VALUE
        );

        // WHEN / THEN
        assertThrows(
                InvalidPromptException.class,
                () -> recordValidator.isOneOfTheDataValuesPresent(data)
        );
    }

    @Test
    void givenAtLeastOneValuePresent_whenIsOneOfTheDataValuesPresentIsCalled_thenNoExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, EMPTY_VALUE
        );

        // WHEN / THEN
        recordValidator.isOneOfTheDataValuesPresent(data);
    }

    @Test
    void givenRecordDoesNotExistInRepository_whenDoesRecordExistsInRepositoryIsCalled_thenExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(recordRepository.existsByNameOrNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(false);

        // WHEN / THEN
        assertThrows(
                InvalidPromptException.class,
                () -> recordValidator.doesRecordExistsInRepository(data)
        );
    }

    @Test
    void givenRecordExistsInRepository_whenDoesRecordExistsInRepositoryIsCalled_thenNoExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(recordRepository.existsByNameOrNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(true);

        // WHEN / THEN
        recordValidator.doesRecordExistsInRepository(data);
    }

    @Test
    void givenRecordAlreadyExists_whenIsRecordNonExistentIsCalled_thenExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(recordRepository.countByNameAndNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(1);

        // WHEN / THEN
        assertThrows(
                InvalidPromptException.class,
                () -> recordValidator.isRecordNonExistent(data)
        );
    }

    @Test
    void givenRecordDoesNotExist_whenIsRecordNonExistentIsCalled_thenNoExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(recordRepository.countByNameAndNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(0);

        // WHEN / THEN
        recordValidator.isRecordNonExistent(data);
    }

    @Test
    void givenRecordIsNotUnique_whenIsRecordUniqueIsCalled_thenExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(recordRepository.countByNameOrNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(2);

        // WHEN / THEN
        assertThrows(
                InvalidPromptException.class,
                () -> recordValidator.isRecordUnique(data)
        );
    }

    @Test
    void givenRecordIsUnique_whenIsRecordUniqueIsCalled_thenNoExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(recordRepository.countByNameOrNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(1);

        // WHEN / THEN
        recordValidator.isRecordUnique(data);
    }
}
