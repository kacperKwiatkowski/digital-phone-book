package com.kacperkwiatkowski.digitalphonebook.controller;

import com.kacperkwiatkowski.digitalphonebook.dto.EntryDto;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptRequest;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;
import com.kacperkwiatkowski.digitalphonebook.executor.CommandExecutor;
import com.kacperkwiatkowski.digitalphonebook.repository.EntryRepository;
import com.kacperkwiatkowski.digitalphonebook.service.EntryService;
import com.kacperkwiatkowski.digitalphonebook.service.OpenAiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/entry")
public class EntryController {

    private final OpenAiService openAiService;
    private final EntryService entryService;
    private final CommandExecutor commandExecutor;

    @PostMapping
    public ResponseEntity<PromptResponse> interpretCommand(@RequestBody PromptRequest request) {
        StructuredCommand command = openAiService.interpret(request.getPrompt());
        return ResponseEntity.ok(commandExecutor.execute(command));
    }

    @GetMapping("/all")
    public ResponseEntity<List<EntryDto>> getAllEntries() {
        return ResponseEntity.ok(entryService.findAll());
    }
}
