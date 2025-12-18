package com.kacperkwiatkowski.digitalphonebook.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kacperkwiatkowski.digitalphonebook.dto.StructuredCommand;
import com.kacperkwiatkowski.digitalphonebook.exception.InvalidPromptException;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenAiServiceImpl implements OpenAiService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public OpenAiServiceImpl(ChatClient.Builder builder, ObjectMapper objectMapper) {
        chatClient = builder.build();
        this.objectMapper = objectMapper;
    }

    @Override
    public StructuredCommand interpret(String promptContent) {
        String response = chatClient.prompt()
                .system("""
                        You are an assistant that converts natural language commands
                        into JSON CRUD commands.
                        
                        Rules:
                        - Only return valid JSON
                        - Allowed operations: CREATE, READ, UPDATE, DELETE
                        - Allowed entities: Entry
                        - Never explain anything
                        - Data keys should always be named "name" and "number"
                        - If a name or number is missing, put null value as default
                        - Always return JSON in this format:
                                {
                                    "operation": "",
                                    "entity": "",
                                    "data": {
                                        "name": "",
                                        "number": ""
                                }
                            }

                        User command:
                        "{{promptContent}}"
                        """)
                .user(promptContent)
                .call()
                .content();

        try {
            return objectMapper.readValue(response, StructuredCommand.class);
        } catch (Exception e) {
            throw new InvalidPromptException("Invalid prompt");
        }
    }
}
