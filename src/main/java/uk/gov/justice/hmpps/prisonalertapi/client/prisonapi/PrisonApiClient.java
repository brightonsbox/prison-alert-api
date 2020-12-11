package uk.gov.justice.hmpps.prisonalertapi.client.prisonapi;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import uk.gov.justice.hmpps.prisonalertapi.client.prisonapi.dto.Alert;
import uk.gov.justice.hmpps.prisonalertapi.config.PrisonAlertApiConfig;

@Service
public class PrisonApiClient {
    private final WebClient webClient;
    private final PrisonAlertApiConfig prisonAlertApiConfig;

    public PrisonApiClient(WebClient webClient, PrisonAlertApiConfig prisonAlertApiConfig) {
        this.webClient = webClient;
        this.prisonAlertApiConfig = prisonAlertApiConfig;
    }

    public Alert getAlert(final long bookingId, final long alertId) {
        return webClient.get()
                .uri(String.format("%s/api/bookings/%s/alerts/%s", prisonAlertApiConfig.getPrisonApiBaseUrl(), bookingId, alertId))
                .retrieve()
                .bodyToMono(Alert.class)
                .block();
    }
}
