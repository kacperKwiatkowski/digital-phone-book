package com.kacperkwiatkowski.digitalphonebook.executor;

import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import com.kacperkwiatkowski.digitalphonebook.service.EntryServiceImpl;
import com.kacperkwiatkowski.digitalphonebook.validator.PromptValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommandExecutor {

    private final EntryServiceImpl entryServiceImpl;
    private final PromptValidator promptValidator;

    public PromptResponse execute(StructuredCommand cmd) {

        promptValidator.isDataAvailable(cmd);
        promptValidator.areKeysValid(cmd.getData());
        promptValidator.isAnyOfTheDataValuesPresent(cmd.getData());

        return switch (cmd.getEntity()) {
            case "Entry" -> executeUserCommand(cmd);
            default -> throw new InvalidPromptException("Unsupported command");
        };
    }

    private PromptResponse executeUserCommand(StructuredCommand cmd) {
        return switch (cmd.getOperation()) {
            case CREATE -> entryServiceImpl.create(cmd.getData());
            case READ -> entryServiceImpl.find(cmd.getData());
            case UPDATE -> entryServiceImpl.update(cmd.getData());
            case DELETE -> entryServiceImpl.delete(cmd.getData());
            default -> throw new IllegalArgumentException("Invalid operation");
        };
    }
}
