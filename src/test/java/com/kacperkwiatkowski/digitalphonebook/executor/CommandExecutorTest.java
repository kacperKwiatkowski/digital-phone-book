package com.kacperkwiatkowski.digitalphonebook.executor;

import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;
import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import com.kacperkwiatkowski.digitalphonebook.service.EntryServiceImpl;
import com.kacperkwiatkowski.digitalphonebook.validator.PromptValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandExecutorTests {

    @Mock
    private EntryServiceImpl entryServiceImpl;

    @Mock
    private PromptValidator promptValidator;

    @InjectMocks
    private CommandExecutor commandExecutor;

    @Test
    void givenValidCreateEntryCommand_whenExecuteIsCalled_thenEntryIsCreated() {
        // GIVEN
        Map<String, String> data = Map.of("name", "Joanna", "number", "22222222");
        StructuredCommand command = StructuredCommand.builder()
                .entity("Entry")
                .operation(Operation.CREATE)
                .data(data)
                .build();

        when(entryServiceImpl.create(data)).thenReturn(new PromptResponse());

        // WHEN
        PromptResponse result = commandExecutor.execute(command);

        // THEN
        verify(promptValidator).isDataAvailable(command);
        verify(promptValidator).areKeysValid(data);
        verify(promptValidator).isAnyOfTheDataValuesPresent(data);
        verify(entryServiceImpl).create(data);
    }

    @Test
    void givenValidReadEntryCommand_whenExecuteIsCalled_thenEntryIsRead() {
        // GIVEN
        Map<String, String> data = Map.of("name", "Joanna");
        StructuredCommand command = StructuredCommand.builder()
                .entity("Entry")
                .operation(Operation.READ)
                .data(data)
                .build();

        when(entryServiceImpl.find(data)).thenReturn(new PromptResponse());

        // WHEN
        PromptResponse result = commandExecutor.execute(command);

        // THEN
        verify(entryServiceImpl).find(data);
    }

    @Test
    void givenValidUpdateEntryCommand_whenExecuteIsCalled_thenEntryIsUpdated() {
        // GIVEN
        Map<String, String> data = Map.of("name", "Joanna", "number", "33333333");
        StructuredCommand command = StructuredCommand.builder()
                .entity("Entry")
                .operation(Operation.UPDATE)
                .data(data)
                .build();

        when(entryServiceImpl.update(data)).thenReturn(new PromptResponse());

        // WHEN
        PromptResponse result = commandExecutor.execute(command);

        // THEN
        verify(entryServiceImpl).update(data);
    }

    @Test
    void givenValidDeleteEntryCommand_whenExecuteIsCalled_thenEntryIsDeleted() {
        // GIVEN
        Map<String, String> data = Map.of("name", "Joanna");
        StructuredCommand command = StructuredCommand.builder()
                .entity("Entry")
                .operation(Operation.DELETE)
                .data(data)
                .build();

        when(entryServiceImpl.delete(data)).thenReturn(new PromptResponse());

        // WHEN
        PromptResponse result = commandExecutor.execute(command);

        // THEN
        verify(entryServiceImpl).delete(data);
    }

    @Test
    void givenErrorOperation_whenExecuteIsCalled_thenIllegalArgumentExceptionIsThrown() {
        // GIVEN
        StructuredCommand command = StructuredCommand.builder()
                .entity("Entry")
                .operation(Operation.ERROR)
                .data(Map.of())
                .build();

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> commandExecutor.execute(command));

        verifyNoInteractions(entryServiceImpl);
    }

    @Test
    void givenUnsupportedEntity_whenExecuteIsCalled_thenInvalidPromptExceptionIsThrown() {
        // GIVEN
        StructuredCommand command = StructuredCommand.builder()
                .entity("Unknown")
                .operation(Operation.CREATE)
                .data(Map.of("key", "value"))
                .build();

        // WHEN / THEN
        assertThrows(InvalidPromptException.class, () -> commandExecutor.execute(command));

        verifyNoInteractions(entryServiceImpl);
    }

    @Test
    void givenValidatorFails_whenExecuteIsCalled_thenServiceIsNotInvoked() {
        // GIVEN
        StructuredCommand command = StructuredCommand.builder()
                .entity("Entry")
                .operation(Operation.CREATE)
                .data(Map.of())
                .build();

        doThrow(new InvalidPromptException("No data"))
                .when(promptValidator).isDataAvailable(command);

        // WHEN / THEN
        assertThrows(InvalidPromptException.class, () -> commandExecutor.execute(command));

        verifyNoInteractions(entryServiceImpl);
    }
}
