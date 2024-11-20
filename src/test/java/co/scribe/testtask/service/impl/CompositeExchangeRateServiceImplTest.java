package co.scribe.testtask.service.impl;

import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.service.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompositeExchangeRateServiceImplTest {

    @Mock
    private ExchangeRateService cacheExchangeRateService;

    @Mock
    private ExchangeRateService repositoryExchangeRateService;

    private CompositeExchangeRateService compositeExchangeRateService;

    private ExchangeRate cachedExchangeRate;
    private ExchangeRate repositoryExchangeRate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        compositeExchangeRateService = new CompositeExchangeRateService(cacheExchangeRateService, repositoryExchangeRateService);

        cachedExchangeRate = new ExchangeRate();
        cachedExchangeRate.setBaseCurrency("USD");
        cachedExchangeRate.setRates(Map.of("EUR", new BigDecimal("0.85"), "JPY", new BigDecimal("110.0")));
        cachedExchangeRate.setLastUpdated(LocalDateTime.now());

        repositoryExchangeRate = new ExchangeRate();
        repositoryExchangeRate.setBaseCurrency("EUR");
        repositoryExchangeRate.setRates(Map.of("USD", new BigDecimal("1.18")));
        repositoryExchangeRate.setLastUpdated(LocalDateTime.now());
    }

    @Test
    public void testGetLatestExchangeRate_FromCache() {
        when(cacheExchangeRateService.getLatestExchangeRate("USD")).thenReturn(cachedExchangeRate);

        ExchangeRate result = compositeExchangeRateService.getLatestExchangeRate("USD");

        assertNotNull(result);
        assertEquals("USD", result.getBaseCurrency());
        assertEquals(cachedExchangeRate.getRates(), result.getRates());

        verify(cacheExchangeRateService, times(1)).getLatestExchangeRate("USD");
        verify(repositoryExchangeRateService, never()).getLatestExchangeRate(anyString());
        verify(cacheExchangeRateService, never()).saveExchangeRate(any(ExchangeRate.class));
    }

    @Test
    public void testGetLatestExchangeRate_FromRepository() {
        when(cacheExchangeRateService.getLatestExchangeRate("EUR")).thenReturn(null);
        when(repositoryExchangeRateService.getLatestExchangeRate("EUR")).thenReturn(repositoryExchangeRate);

        ExchangeRate result = compositeExchangeRateService.getLatestExchangeRate("EUR");

        assertNotNull(result);
        assertEquals("EUR", result.getBaseCurrency());
        assertEquals(repositoryExchangeRate.getRates(), result.getRates());

        verify(cacheExchangeRateService, times(1)).getLatestExchangeRate("EUR");
        verify(repositoryExchangeRateService, times(1)).getLatestExchangeRate("EUR");
        verify(cacheExchangeRateService, times(1)).saveExchangeRate(repositoryExchangeRate);
    }

    @Test
    public void testGetLatestExchangeRate_NotFound() {
        when(cacheExchangeRateService.getLatestExchangeRate("GBP")).thenReturn(null);
        when(repositoryExchangeRateService.getLatestExchangeRate("GBP")).thenReturn(null);

        ExchangeRate result = compositeExchangeRateService.getLatestExchangeRate("GBP");

        assertNull(result);

        verify(cacheExchangeRateService, times(1)).getLatestExchangeRate("GBP");
        verify(repositoryExchangeRateService, times(1)).getLatestExchangeRate("GBP");
        verify(cacheExchangeRateService, never()).saveExchangeRate(any(ExchangeRate.class));
    }

    @Test
    public void testSaveExchangeRate() {
        ExchangeRate newExchangeRate = new ExchangeRate();
        newExchangeRate.setBaseCurrency("JPY");
        newExchangeRate.setRates(Map.of("USD", new BigDecimal("0.0091")));
        newExchangeRate.setLastUpdated(LocalDateTime.now());

        compositeExchangeRateService.saveExchangeRate(newExchangeRate);

        verify(repositoryExchangeRateService, times(1)).saveExchangeRate(newExchangeRate);
        verify(cacheExchangeRateService, times(1)).saveExchangeRate(newExchangeRate);
    }
}