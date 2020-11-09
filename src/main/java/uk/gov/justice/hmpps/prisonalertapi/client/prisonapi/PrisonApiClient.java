package uk.gov.justice.hmpps.prisonalertapi.client.prisonapi;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import uk.gov.justice.hmpps.prisonalertapi.client.prisonapi.dto.Alert;

@Service
public class PrisonApiClient {
    private final WebClient webClient;

    public PrisonApiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Alert getAlert(final long bookingId, final long alertId) {
        return webClient.get()
                .uri("http://localhost:8082/api/bookings/-1/alerts/1")
                .retrieve()
                .bodyToMono(Alert.class)
                .block();
//        return new Alert(alertId);
    }
}
