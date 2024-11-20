package co.scribe.testtask.service.impl;

import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.repository.ExchangeRateRepository;
import co.scribe.testtask.service.ExchangeRateService;
import org.springframework.stereotype.Service;

@Service("repositoryExchangeRateService")
public class RepositoryExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    public RepositoryExchangeRateServiceImpl(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public ExchangeRate getLatestExchangeRate(String baseCurrency) {
        return exchangeRateRepository.findFirstByBaseCurrencyOrderByLastUpdatedDesc(baseCurrency);
    }

    @Override
    public void saveExchangeRate(ExchangeRate exchangeRate) {
        exchangeRateRepository.save(exchangeRate);
    }
}