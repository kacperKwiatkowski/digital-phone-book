package com.kacperkwiatkowski.digitalphonebook.mapper;

import com.kacperkwiatkowski.digitalphonebook.domain.Record;
import com.kacperkwiatkowski.digitalphonebook.dto.RecordDto;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RecordMapperTest {

    private static final String NAME_KEY = "name";
    private static final String NUMBER_KEY = "number";

    private static final String NAME_VALUE = "Joanna";
    private static final String NUMBER_VALUE = "22222222";

    private final RecordMapper recordMapper = new RecordMapper();

    @Test
    void givenValidDataMap_whenFromDtoToRecordIsCalled_thenRecordIsMappedCorrectly() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        // WHEN
        Record result = recordMapper.fromDtoToRecord(data);

        // THEN
        assertNotNull(result);
        assertEquals(NAME_VALUE, result.getName());
        assertEquals(NUMBER_VALUE, result.getNumber());
    }

    @Test
    void givenRecordEntity_whenFromEntityToDtoIsCalled_thenRecordDtoIsMappedCorrectly() {
        // GIVEN
        Record record = Record.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        // WHEN
        RecordDto result = recordMapper.fromEntityToDto(record);

        // THEN
        assertNotNull(result);
        assertEquals(NAME_VALUE, result.getName());
        assertEquals(NUMBER_VALUE, result.getNumber());
    }

    @Test
    void givenDataMapWithoutNumber_whenFromDtoToRecordIsCalled_thenNumberIsNull() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE
        );

        // WHEN
        Record result = recordMapper.fromDtoToRecord(data);

        // THEN
        assertNotNull(result);
        assertEquals(NAME_VALUE, result.getName());
        assertNull(result.getNumber());
    }

    @Test
    void givenDataMapWithoutName_whenFromDtoToRecordIsCalled_thenNameIsNull() {
        // GIVEN
        Map<String, String> data = Map.of(
                NUMBER_KEY, NUMBER_VALUE
        );

        // WHEN
        Record result = recordMapper.fromDtoToRecord(data);

        // THEN
        assertNotNull(result);
        assertNull(result.getName());
        assertEquals(NUMBER_VALUE, result.getNumber());
    }
}