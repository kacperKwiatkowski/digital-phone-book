package com.kacperkwiatkowski.digitalphonebook;

import com.kacperkwiatkowski.digitalphonebook.repository.EntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class EntryApiTest {

    public static final String ENTRY_SERVICE_BASE_MAPPING = "/api/v.1.0";
    public static final String ENTRY_CONTROLLER_MAPPING = "/entry";
    public static final String ENTRY_CONTROLLER_ALL_ENTRIES_MAPPING = "/all";

    @LocalServerPort
    private int port;

    protected WebTestClient webTestClient;

    @Autowired
    protected EntryRepository entryRepository;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }
}
