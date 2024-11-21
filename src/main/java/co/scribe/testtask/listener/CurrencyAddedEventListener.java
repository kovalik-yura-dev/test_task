package co.scribe.testtask.listener;

import co.scribe.testtask.event.CurrencyAddedEvent;
import co.scribe.testtask.service.ExchangeRateRefreshService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("!skipAddEvent")
@Slf4j
public class CurrencyAddedEventListener {
    private final ExchangeRateRefreshService refreshService;

    public CurrencyAddedEventListener(ExchangeRateRefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @EventListener
    public void handleCurrencyAddedEvent(CurrencyAddedEvent event) {
            log.info("Refresh Exchange Rate of Currency '{}' from Event", event.getCurrency());
            refreshService.refresh(event.getCurrency());
    }
}