package co.scribe.testtask.service.impl;

import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CompositeExchangeRateService implements ExchangeRateService {

    private final ExchangeRateService cacheService;
    private final ExchangeRateService repositoryService;

    public CompositeExchangeRateService(
            @Qualifier("cacheExchangeRateService") ExchangeRateService cacheService,
            @Qualifier("repositoryExchangeRateService") ExchangeRateService repositoryService) {
        this.cacheService = cacheService;
        this.repositoryService = repositoryService;
    }

    @Override
    public ExchangeRate getLatestExchangeRate(String baseCurrency) {
        // First, try to get from the cache
        ExchangeRate exchangeRate = cacheService.getLatestExchangeRate(baseCurrency);
        if (exchangeRate != null) {
            return exchangeRate;
        }
        // If it is not in the cache, we get it from the database
        exchangeRate = repositoryService.getLatestExchangeRate(baseCurrency);
        if (exchangeRate != null) {
            cacheService.saveExchangeRate(exchangeRate);
        }
        return exchangeRate;
    }

    @Override
    public void saveExchangeRate(ExchangeRate exchangeRate) {

        repositoryService.saveExchangeRate(exchangeRate);
        cacheService.saveExchangeRate(exchangeRate);
    }
}
