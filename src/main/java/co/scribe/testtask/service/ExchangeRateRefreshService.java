package co.scribe.testtask.service;

import co.scribe.testtask.model.Currency;
import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.model.ExchangeRateResponse;
import co.scribe.testtask.util.ExchangeRateConverter;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateRefreshService {

    private final ExchangeRateService exchangeRateService;
    private final ExchangeRateApiClient exchangeRateApiClient;

    public ExchangeRateRefreshService(ExchangeRateService exchangeRateService, ExchangeRateApiClient exchangeRateApiClient) {
        this.exchangeRateService = exchangeRateService;
        this.exchangeRateApiClient = exchangeRateApiClient;
    }

    public void refresh(Currency currency){
        ExchangeRateResponse response = exchangeRateApiClient.fetchExchangeRates(currency.getCode());

        ExchangeRate exchangeRate = ExchangeRateConverter.convertToExchangeRate(response);
        exchangeRateService.saveExchangeRate(exchangeRate);
    }
}
