package com.kacperkwiatkowski.digitalphonebook.controller;

import com.kacperkwiatkowski.digitalphonebook.dto.RecordDto;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptRequest;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;
import com.kacperkwiatkowski.digitalphonebook.executor.CommandExecutor;
import com.kacperkwiatkowski.digitalphonebook.service.RecordService;
import com.kacperkwiatkowski.digitalphonebook.service.OpenAiService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/record")
public class RecordController {

    private final OpenAiService openAiService;
    private final RecordService recordService;
    private final CommandExecutor commandExecutor;

    @PostMapping
    public ResponseEntity<PromptResponse> interpretCommand(@RequestBody PromptRequest request) {
        StructuredCommand command = openAiService.interpret(request.getPrompt());
        return ResponseEntity.ok(commandExecutor.execute(command));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RecordDto>> getAllRecords() {
        return ResponseEntity.ok(recordService.findAll());
    }
}
