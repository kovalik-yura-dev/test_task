package co.scribe.testtask.service.impl.apiclient;

import co.scribe.testtask.model.ExchangeRateResponse;
import co.scribe.testtask.service.ExchangeRateApiClient;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExchangeRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @ParameterizedTest
    @MethodSource("provideExchangeRateApiClients")
    public void testGetExchangeRates(Supplier<ExchangeRateApiClient> apiClientSupplier) {
        ExchangeRateApiClient exchangeRateService = apiClientSupplier.get();

        String currencyCode = "EUR";
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setBase(currencyCode);

        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class)))
                .thenReturn(mockResponse);

        ExchangeRateResponse response = exchangeRateService.fetchExchangeRates(currencyCode);

        assertEquals(currencyCode, response.getBase());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(ExchangeRateResponse.class));

        reset(restTemplate);
    }


    private Stream<Arguments> provideExchangeRateApiClients() {
        return Stream.of(
                Arguments.of((Supplier<ExchangeRateApiClient>)
                        () -> new FixerExchangeRateApiClient(restTemplate)),
                Arguments.of((Supplier<ExchangeRateApiClient>)
                        () -> new ExchangerateApiClient(restTemplate, "localhost", "42"))
        );
    }
}