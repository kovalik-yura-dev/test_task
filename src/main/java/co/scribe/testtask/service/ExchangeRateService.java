package co.scribe.testtask.service;

import co.scribe.testtask.model.ExchangeRate;


public interface ExchangeRateService {
    ExchangeRate getLatestExchangeRate(String baseCurrency);
    void saveExchangeRate(ExchangeRate exchangeRate);
}