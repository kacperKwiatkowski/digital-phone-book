package com.kacperkwiatkowski.digitalphonebook.controller;

import com.kacperkwiatkowski.digitalphonebook.RecordApiTest;
import com.kacperkwiatkowski.digitalphonebook.dto.RecordDto;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptRequest;
import org.junit.jupiter.api.*;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RecordControllerIntegrationTests extends RecordApiTest {

    private static final String TEST_RECORD_NAME = "Joanna";
    private static final String TEST_RECORD_NUMBER = "22222222";
    private static final String TEST_RECORD_UPDATED_NAME = "John";
    private static final String TEST_RECORD_UPDATED_NUMBER = "33333333";

    @Test
    @Order(1)
    void givenRecordCreationPromptIsCreated_whenPromptInvokedThroughController_thenRecordIsCreated() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt("Please add a record for " + TEST_RECORD_NAME + " with the number " + TEST_RECORD_NUMBER).build();

        // WHEN
        webTestClient.post().uri(RECORD_SERVICE_BASE_MAPPING + RECORD_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // THEN
        assertTrue(recordRepository.existsByNameAndNumber(TEST_RECORD_NAME, TEST_RECORD_NUMBER));
    }

    @Test
    @Order(2)
    void givenRecordReadPromptIsCreated_whenPromptInvokedThroughController_thenRecordIsFound() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt("Please find Joanna").build();

        // WHEN
        webTestClient.post().uri(RECORD_SERVICE_BASE_MAPPING + RECORD_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // THEN
        assertTrue(recordRepository.existsByNameAndNumber(TEST_RECORD_NAME, TEST_RECORD_NUMBER));
    }

    @Test
    @Order(3)
    void givenAllEntriesAreNeeded_whenEndpointIsInvokedThroughController_thenAllEntriesAreNeeded() {
        // GIVEN

        // WHEN
        EntityExchangeResult<List<RecordDto>> response = webTestClient.get().uri(RECORD_SERVICE_BASE_MAPPING + RECORD_CONTROLLER_MAPPING + RECORD_CONTROLLER_ALL_ENTRIES_MAPPING)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RecordDto.class)
                .returnResult();

        // THEN
        Assertions.assertNotNull(response.getResponseBody());
        assertTrue(response.getResponseBody().stream().allMatch(record -> record.getName().equals(TEST_RECORD_NAME) && record.getNumber().equals(TEST_RECORD_NUMBER)));
    }

    @Test
    @Order(4)
    void givenRecordNumberUpdatePromptIsCreated_whenPromptInvokedThroughController_thenRecordNumberIsUpdated() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt("Please change " + TEST_RECORD_NAME + " number to " + TEST_RECORD_UPDATED_NUMBER).build();

        // WHEN
        webTestClient.post().uri(RECORD_SERVICE_BASE_MAPPING + RECORD_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // THEN
        assertTrue(recordRepository.existsByNameAndNumber(TEST_RECORD_NAME, TEST_RECORD_UPDATED_NUMBER));
    }

    @Test
    @Order(5)
    void givenRecordNameUpdatePromptIsCreated_whenPromptInvokedThroughController_thenRecordNameIsUpdated() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt("Please change number's " + TEST_RECORD_UPDATED_NUMBER + " owner to " + TEST_RECORD_UPDATED_NAME).build();

        // WHEN
        webTestClient.post().uri(RECORD_SERVICE_BASE_MAPPING + RECORD_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // THEN
        assertTrue(recordRepository.existsByNameAndNumber(TEST_RECORD_UPDATED_NAME, TEST_RECORD_UPDATED_NUMBER));
    }

    @Test
    @Order(6)
    void givenRecordDeletePromptIsCreated_whenPromptInvokedThroughController_thenRecordIsDeleted() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt("Please remove " + TEST_RECORD_UPDATED_NAME + " number.").build();

        // WHEN
        webTestClient.post().uri(RECORD_SERVICE_BASE_MAPPING + RECORD_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isOk();

        // THEN
        assertFalse(recordRepository.existsByNameAndNumber(TEST_RECORD_UPDATED_NAME, TEST_RECORD_UPDATED_NUMBER));
    }
}
