package co.scribe.testtask.scheduler;

import co.scribe.testtask.model.Currency;
import co.scribe.testtask.repository.CurrencyRepository;
import co.scribe.testtask.service.ExchangeRateRefreshService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeRateScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateScheduler.class);
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRefreshService exchangeRateRefreshService;


    public ExchangeRateScheduler(CurrencyRepository currencyRepository, ExchangeRateRefreshService exchangeRateRefreshService) {
        this.currencyRepository = currencyRepository;
        this.exchangeRateRefreshService = exchangeRateRefreshService;
    }

    @Scheduled(fixedRate = 3600000)
    public void updateExchangeRates() {
        logger.info("Starting exchange rates update");

        List<Currency> currencies = currencyRepository.findAll();
        for (Currency currency : currencies) {
            logger.debug("Updating exchange rate for currency: {}", currency.getCode());
            try (var ignored = MDC.putCloseable("currencyCode", currency.getCode())) {
                exchangeRateRefreshService.refresh(currency);
                logger.debug("Exchange rate for currency {} updated successfully", currency.getCode());
            }

        }

        logger.info("Exchange rates update completed successfully");
    }
}
