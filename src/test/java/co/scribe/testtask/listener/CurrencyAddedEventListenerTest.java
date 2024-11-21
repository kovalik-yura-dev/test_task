package co.scribe.testtask.listener;


import co.scribe.testtask.event.CurrencyAddedEvent;
import co.scribe.testtask.model.Currency;
import co.scribe.testtask.service.ExchangeRateRefreshService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CurrencyAddedEventListenerTest {

    @Mock
    private ExchangeRateRefreshService exchangeRateRefreshService;

    private CurrencyAddedEventListener listener;

    @BeforeEach
    public void setUp() {
        listener = new CurrencyAddedEventListener(exchangeRateRefreshService);
    }

    @Test
    public void testHandleCurrencyAddedEvent() {
        Currency currency = new Currency("USD");
        CurrencyAddedEvent event = new CurrencyAddedEvent(currency);

        listener.handleCurrencyAddedEvent(event);

        verify(exchangeRateRefreshService, times(1)).refresh(currency);
    }
}