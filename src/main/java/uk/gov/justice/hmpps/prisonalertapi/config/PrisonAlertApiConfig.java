package uk.gov.justice.hmpps.prisonalertapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrisonAlertApiConfig {
    private final String prisonApiBaseUrl;

    public PrisonAlertApiConfig(@Value("${prison.api.base.url}") final String prisonApiBaseUrl) {
        this.prisonApiBaseUrl = prisonApiBaseUrl;
    }

    public String getPrisonApiBaseUrl(){
        return prisonApiBaseUrl;
    }
}
