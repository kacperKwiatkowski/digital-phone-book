package com.kacperkwiatkowski.digitalphonebook.controller;

import com.kacperkwiatkowski.digitalphonebook.EntryApiTest;
import com.kacperkwiatkowski.digitalphonebook.dto.EntryDto;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptRequest;
import org.junit.jupiter.api.*;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EntryControllerIntegrationTests extends EntryApiTest {

    private static final String TEST_ENTRY_NAME = "Joanna";
    private static final String TEST_ENTRY_NUMBER = "22222222";
    private static final String TEST_ENTRY_UPDATED_NAME = "John";
    private static final String TEST_ENTRY_UPDATED_NUMBER = "33333333";

    @Test
    @Order(1)
    void givenEntryCreationPromptIsCreated_whenPromptInvokedThroughController_thenEntryIsCreated() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt("Please add a record for " + TEST_ENTRY_NAME + " with the number " + TEST_ENTRY_NUMBER).build();

        // WHEN
        webTestClient.post().uri(ENTRY_SERVICE_BASE_MAPPING + ENTRY_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // THEN
        assertTrue(entryRepository.existsByNameAndNumber(TEST_ENTRY_NAME, TEST_ENTRY_NUMBER));
    }

    @Test
    @Order(2)
    void givenEntryReadPromptIsCreated_whenPromptInvokedThroughController_thenEntryIsFound() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt("Please find Joanna").build();

        // WHEN
        webTestClient.post().uri(ENTRY_SERVICE_BASE_MAPPING + ENTRY_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // THEN
        assertTrue(entryRepository.existsByNameAndNumber(TEST_ENTRY_NAME, TEST_ENTRY_NUMBER));
    }

    @Test
    @Order(3)
    void givenAllEntriesAreNeeded_whenEndpointIsInvokedThroughController_thenAllEntriesAreNeeded() {
        // GIVEN

        // WHEN
        EntityExchangeResult<List<EntryDto>> response = webTestClient.get().uri(ENTRY_SERVICE_BASE_MAPPING + ENTRY_CONTROLLER_MAPPING + ENTRY_CONTROLLER_ALL_ENTRIES_MAPPING)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EntryDto.class)
                .returnResult();

        // THEN
        Assertions.assertNotNull(response.getResponseBody());
        assertTrue(response.getResponseBody().stream().allMatch(entry -> entry.getName().equals(TEST_ENTRY_NAME) && entry.getNumber().equals(TEST_ENTRY_NUMBER)));
    }

    @Test
    @Order(4)
    void givenEntryNumberUpdatePromptIsCreated_whenPromptInvokedThroughController_thenEntryNumberIsUpdated() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt("Please change " + TEST_ENTRY_NAME + " number to " + TEST_ENTRY_UPDATED_NUMBER).build();

        // WHEN
        webTestClient.post().uri(ENTRY_SERVICE_BASE_MAPPING + ENTRY_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // THEN
        assertTrue(entryRepository.existsByNameAndNumber(TEST_ENTRY_NAME, TEST_ENTRY_UPDATED_NUMBER));
    }

    @Test
    @Order(5)
    void givenEntryNameUpdatePromptIsCreated_whenPromptInvokedThroughController_thenEntryNameIsUpdated() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt("Please change number's " + TEST_ENTRY_UPDATED_NUMBER + " owner to " + TEST_ENTRY_UPDATED_NAME).build();

        // WHEN
        webTestClient.post().uri(ENTRY_SERVICE_BASE_MAPPING + ENTRY_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // THEN
        assertTrue(entryRepository.existsByNameAndNumber(TEST_ENTRY_UPDATED_NAME, TEST_ENTRY_UPDATED_NUMBER));
    }

    @Test
    @Order(6)
    void givenEntryDeletePromptIsCreated_whenPromptInvokedThroughController_thenEntryIsDeleted() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt("Please remove " + TEST_ENTRY_UPDATED_NAME + " number.").build();

        // WHEN
        webTestClient.post().uri(ENTRY_SERVICE_BASE_MAPPING + ENTRY_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // THEN
        assertFalse(entryRepository.existsByNameAndNumber(TEST_ENTRY_UPDATED_NAME, TEST_ENTRY_UPDATED_NUMBER));
    }
}
