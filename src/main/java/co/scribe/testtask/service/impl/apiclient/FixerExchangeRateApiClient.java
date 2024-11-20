package co.scribe.testtask.service.impl.apiclient;

import co.scribe.testtask.model.ExchangeRateResponse;
import co.scribe.testtask.service.ExchangeRateApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Profile("fixer")
public class FixerExchangeRateApiClient implements ExchangeRateApiClient {

    private final RestTemplate restTemplate;

    @Value("${exchange.api-url}")
    private String apiUrl;

    @Value("${exchange.access-key}")
    private String accessKey;

    public FixerExchangeRateApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ExchangeRateResponse fetchExchangeRates(String baseCurrency) {
        String url = apiUrl + "?access_key=" + accessKey + "&base=" + baseCurrency;
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
        return response;
    }
}
