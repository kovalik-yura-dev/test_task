package co.scribe.testtask.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baseCurrency;

    private LocalDateTime lastUpdated;

    @ElementCollection
    @CollectionTable(name = "exchange_rate_values", joinColumns = @JoinColumn(name = "exchange_rate_id"))
    @MapKeyColumn(name = "currency")
    @Column(name = "rate")
    private Map<String, BigDecimal> rates;

}