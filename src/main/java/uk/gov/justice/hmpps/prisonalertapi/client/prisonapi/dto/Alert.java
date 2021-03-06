package uk.gov.justice.hmpps.prisonalertapi.client.prisonapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Alert {
    private Long alertId;
    private String alertType;

    public Alert(Long alertId, String alertType) {
        this.alertId = alertId;
        this.alertType = alertType;
    }

    public Alert(){}

    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return Objects.equal(alertId, alert.alertId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(alertId);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("alertId", alertId)
                .add("alertType", alertType)
                .toString();
    }
}
