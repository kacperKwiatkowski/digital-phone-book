package com.kacperkwiatkowski.digitalphonebook.service;

import com.kacperkwiatkowski.digitalphonebook.domain.Entry;
import com.kacperkwiatkowski.digitalphonebook.dto.EntryDto;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import com.kacperkwiatkowski.digitalphonebook.enums.PromptKeys;
import com.kacperkwiatkowski.digitalphonebook.mapper.EntryMapper;
import com.kacperkwiatkowski.digitalphonebook.repository.EntryRepository;
import com.kacperkwiatkowski.digitalphonebook.validator.EntryValidator;
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
class EntryServiceImplTest {

    private static final String NAME_KEY = PromptKeys.NAME.getValue();
    private static final String NUMBER_KEY = PromptKeys.NUMBER.getValue();

    private static final String NAME_VALUE = "Joanna";
    private static final String NUMBER_VALUE = "22222222";

    @Mock
    private EntryRepository entryRepository;

    @Mock
    private EntryMapper entryMapper;

    @Mock
    private EntryValidator entryValidator;

    @InjectMocks
    private EntryServiceImpl entryService;

    @Test
    void givenValidData_whenCreateIsCalled_thenEntryIsSavedAndReturned() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        Entry entry = Entry.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        EntryDto entryDto = EntryDto.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        when(entryMapper.fromDtoToEntry(data)).thenReturn(entry);
        when(entryRepository.save(entry)).thenReturn(entry);
        when(entryMapper.fromEntityToDto(entry)).thenReturn(entryDto);

        // WHEN
        PromptResponse result = entryService.create(data);

        // THEN
        verify(entryValidator).isEntryNonExistent(data);
        verify(entryRepository).save(entry);
        assertEquals(Operation.CREATE, result.getOperation());
        assertEquals(entryDto, result.getEntry());
    }

    @Test
    void givenValidData_whenFindIsCalled_thenEntryIsReturned() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE
        );

        Entry entry = Entry.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        EntryDto entryDto = EntryDto.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        when(entryRepository.findByNameOrNumber(NAME_VALUE, null)).thenReturn(entry);
        when(entryMapper.fromEntityToDto(entry)).thenReturn(entryDto);

        // WHEN
        PromptResponse result = entryService.find(data);

        // THEN
        verify(entryValidator).doesEntryExistsInRepository(data);
        verify(entryValidator).isEntryUnique(data);
        verify(entryRepository).findByNameOrNumber(NAME_VALUE, null);
        assertEquals(Operation.READ, result.getOperation());
        assertEquals(entryDto, result.getEntry());
    }

    @Test
    void givenValidData_whenUpdateIsCalled_thenEntryIsUpdated() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE,
                NUMBER_KEY, NUMBER_VALUE
        );

        Entry existingEntry = Entry.builder()
                .name("OldName")
                .number("11111111")
                .build();

        EntryDto entryDto = EntryDto.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        when(entryRepository.findByNameOrNumber(NAME_VALUE, NUMBER_VALUE))
                .thenReturn(existingEntry);
        when(entryRepository.save(existingEntry))
                .thenReturn(existingEntry);
        when(entryMapper.fromEntityToDto(existingEntry))
                .thenReturn(entryDto);

        // WHEN
        PromptResponse result = entryService.update(data);

        // THEN
        verify(entryValidator).isOneOfTheDataValuesPresent(data);
        verify(entryValidator).doesEntryExistsInRepository(data);
        verify(entryValidator).isEntryUnique(data);
        verify(entryRepository).save(existingEntry);

        assertEquals(NAME_VALUE, existingEntry.getName());
        assertEquals(NUMBER_VALUE, existingEntry.getNumber());
        assertEquals(Operation.UPDATE, result.getOperation());
    }

    @Test
    void givenValidData_whenDeleteIsCalled_thenEntryIsDeleted() {
        // GIVEN
        Map<String, String> data = Map.of(
                NAME_KEY, NAME_VALUE
        );

        Entry entry = Entry.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        EntryDto entryDto = EntryDto.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        when(entryRepository.findByNameOrNumber(NAME_VALUE, NUMBER_KEY))
                .thenReturn(entry);
        when(entryMapper.fromEntityToDto(entry))
                .thenReturn(entryDto);

        // WHEN
        PromptResponse result = entryService.delete(data);

        // THEN
        verify(entryValidator).doesEntryExistsInRepository(data);
        verify(entryValidator).isEntryUnique(data);
        verify(entryRepository).delete(entry);
        assertEquals(Operation.DELETE, result.getOperation());
        assertEquals(entryDto, result.getEntry());
    }

    @Test
    void givenEntriesInRepository_whenFindAllIsCalled_thenAllEntriesAreReturned() {
        // GIVEN
        Entry entry = Entry.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        EntryDto entryDto = EntryDto.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        when(entryRepository.findAll()).thenReturn(List.of(entry));
        when(entryMapper.fromEntityToDto(entry)).thenReturn(entryDto);

        // WHEN
        List<EntryDto> result = entryService.findAll();

        // THEN
        assertEquals(1, result.size());
        assertEquals(entryDto, result.get(0));
        verify(entryRepository).findAll();
    }
}