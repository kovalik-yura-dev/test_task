package co.scribe.testtask.service.impl;

import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RepositoryExchangeRateServiceImplTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @InjectMocks
    private RepositoryExchangeRateServiceImpl exchangeRateService;

    private ExchangeRate exchangeRate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        exchangeRate = new ExchangeRate();
        exchangeRate.setBaseCurrency("USD");
        exchangeRate.setRates(Map.of("EUR", new BigDecimal("0.85"), "JPY", new BigDecimal("110.0")));
        exchangeRate.setLastUpdated(LocalDateTime.now());
    }

    @Test
    public void testGetLatestExchangeRate() {
        when(exchangeRateRepository.findFirstByBaseCurrencyOrderByLastUpdatedDesc("USD")).thenReturn(exchangeRate);

        ExchangeRate result = exchangeRateService.getLatestExchangeRate("USD");

        assertNotNull(result);
        assertEquals("USD", result.getBaseCurrency());
        assertEquals(exchangeRate.getRates(), result.getRates());
        assertEquals(exchangeRate.getLastUpdated(), result.getLastUpdated());
    }

    @Test
    public void testGetLatestExchangeRate_NotFound() {
        when(exchangeRateRepository.findFirstByBaseCurrencyOrderByLastUpdatedDesc("EUR")).thenReturn(null);

        ExchangeRate result = exchangeRateService.getLatestExchangeRate("EUR");

        assertNull(result);
    }

    @Test
    public void testSaveExchangeRate() {
        exchangeRateService.saveExchangeRate(exchangeRate);

        verify(exchangeRateRepository, times(1)).save(exchangeRate);
    }


}