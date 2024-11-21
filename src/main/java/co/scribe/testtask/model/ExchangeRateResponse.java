package co.scribe.testtask.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse {

    @JsonProperty
    @JsonAlias({"base", "base_code"})
    private String base;

    @JsonProperty
    @JsonAlias({"date", "time_last_update_utc"})
    private String date;

    @JsonProperty
    @JsonAlias({"rates", "conversion_rates"})
    private Map<String, BigDecimal> rates;

}