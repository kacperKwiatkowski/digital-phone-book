package com.kacperkwiatkowski.digitalphonebook.advisor;

import com.kacperkwiatkowski.digitalphonebook.EntryApiTest;
import com.kacperkwiatkowski.digitalphonebook.dto.EntryDto;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptRequest;
import com.kacperkwiatkowski.digitalphonebook.dto.PromptResponse;
import com.kacperkwiatkowski.digitalphonebook.enums.Operation;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ControllerAdvisorTest extends EntryApiTest {

    private static final String WRONG_DATA = "Invalid data";
    private static final String INCOMPLETE_DATA = "Create entry with name and number";

    @Test
    @Order(1)
    void givenPromptIsInvalid_whenPromptInvokedThroughController_thenControllerAdvisorReturnsProperError() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt(WRONG_DATA).build();

        // WHEN
        EntityExchangeResult<PromptResponse> response = webTestClient.post().uri(ENTRY_SERVICE_BASE_MAPPING + ENTRY_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(PromptResponse.class)
                .returnResult();

        // THEN
        assertNotNull(response.getResponseBody());
        assertEquals(Operation.ERROR, response.getResponseBody().getOperation());
    }

    @Test
    @Order(2)
    void givenPromptIsIncomplete_whenPromptInvokedThroughController_thenControllerAdvisorReturnsProperError() {
        // GIVEN
        PromptRequest promptRequest = PromptRequest.builder()
                .prompt(INCOMPLETE_DATA).build();

        // WHEN
        EntityExchangeResult<PromptResponse> response = webTestClient.post().uri(ENTRY_SERVICE_BASE_MAPPING + ENTRY_CONTROLLER_MAPPING)
                .bodyValue(promptRequest)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(PromptResponse.class)
                .returnResult();

        // THEN
        assertNotNull(response.getResponseBody());
        assertEquals(Operation.ERROR, response.getResponseBody().getOperation());
    }
}