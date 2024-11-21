package co.scribe.testtask.controller;

import co.scribe.testtask.event.CurrencyAddedEvent;
import co.scribe.testtask.model.Currency;
import co.scribe.testtask.repository.CurrencyRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrencyController {

    private final CurrencyRepository currencyRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CurrencyController(CurrencyRepository currencyRepository, ApplicationEventPublisher eventPublisher) {
        this.currencyRepository = currencyRepository;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/currencies")
    public List<Currency> getCurrencies() {
        return currencyRepository.findAll();
    }

    @PostMapping("/currencies")
    public ResponseEntity<Currency> addCurrency(@RequestBody Currency currency) {
        if (currencyRepository.existsByCode(currency.getCode())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Currency saved = currencyRepository.save(currency);
        eventPublisher.publishEvent(new CurrencyAddedEvent(currency));

        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}