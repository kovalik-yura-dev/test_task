package co.scribe.testtask.service.impl;

import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.service.ExchangeRateService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("cacheExchangeRateService")
public class CacheExchangeRateServiceImpl implements ExchangeRateService {

    private final Map<String, ExchangeRate> cache = new ConcurrentHashMap<>();

    @Override
    public ExchangeRate getLatestExchangeRate(String baseCurrency) {
        return cache.get(baseCurrency);
    }

    @Override
    public void saveExchangeRate(ExchangeRate exchangeRate) {
        cache.put(exchangeRate.getBaseCurrency(), exchangeRate);
    }
}