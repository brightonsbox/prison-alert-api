package uk.gov.justice.hmpps.prisonalertapi.client.prisonapi;

import org.junit.jupiter.api.Test;
import uk.gov.justice.hmpps.prisonalertapi.client.prisonapi.dto.Alert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PrisonApiClientTest {
    private final PrisonApiClient prisonApiClient = new PrisonApiClient(null);

    @Test
    void getAlert() {
        assertThat(prisonApiClient.getAlert(1, 3)).isEqualTo(new Alert(3L));
    }
}