package com.kacperkwiatkowski.digitalphonebook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;
import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenAiServiceImplTest {

    private static final String PROMPT =
            "Create entry for Joanna with number 22222222";

    private static final String VALID_JSON_RESPONSE = """
            {
              "operation": "CREATE",
              "entity": "Entry",
              "data": {
                "name": "Joanna",
                "number": "22222222"
              }
            }
            """;

    private static final String INVALID_JSON_RESPONSE = "not a json";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.ChatClientRequestSpec requestSpec;

    @Mock
    private ChatClient.ChatClientRequestSpec userSpec;

    @Mock
    private ChatClient.CallResponseSpec callResponseSpec;

    private OpenAiServiceImpl openAiService;

    @BeforeEach
    void setUp() {
        // GIVEN
        when(chatClientBuilder.build()).thenReturn(chatClient);
        openAiService = new OpenAiServiceImpl(chatClientBuilder, OBJECT_MAPPER);
    }

    @Test
    void givenValidPrompt_whenInterpretIsCalled_thenStructuredCommandIsReturned() {
        // GIVEN
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(PROMPT)).thenReturn(userSpec);
        when(userSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn(VALID_JSON_RESPONSE);

        // WHEN
        StructuredCommand result = openAiService.interpret(PROMPT);

        // THEN
        assertNotNull(result);
        assertEquals(Operation.CREATE, result.getOperation());
        assertEquals("Entry", result.getEntity());
        assertEquals("Joanna", result.getData().get("name"));
        assertEquals("22222222", result.getData().get("number"));

        verify(chatClientBuilder).build();
        verify(chatClient).prompt();
        verify(requestSpec).system(anyString());
        verify(requestSpec).user(PROMPT);
        verify(userSpec).call();
    }

    @Test
    void givenInvalidJsonResponse_whenInterpretIsCalled_thenInvalidPromptExceptionIsThrown() {
        // GIVEN
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(PROMPT)).thenReturn(userSpec);
        when(userSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn(INVALID_JSON_RESPONSE);

        // WHEN / THEN
        assertThrows(
                InvalidPromptException.class,
                () -> openAiService.interpret(PROMPT)
        );

        verify(userSpec).call();
    }
}
