package uk.gov.justice.hmpps.prisonalertapi.client.prisonapi;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import uk.gov.justice.hmpps.prisonalertapi.client.prisonapi.dto.Alert;
import uk.gov.justice.hmpps.prisonalertapi.config.PrisonAlertApiConfig;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PrisonApiClientTest {
    private final MockWebServer prisonApiMock = new MockWebServer();
    private PrisonApiClient prisonApiClient;


    @BeforeEach
    void beforeEach() {
        prisonApiClient = new PrisonApiClient(WebClient.create(), new PrisonAlertApiConfig(String.format("http://localhost:%s", prisonApiMock.getPort())));
    }

    @AfterEach
    void tearDown() throws IOException {
        prisonApiMock.shutdown();
    }

    @Test
    void getAlert() {
        prisonApiMock.enqueue(new MockResponse().setHeader("Content-type", "application/json").setBody("{\"alertId\": 3, \"alertType\": \"AA\"}"));
        assertThat(prisonApiClient.getAlert(1, 3)).isEqualTo(new Alert(3L, "AA"));
    }
}