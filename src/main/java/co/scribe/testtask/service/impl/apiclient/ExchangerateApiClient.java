package co.scribe.testtask.service.impl.apiclient;

import co.scribe.testtask.model.ExchangeRateResponse;
import co.scribe.testtask.service.ExchangeRateApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("exchangerateapi")
public class ExchangerateApiClient implements ExchangeRateApiClient {

    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final String apiKey;

    public ExchangerateApiClient(RestTemplate restTemplate,
                                 @Value("${exchangerate-api.api-url}") String apiUrl,
                                 @Value("${exchangerate-api.api-key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    @Override
    public ExchangeRateResponse fetchExchangeRates(String baseCurrency) {
        String url = String.format("%s/%s/latest/%s", apiUrl, apiKey, baseCurrency);
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
        return response;
    }
}