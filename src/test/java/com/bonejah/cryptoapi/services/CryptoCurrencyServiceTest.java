package com.bonejah.cryptoapi.services;

import com.bonejah.cryptoapi.domains.CryptoCurrencyData;
import com.bonejah.cryptoapi.dtos.CryptoCurrencyDTO;
import com.bonejah.cryptoapi.exceptions.CryptoBadRequestException;
import com.bonejah.cryptoapi.exceptions.CryptoInternalServerErrorException;
import com.bonejah.cryptoapi.exceptions.CryptoNotFoundException;
import com.bonejah.cryptoapi.repositories.CryptoCurrencyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CryptoCurrencyServiceTest {

    @InjectMocks
    private CryptoCurrencyService cryptoCurrencyService;

    @Mock
    private CryptoCurrencyRepository cryptoCurrencyRepository;

    private final String data = "{\n" +
            "  \"Response\": \"Success\",\n" +
            "  \"Message\": \"\",\n" +
            "  \"HasWarning\": false,\n" +
            "  \"Type\": 100,\n" +
            "  \"RateLimit\": {},\n" +
            "  \"Data\": {\n" +
            "    \"Aggregated\": false,\n" +
            "    \"TimeFrom\": 1719619200,\n" +
            "    \"TimeTo\": 1722211200,\n" +
            "    \"Data\": [\n" +
            "      {\n" +
            "        \"id\": 1182,\n" +
            "        \"symbol\": \"BTC\",\n" +
            "        \"time\": 1719619200,\n" +
            "        \"zero_balance_addresses_all_time\": 1271910461,\n" +
            "        \"unique_addresses_all_time\": 1324972563,\n" +
            "        \"new_addresses\": 323233,\n" +
            "        \"active_addresses\": 731018,\n" +
            "        \"transaction_count\": 712249,\n" +
            "        \"transaction_count_all_time\": 1032424096,\n" +
            "        \"large_transaction_count\": 12215,\n" +
            "        \"average_transaction_value\": 0.636404003964878,\n" +
            "        \"block_height\": 850039,\n" +
            "        \"hashrate\": 659689031.552329,\n" +
            "        \"difficulty\": 83675262295059.9,\n" +
            "        \"block_time\": 544.775641025641,\n" +
            "        \"block_size\": 1638790,\n" +
            "        \"current_supply\": 19718600\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": 1182,\n" +
            "        \"symbol\": \"BTC\",\n" +
            "        \"time\": 1719705600,\n" +
            "        \"zero_balance_addresses_all_time\": 1272210976,\n" +
            "        \"unique_addresses_all_time\": 1325278980,\n" +
            "        \"new_addresses\": 306417,\n" +
            "        \"active_addresses\": 674631,\n" +
            "        \"transaction_count\": 505127,\n" +
            "        \"transaction_count_all_time\": 1032929223,\n" +
            "        \"large_transaction_count\": 12397,\n" +
            "        \"average_transaction_value\": 0.738854943903711,\n" +
            "        \"block_height\": 850160,\n" +
            "        \"hashrate\": 501079425.150239,\n" +
            "        \"difficulty\": 83675262295059.9,\n" +
            "        \"block_time\": 717.216666666667,\n" +
            "        \"block_size\": 1587947,\n" +
            "        \"current_supply\": 19718878\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    private final String dataWrong = "{\n" +
            "  \"Response\": \"Success\",\n" +
            "  \"Message\": \"\",\n" +
            "  \"HasWarning\": false,\n" +
            "  \"Type\": 100,\n" +
            "  \"RateLimit\": {},\n" +
            "  \"Data\": {\n" +
            "    \"Aggregated\": false,\n" +
            "    \"TimeFrom\": 1719619200,\n" +
            "    \"TimeTo\": 1722211200,\n" +
            "  }\n" +
            "}";

    @Test
    public void shouldSaveCryptoData() {
        List<CryptoCurrencyData> lsCryptoCurrencyData = new ArrayList<>();
        lsCryptoCurrencyData.add(createCryptoCurrencyData());
        when(cryptoCurrencyRepository.saveAll(Arrays.asList(createCryptoCurrencyData(), createCryptoCurrencyData())))
                .thenReturn(lsCryptoCurrencyData);
        cryptoCurrencyService.saveCryptoData(data);
        verify(cryptoCurrencyRepository, times(1)).saveAll(any());
    }

    @Test
    public void shouldNotSaveCryptoDataWhenExceptionOccurs() {
        assertThrows(CryptoInternalServerErrorException.class, () -> {
            cryptoCurrencyService.saveCryptoData(dataWrong);
        });
        verify(cryptoCurrencyRepository, times(0)).saveAll(any());
    }

    @Test
    public void shouldGetCryptosByCurrency() {
        var page = new PageImpl(Arrays.asList(createCryptoCurrencyData()));
        when(cryptoCurrencyRepository.findAllBySymbol(any(), any())).thenReturn(page);
        var response = cryptoCurrencyService.getCryptosByCurrency("btc", PageRequest.of(0, 3));
        assertNotNull(response);
        assertEquals(1, ((ArrayList<?>)response.get("cryptos")).size());
        assertNotNull(response.get("currentPage"));
        assertNotNull(response.get("totalItems"));
        assertNotNull(response.get("totalPages"));
    }

    @Test
    public void shouldNotGetCryptosByCurrencyWhenSymbolIsNotValid() {
        assertThrows(CryptoBadRequestException.class, () -> {
            cryptoCurrencyService.getCryptosByCurrency("xpto", PageRequest.of(0, 3));
        });
    }

    @Test
    public void shouldGetMinAverageTransactionValueBySymbol() {
        when(cryptoCurrencyRepository.getMinAverageTransactionValueBySymbol(any()))
                .thenReturn(BigDecimal.valueOf(0.54834515471025));
        var response = cryptoCurrencyService.getMinAverageTransactionValueBySymbol("btc");
        assertNotNull(response);
        assertNotNull(response.get("min-average-transaction-value"));
    }

    @Test
    public void shouldNotGetMinAverageTransactionValueBySymbol() {
        when(cryptoCurrencyRepository.getMinAverageTransactionValueBySymbol(any()))
                .thenReturn(null);
        assertThrows(CryptoNotFoundException.class, () -> {
            cryptoCurrencyService.getMinAverageTransactionValueBySymbol("btc");
        });
    }

    @Test
    public void shouldGetMaxAverageTransactionValueBySymbol() {
        when(cryptoCurrencyRepository.getMaxAverageTransactionValueBySymbol(any()))
                .thenReturn(BigDecimal.valueOf(0.54834515471025));
        var response = cryptoCurrencyService.getMaxAverageTransactionValueBySymbol("btc");
        assertNotNull(response);
        assertNotNull(response.get("max-average-transaction-value"));
    }

    @Test
    public void shouldNotGetMaxAverageTransactionValueBySymbol() {
        when(cryptoCurrencyRepository.getMaxAverageTransactionValueBySymbol(any()))
                .thenReturn(null);
        assertThrows(CryptoNotFoundException.class, () -> {
            cryptoCurrencyService.getMaxAverageTransactionValueBySymbol("btc");
        });
    }

    @Test
    public void shouldGetCryptoById() {
        when(cryptoCurrencyRepository.findById(anyLong())).thenReturn(Optional.of(createCryptoCurrencyData()));
        var crypto = cryptoCurrencyService.getCryptoById("1");
        assertNotNull(crypto);
    }

    @Test
    public void shouldNotGetCryptoById() {
        when(cryptoCurrencyRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CryptoNotFoundException.class, () -> {
            cryptoCurrencyService.getCryptoById("1");
        });
    }

    private CryptoCurrencyData createCryptoCurrencyData() {
        return CryptoCurrencyData.builder()
                .id(1L)
                .symbol("BTC")
                .time(1719619200L)
                .transactionCount(712249L)
                .transactionCountAllTime(1032424096L)
                .largeTransactionCount(12215L)
                .averageTransactionValue(BigDecimal.valueOf(0.63640400396488))
                .blockHeight(850039L)
                .currentSupply(19718600L)
                .build();
    }

}
