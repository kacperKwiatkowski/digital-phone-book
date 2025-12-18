package com.kacperkwiatkowski.digitalphonebook.validator;

import com.kacperkwiatkowski.digitalphonebook.enums.PromptKeys;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import com.kacperkwiatkowski.digitalphonebook.repository.EntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntryValidatorTest {

    private static final String NAME_KEY = PromptKeys.NAME.getValue();
    private static final String NUMBER_KEY = PromptKeys.NUMBER.getValue();

    private static final String NAME_VALUE = "Joanna";
    private static final String NUMBER_VALUE = "22222222";
    private static final String EMPTY_VALUE = "";

    @Mock
    private EntryRepository entryRepository;

    @InjectMocks
    private EntryValidator entryValidator;

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
                () -> entryValidator.isOneOfTheDataValuesPresent(data)
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
        entryValidator.isOneOfTheDataValuesPresent(data);
    }

    @Test
    void givenEntryDoesNotExistInRepository_whenDoesEntryExistsInRepositoryIsCalled_thenExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(entryRepository.existsByNameOrNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(false);

        // WHEN / THEN
        assertThrows(
                InvalidPromptException.class,
                () -> entryValidator.doesEntryExistsInRepository(data)
        );
    }

    @Test
    void givenEntryExistsInRepository_whenDoesEntryExistsInRepositoryIsCalled_thenNoExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(entryRepository.existsByNameOrNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(true);

        // WHEN / THEN
        entryValidator.doesEntryExistsInRepository(data);
    }

    @Test
    void givenEntryAlreadyExists_whenIsEntryNonExistentIsCalled_thenExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(entryRepository.countByNameAndNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(1);

        // WHEN / THEN
        assertThrows(
                InvalidPromptException.class,
                () -> entryValidator.isEntryNonExistent(data)
        );
    }

    @Test
    void givenEntryDoesNotExist_whenIsEntryNonExistentIsCalled_thenNoExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(entryRepository.countByNameAndNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(0);

        // WHEN / THEN
        entryValidator.isEntryNonExistent(data);
    }

    @Test
    void givenEntryIsNotUnique_whenIsEntryUniqueIsCalled_thenExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(entryRepository.countByNameOrNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(2);

        // WHEN / THEN
        assertThrows(
                InvalidPromptException.class,
                () -> entryValidator.isEntryUnique(data)
        );
    }

    @Test
    void givenEntryIsUnique_whenIsEntryUniqueIsCalled_thenNoExceptionIsThrown() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        when(entryRepository.countByNameOrNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(1);

        // WHEN / THEN
        entryValidator.isEntryUnique(data);
    }
}
