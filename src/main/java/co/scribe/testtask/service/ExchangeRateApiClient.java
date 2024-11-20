package co.scribe.testtask.service;

import co.scribe.testtask.model.ExchangeRateResponse;

public interface ExchangeRateApiClient {
    ExchangeRateResponse fetchExchangeRates(String baseCurrency);
}
