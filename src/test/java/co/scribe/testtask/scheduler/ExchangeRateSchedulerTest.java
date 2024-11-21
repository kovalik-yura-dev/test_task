package co.scribe.testtask.scheduler;

import co.scribe.testtask.model.Currency;
import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.repository.CurrencyRepository;
import co.scribe.testtask.service.ExchangeRateRefreshService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateSchedulerTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateRefreshService exchangeRateRefreshService;

    @InjectMocks
    private ExchangeRateScheduler exchangeRateScheduler;

    @Test
    public void testUpdateExchangeRates() {
        Currency currency1 = new Currency();
        currency1.setCode("USD");
        Currency currency2 = new Currency();
        currency2.setCode("EUR");

        List<Currency> currencies = List.of(currency1, currency2);
        when(currencyRepository.findAll()).thenReturn(currencies);


        ExchangeRate exchangeRate1 = new ExchangeRate();
        exchangeRate1.setBaseCurrency("USD");
        exchangeRate1.setRates(Map.of("EUR", new BigDecimal("0.85")));
        exchangeRate1.setLastUpdated(LocalDateTime.now());

        ExchangeRate exchangeRate2 = new ExchangeRate();
        exchangeRate2.setBaseCurrency("EUR");
        exchangeRate2.setRates(Map.of("USD", new BigDecimal("1.18")));
        exchangeRate2.setLastUpdated(LocalDateTime.now());

        exchangeRateScheduler.updateExchangeRates();

        verify(currencyRepository, times(1)).findAll();
        verify(exchangeRateRefreshService, times(2)).refresh(any());
    }

    @Test
    public void testUpdateExchangeRates_NoCurrencies() {

        when(currencyRepository.findAll()).thenReturn(List.of());

        exchangeRateScheduler.updateExchangeRates();

        verify(currencyRepository, times(1)).findAll();
        verify(exchangeRateRefreshService, never()).refresh(any());
    }

}