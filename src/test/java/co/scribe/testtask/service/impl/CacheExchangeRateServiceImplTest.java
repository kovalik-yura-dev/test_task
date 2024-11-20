package co.scribe.testtask.service.impl;

import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.service.ExchangeRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CacheExchangeRateServiceImplTest {

    private ExchangeRateService exchangeRateService;

    @BeforeEach
    public void setUp() {
        exchangeRateService = new CacheExchangeRateServiceImpl();
    }

    @Test
    public void testSaveAndGetExchangeRate() {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency("USD");
        exchangeRate.setRates(Map.of("EUR", new BigDecimal("0.85"), "JPY", new BigDecimal("110.0")));
        exchangeRate.setLastUpdated(LocalDateTime.now());

        exchangeRateService.saveExchangeRate(exchangeRate);

        ExchangeRate retrievedExchangeRate = exchangeRateService.getLatestExchangeRate("USD");

        assertNotNull(retrievedExchangeRate);
        assertEquals("USD", retrievedExchangeRate.getBaseCurrency());
        assertEquals(Map.of("EUR", new BigDecimal("0.85"), "JPY", new BigDecimal("110.0")), retrievedExchangeRate.getRates());
        assertEquals(exchangeRate.getLastUpdated(), retrievedExchangeRate.getLastUpdated());
    }

    @Test
    public void testGetExchangeRate_NotFound() {
        ExchangeRate retrievedExchangeRate = exchangeRateService.getLatestExchangeRate("EUR");

        assertNull(retrievedExchangeRate);
    }

    @Test
    public void testUpdateExchangeRate() {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency("USD");
        exchangeRate.setRates(Map.of("EUR", new BigDecimal("0.85")));
        exchangeRate.setLastUpdated(LocalDateTime.now());

        exchangeRateService.saveExchangeRate(exchangeRate);

        ExchangeRate updatedExchangeRate = new ExchangeRate();
        updatedExchangeRate.setBaseCurrency("USD");
        updatedExchangeRate.setRates(Map.of("EUR", new BigDecimal("0.80"), "GBP", new BigDecimal("0.75")));
        updatedExchangeRate.setLastUpdated(LocalDateTime.now().plusHours(1));

        exchangeRateService.saveExchangeRate(updatedExchangeRate);

        ExchangeRate retrievedExchangeRate = exchangeRateService.getLatestExchangeRate("USD");

        assertNotNull(retrievedExchangeRate);
        assertEquals("USD", retrievedExchangeRate.getBaseCurrency());
        assertEquals(Map.of("EUR", new BigDecimal("0.80"), "GBP", new BigDecimal("0.75")), retrievedExchangeRate.getRates());
        assertEquals(updatedExchangeRate.getLastUpdated(), retrievedExchangeRate.getLastUpdated());
    }

    @Test
    public void testSaveExchangeRate_NullInput() {
        assertThrows(NullPointerException.class, () -> exchangeRateService.saveExchangeRate(null));
    }

    @Test
    public void testGetExchangeRate_NullInput() {
        assertThrows(NullPointerException.class, () -> exchangeRateService.getLatestExchangeRate(null));
    }
}