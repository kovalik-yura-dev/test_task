package co.scribe.testtask.scheduler;

import co.scribe.testtask.model.Currency;
import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.model.ExchangeRateResponse;
import co.scribe.testtask.repository.CurrencyRepository;
import co.scribe.testtask.service.ExchangeRateApiClient;
import co.scribe.testtask.service.impl.CompositeExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateSchedulerTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private ExchangeRateApiClient exchangeRateApiClient;

    @Mock
    private CompositeExchangeRateService exchangeRateService;

    @InjectMocks
    private ExchangeRateScheduler exchangeRateScheduler;

    @Test
    public void testUpdateExchangeRates() {
        // Arrange
        Currency currency1 = new Currency();
        currency1.setCode("USD");
        Currency currency2 = new Currency();
        currency2.setCode("EUR");

        List<Currency> currencies = List.of(currency1, currency2);
        when(currencyRepository.findAll()).thenReturn(currencies);

        ExchangeRateResponse response1 = new ExchangeRateResponse();

        ExchangeRateResponse response2 = new ExchangeRateResponse();


        when(exchangeRateApiClient.fetchExchangeRates("USD")).thenReturn(response1);
        when(exchangeRateApiClient.fetchExchangeRates("EUR")).thenReturn(response2);

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
        verify(exchangeRateApiClient, times(1)).fetchExchangeRates("USD");
        verify(exchangeRateApiClient, times(1)).fetchExchangeRates("EUR");
        verify(exchangeRateService, times(2)).saveExchangeRate(any(ExchangeRate.class));
    }

    @Test
    public void testUpdateExchangeRates_NoCurrencies() {

        when(currencyRepository.findAll()).thenReturn(List.of());


        exchangeRateScheduler.updateExchangeRates();


        verify(currencyRepository, times(1)).findAll();
        verify(exchangeRateApiClient, never()).fetchExchangeRates(anyString());
        verify(exchangeRateService, never()).saveExchangeRate(any());
    }

    @Test
    public void testUpdateExchangeRates_ApiClientThrowsException() {

        Currency currency = new Currency();
        currency.setCode("USD");
        when(currencyRepository.findAll()).thenReturn(List.of(currency));
        when(exchangeRateApiClient.fetchExchangeRates("USD")).thenThrow(new RuntimeException("API error"));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> exchangeRateScheduler.updateExchangeRates());
        assertEquals("API error", exception.getMessage());

        verify(currencyRepository, times(1)).findAll();
        verify(exchangeRateApiClient, times(1)).fetchExchangeRates("USD");
        verify(exchangeRateService, never()).saveExchangeRate(any());
    }
}