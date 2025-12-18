package com.kacperkwiatkowski.digitalphonebook.service;

import com.kacperkwiatkowski.digitalphonebook.domain.Record;
import com.kacperkwiatkowski.digitalphonebook.dto.RecordDto;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import com.kacperkwiatkowski.digitalphonebook.enums.PromptKeys;
import com.kacperkwiatkowski.digitalphonebook.mapper.RecordMapper;
import com.kacperkwiatkowski.digitalphonebook.repository.RecordRepository;
import com.kacperkwiatkowski.digitalphonebook.validator.RecordValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordServiceImplTest {

    private static final String NAME_KEY = PromptKeys.NAME.getValue();
    private static final String NUMBER_KEY = PromptKeys.NUMBER.getValue();

    private static final String NAME_VALUE = "Joanna";
    private static final String NUMBER_VALUE = "22222222";

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private RecordMapper recordMapper;

    @Mock
    private RecordValidator recordValidator;

    @InjectMocks
    private RecordServiceImpl recordService;

    @Test
    void givenValidData_whenCreateIsCalled_thenRecordIsSavedAndReturned() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        Record record = Record.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        RecordDto recordDto = RecordDto.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        when(recordMapper.fromDtoToRecord(data)).thenReturn(record);
        when(recordRepository.save(record)).thenReturn(record);
        when(recordMapper.fromEntityToDto(record)).thenReturn(recordDto);

        // WHEN
        PromptResponse result = recordService.create(data);

        // THEN
        verify(recordValidator).isRecordNonExistent(data);
        verify(recordRepository).save(record);
        assertEquals(Operation.CREATE, result.getOperation());
        assertEquals(recordDto, result.getRecord());
    }

    @Test
    void givenValidData_whenFindIsCalled_thenRecordIsReturned() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE
        );

        Record record = Record.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        RecordDto recordDto = RecordDto.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        when(recordRepository.findByNameOrNumber(NAME_VALUE, null)).thenReturn(record);
        when(recordMapper.fromEntityToDto(record)).thenReturn(recordDto);

        // WHEN
        PromptResponse result = recordService.find(data);

        // THEN
        verify(recordValidator).doesRecordExistsInRepository(data);
        verify(recordValidator).isRecordUnique(data);
        verify(recordRepository).findByNameOrNumber(NAME_VALUE, null);
        assertEquals(Operation.READ, result.getOperation());
        assertEquals(recordDto, result.getRecord());
    }

    @Test
    void givenValidData_whenUpdateIsCalled_thenRecordIsUpdated() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        Record existingRecord = Record.builder()
                .name("OldName")
                .number("11111111")
                .build();

        RecordDto recordDto = RecordDto.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        when(recordRepository.findByNameOrNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(existingRecord);
        when(recordRepository.save(existingRecord))
                .thenReturn(existingRecord);
        when(recordMapper.fromEntityToDto(existingRecord))
                .thenReturn(recordDto);

        // WHEN
        PromptResponse result = recordService.update(data);

        // THEN
        verify(recordValidator).isOneOfTheDataValuesPresent(data);
        verify(recordValidator).doesRecordExistsInRepository(data);
        verify(recordValidator).isRecordUnique(data);
        verify(recordRepository).save(existingRecord);

        assertEquals(NAME_VALUE, existingRecord.getName());
        assertEquals(NUMBER_VALUE, existingRecord.getNumber());
        assertEquals(Operation.UPDATE, result.getOperation());
    }

    @Test
    void givenValidData_whenDeleteIsCalled_thenRecordIsDeleted() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE
        );

        Record record = Record.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        RecordDto recordDto = RecordDto.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        when(recordRepository.findByNameOrNumber(NAME_VALUE, NUMBER_KEY))
                .thenReturn(record);
        when(recordMapper.fromEntityToDto(record))
                .thenReturn(recordDto);

        // WHEN
        PromptResponse result = recordService.delete(data);

        // THEN
        verify(recordValidator).doesRecordExistsInRepository(data);
        verify(recordValidator).isRecordUnique(data);
        verify(recordRepository).delete(record);
        assertEquals(Operation.DELETE, result.getOperation());
        assertEquals(recordDto, result.getRecord());
    }

    @Test
    void givenEntriesInRepository_whenFindAllIsCalled_thenAllEntriesAreReturned() {
        // GIVEN
        Record record = Record.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        RecordDto recordDto = RecordDto.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        when(recordRepository.findAll()).thenReturn(List.of(record));
        when(recordMapper.fromEntityToDto(record)).thenReturn(recordDto);

        // WHEN
        List<RecordDto> result = recordService.findAll();

        // THEN
        assertEquals(1, result.size());
        assertEquals(recordDto, result.get(0));
        verify(recordRepository).findAll();
    }
}