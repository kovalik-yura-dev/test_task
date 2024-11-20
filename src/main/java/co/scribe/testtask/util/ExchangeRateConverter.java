package co.scribe.testtask.util;

import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.model.ExchangeRateResponse;

import java.time.LocalDateTime;

public final class ExchangeRateConverter {
    public static ExchangeRate convertToExchangeRate(ExchangeRateResponse response) {


        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency(response.getBase());
        exchangeRate.setLastUpdated(LocalDateTime.now());
        exchangeRate.setRates(response.getRates());

        return exchangeRate;
    }
}
