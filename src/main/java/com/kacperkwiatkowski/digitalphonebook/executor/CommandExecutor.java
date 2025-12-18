package com.kacperkwiatkowski.digitalphonebook.executor;

import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import com.kacperkwiatkowski.digitalphonebook.service.RecordServiceImpl;
import com.kacperkwiatkowski.digitalphonebook.validator.PromptValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommandExecutor {

    private final RecordServiceImpl recordServiceImpl;
    private final PromptValidator promptValidator;

    public PromptResponse execute(StructuredCommand cmd) {

        promptValidator.isDataAvailable(cmd);
        promptValidator.areKeysValid(cmd.getData());
        promptValidator.isAnyOfTheDataValuesPresent(cmd.getData());

        return switch (cmd.getEntity()) {
            case "Record" -> executeUserCommand(cmd);
            default -> throw new InvalidPromptException("Unsupported command");
        };
    }

    private PromptResponse executeUserCommand(StructuredCommand cmd) {
        return switch (cmd.getOperation()) {
            case CREATE -> recordServiceImpl.create(cmd.getData());
            case READ -> recordServiceImpl.find(cmd.getData());
            case UPDATE -> recordServiceImpl.update(cmd.getData());
            case DELETE -> recordServiceImpl.delete(cmd.getData());
            default -> throw new IllegalArgumentException("Invalid operation");
        };
    }
}
