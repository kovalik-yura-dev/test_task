package co.scribe.testtask.controller;

import co.scribe.testtask.model.ExchangeRate;
import co.scribe.testtask.service.ExchangeRateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ExchangeRateController.class)
public class ExchangeRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExchangeRateService exchangeRateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetExchangeRates_Success() throws Exception {

        String currency = "USD";
        ExchangeRate mockExchangeRate = new ExchangeRate();
        mockExchangeRate.setBaseCurrency(currency);
        mockExchangeRate.setRates(Map.of(
                "EUR", new BigDecimal("0.85"),
                "JPY", new BigDecimal("110.0")
        ));
        mockExchangeRate.setLastUpdated(LocalDateTime.now());

        when(exchangeRateService.getLatestExchangeRate(currency)).thenReturn(mockExchangeRate);


        mockMvc.perform(get("/currencies/{currency}", currency)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockExchangeRate)));

        verify(exchangeRateService, times(1)).getLatestExchangeRate(currency);
    }
}