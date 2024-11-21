package co.scribe.testtask.controller;

import co.scribe.testtask.model.Currency;
import co.scribe.testtask.repository.CurrencyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(CurrencyController.class)
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyRepository currencyRepository;

    @MockBean
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetCurrencies() throws Exception {
        Currency currency = new Currency();
        currency.setCode("EUR");

        when(currencyRepository.findAll()).thenReturn(Collections.singletonList(currency));

        mockMvc.perform(get("/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].code", hasItem("EUR")));
    }


    @Test
    public void testAddCurrency() throws Exception {

        Currency currency = new Currency("USD");
        Currency savedCurrency = new Currency("USD");
        when(currencyRepository.save(any(Currency.class))).thenReturn(savedCurrency);

        mockMvc.perform(post("/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currency)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(savedCurrency)));

        verify(currencyRepository, times(1)).save(currency);
    }
}