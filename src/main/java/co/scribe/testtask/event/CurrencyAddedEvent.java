package co.scribe.testtask.event;

import co.scribe.testtask.model.Currency;
import org.springframework.context.ApplicationEvent;

public class CurrencyAddedEvent extends ApplicationEvent {
    private final Currency currency;

    public CurrencyAddedEvent(Currency source) {
        super(source);
        this.currency = source;
    }

    public Currency getCurrency() {
        return currency;
    }
}