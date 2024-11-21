package co.scribe.testtask.controller;

import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @Operation(summary = "Get Exchange Rates",
            description = "Returns the current exchange rate for the specified base currency.")
    @GetMapping("/currencies/{currency}")
    public ExchangeRate getExchangeRates(@PathVariable String currency) {
        return exchangeRateService.getLatestExchangeRate(currency);
    }
}