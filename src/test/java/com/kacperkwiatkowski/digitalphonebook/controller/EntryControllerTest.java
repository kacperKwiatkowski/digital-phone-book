package com.kacperkwiatkowski.digitalphonebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kacperkwiatkowski.digitalphonebook.dto.EntryDto;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptRequest;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import com.kacperkwiatkowski.digitalphonebook.executor.CommandExecutor;
import com.kacperkwiatkowski.digitalphonebook.service.EntryService;
import com.kacperkwiatkowski.digitalphonebook.service.OpenAiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EntryController.class)
class EntryControllerTest {


    private static final String CONTROLLER_MAPPING = "/entry";
    private static final String GET_ALL_MAPPING = "/all";

    private static final String PROMPT = "Create entry for Joanna";
    private static final String NAME_VALUE = "Joanna";
    private static final String NUMBER_VALUE = "22222222";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OpenAiService openAiService;

    @MockitoBean
    private EntryService entryService;

    @MockitoBean
    private CommandExecutor commandExecutor;

    @Test
    void shouldAcceptPromptAndReturnOk() throws Exception {
        // GIVEN
        PromptRequest request = new PromptRequest();
        request.setPrompt(PROMPT);

        StructuredCommand command = StructuredCommand.builder().build();
        PromptResponse response = PromptResponse.builder().build();

        when(openAiService.interpret(PROMPT)).thenReturn(command);
        when(commandExecutor.execute(command)).thenReturn(response);

        // WHEN / THEN
        mockMvc.perform(post(CONTROLLER_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(openAiService).interpret(PROMPT);
        verify(commandExecutor).execute(command);
    }

    @Test
    void shouldReturnInternalServerError_whenOpenAiServiceThrowsException() throws Exception {
        // GIVEN
        PromptRequest request = new PromptRequest();
        request.setPrompt(PROMPT);

        when(openAiService.interpret(PROMPT))
                .thenThrow(new InvalidPromptException("Invalid prompt"));

        // WHEN / THEN
        mockMvc.perform(post(CONTROLLER_MAPPING)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnAllEntriesSuccessfully() throws Exception {
        // GIVEN
        EntryDto entryDto = EntryDto.builder()
                .name(NAME_VALUE)
                .number(NUMBER_VALUE)
                .build();

        when(entryService.findAll()).thenReturn(List.of(entryDto));

        // WHEN / THEN
        mockMvc.perform(get(CONTROLLER_MAPPING + GET_ALL_MAPPING))
                .andExpect(status().isOk());

        verify(entryService).findAll();
    }

    @Test
    void shouldReturnEmptyListSuccessfully_whenNoEntriesExist() throws Exception {
        // GIVEN
        when(entryService.findAll()).thenReturn(List.of());

        // WHEN / THEN
        mockMvc.perform(get(CONTROLLER_MAPPING + GET_ALL_MAPPING))
                .andExpect(status().isOk());

        verify(entryService).findAll();
    }
}