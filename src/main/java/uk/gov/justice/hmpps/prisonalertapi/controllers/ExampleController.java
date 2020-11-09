package uk.gov.justice.hmpps.prisonalertapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.hmpps.prisonalertapi.client.prisonapi.PrisonApiClient;

@RestController
public class ExampleController {
    private final PrisonApiClient prisonApiClient;

    public ExampleController(PrisonApiClient prisonApiClient) {
        this.prisonApiClient = prisonApiClient;
    }

    @GetMapping(value = "/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping(value = "/alert")
    public String alert() {
        return prisonApiClient.getAlert(1, 4).toString();
    }
}
