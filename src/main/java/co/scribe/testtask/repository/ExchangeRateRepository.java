package co.scribe.testtask.repository;

import co.scribe.testtask.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    ExchangeRate findFirstByBaseCurrencyOrderByLastUpdatedDesc(String baseCurrency);

    
}