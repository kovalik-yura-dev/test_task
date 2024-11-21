package co.scribe.testtask.service;

import co.scribe.testtask.model.Currency;
import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.model.ExchangeRateResponse;
import co.scribe.testtask.service.impl.CompositeExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateRefreshServiceTest {

    @Mock
    private ExchangeRateApiClient exchangeRateApiClient;

    @Mock
    private CompositeExchangeRateService exchangeRateService;

    @InjectMocks
    public ExchangeRateRefreshService exchangeRateRefreshService;


    @Test
    public void testUpdateExchangeRates() {
        Currency currency = new Currency("USD");

        ExchangeRateResponse response = new ExchangeRateResponse();
        when(exchangeRateApiClient.fetchExchangeRates("USD")).thenReturn(response);

        ExchangeRate exchangeRate1 = new ExchangeRate();
        exchangeRate1.setBaseCurrency("USD");
        exchangeRate1.setRates(Map.of("EUR", new BigDecimal("0.85")));
        exchangeRate1.setLastUpdated(LocalDateTime.now());

        exchangeRateRefreshService.refresh(currency);

        verify(exchangeRateApiClient, times(1)).fetchExchangeRates("USD");
        verify(exchangeRateService, times(1)).saveExchangeRate(any(ExchangeRate.class));
    }

    @Test
    public void testUpdateExchangeRates_ApiClientThrowsException() {

        Currency currency = new Currency("USD");

        when(exchangeRateApiClient.fetchExchangeRates("USD")).thenThrow(new RuntimeException("API error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> exchangeRateRefreshService.refresh(currency));
        assertEquals("API error", exception.getMessage());

        verify(exchangeRateApiClient, times(1)).fetchExchangeRates("USD");
        verify(exchangeRateService, never()).saveExchangeRate(any());
    }
}
