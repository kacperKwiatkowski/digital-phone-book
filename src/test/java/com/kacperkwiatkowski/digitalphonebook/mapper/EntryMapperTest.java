package com.kacperkwiatkowski.digitalphonebook.mapper;

import com.kacperkwiatkowski.digitalphonebook.domain.Entry;
import com.kacperkwiatkowski.digitalphonebook.dto.EntryDto;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EntryMapperTest {

    private static final String NAME_KEY = "name";
    private static final String NUMBER_KEY = "number";

    private static final String NAME_VALUE = "Joanna";
    private static final String NUMBER_VALUE = "22222222";

    private final EntryMapper entryMapper = new EntryMapper();

    @Test
    void givenValidDataMap_whenFromDtoToEntryIsCalled_thenEntryIsMappedCorrectly() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        // WHEN
        Entry result = entryMapper.fromDtoToEntry(data);

        // THEN
        assertNotNull(result);
        assertEquals(NAME_VALUE, result.getName());
        assertEquals(NUMBER_VALUE, result.getNumber());
    }

    @Test
    void givenEntryEntity_whenFromEntityToDtoIsCalled_thenEntryDtoIsMappedCorrectly() {
        // GIVEN
        Entry entry = Entry.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        // WHEN
        EntryDto result = entryMapper.fromEntityToDto(entry);

        // THEN
        assertNotNull(result);
        assertEquals(NAME_VALUE, result.getName());
        assertEquals(NUMBER_VALUE, result.getNumber());
    }

    @Test
    void givenDataMapWithoutNumber_whenFromDtoToEntryIsCalled_thenNumberIsNull() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE
        );

        // WHEN
        Entry result = entryMapper.fromDtoToEntry(data);

        // THEN
        assertNotNull(result);
        assertEquals(NAME_VALUE, result.getName());
        assertNull(result.getNumber());
    }

    @Test
    void givenDataMapWithoutName_whenFromDtoToEntryIsCalled_thenNameIsNull() {
        // GIVEN
        Map<String, String> data = Map.of(
                NUMBER_KEY, NUMBER_VALUE
        );

        // WHEN
        Entry result = entryMapper.fromDtoToEntry(data);

        // THEN
        assertNotNull(result);
        assertNull(result.getName());
        assertEquals(NUMBER_VALUE, result.getNumber());
    }
}