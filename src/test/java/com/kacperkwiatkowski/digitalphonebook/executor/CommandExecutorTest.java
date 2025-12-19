package com.kacperkwiatkowski.digitalphonebook.executor;

import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;
import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import com.kacperkwiatkowski.digitalphonebook.service.RecordServiceImpl;
import com.kacperkwiatkowski.digitalphonebook.validator.PromptValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandExecutorTest {

    @Mock
    private RecordServiceImpl recordServiceImpl;

    @Mock
    private PromptValidator promptValidator;

    @InjectMocks
    private CommandExecutor commandExecutor;

    @Test
    void givenValidCreateRecordCommand_whenExecuteIsCalled_thenRecordIsCreated() {
        // GIVEN
        Map<String, String> data = Map.of("name", "Joanna", "number", "22222222");
        StructuredCommand command = StructuredCommand.builder()
                .entity("Record")
                .operation(Operation.CREATE)
                .data(data)
                .build();

        when(recordServiceImpl.create(data)).thenReturn(new PromptResponse());

        // WHEN
        commandExecutor.execute(command);

        // THEN
        verify(promptValidator).isDataAvailable(command);
        verify(promptValidator).areKeysValid(data);
        verify(promptValidator).isAnyOfTheDataValuesPresent(data);
        verify(recordServiceImpl).create(data);
    }

    @Test
    void givenValidReadRecordCommand_whenExecuteIsCalled_thenRecordIsRead() {
        // GIVEN
        Map<String, String> data = Map.of("name", "Joanna");
        StructuredCommand command = StructuredCommand.builder()
                .entity("Record")
                .operation(Operation.READ)
                .data(data)
                .build();

        when(recordServiceImpl.find(data)).thenReturn(new PromptResponse());

        // WHEN
        commandExecutor.execute(command);

        // THEN
        verify(recordServiceImpl).find(data);
    }

    @Test
    void givenValidUpdateRecordCommand_whenExecuteIsCalled_thenRecordIsUpdated() {
        // GIVEN
        Map<String, String> data = Map.of("name", "Joanna", "number", "33333333");
        StructuredCommand command = StructuredCommand.builder()
                .entity("Record")
                .operation(Operation.UPDATE)
                .data(data)
                .build();

        when(recordServiceImpl.update(data)).thenReturn(new PromptResponse());

        // WHEN
        commandExecutor.execute(command);

        // THEN
        verify(recordServiceImpl).update(data);
    }

    @Test
    void givenValidDeleteRecordCommand_whenExecuteIsCalled_thenRecordIsDeleted() {
        // GIVEN
        Map<String, String> data = Map.of("name", "Joanna");
        StructuredCommand command = StructuredCommand.builder()
                .entity("Record")
                .operation(Operation.DELETE)
                .data(data)
                .build();

        when(recordServiceImpl.delete(data)).thenReturn(new PromptResponse());

        // WHEN
        commandExecutor.execute(command);

        // THEN
        verify(recordServiceImpl).delete(data);
    }

    @Test
    void givenErrorOperation_whenExecuteIsCalled_thenIllegalArgumentExceptionIsThrown() {
        // GIVEN
        StructuredCommand command = StructuredCommand.builder()
                .entity("Record")
                .operation(Operation.ERROR)
                .data(Map.of())
                .build();

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> commandExecutor.execute(command));

        verifyNoInteractions(recordServiceImpl);
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

        verifyNoInteractions(recordServiceImpl);
    }

    @Test
    void givenValidatorFails_whenExecuteIsCalled_thenServiceIsNotInvoked() {
        // GIVEN
        StructuredCommand command = StructuredCommand.builder()
                .entity("Record")
                .operation(Operation.CREATE)
                .data(Map.of())
                .build();

        doThrow(new InvalidPromptException("No data"))
                .when(promptValidator).isDataAvailable(command);

        // WHEN / THEN
        assertThrows(InvalidPromptException.class, () -> commandExecutor.execute(command));

        verifyNoInteractions(recordServiceImpl);
    }
}
