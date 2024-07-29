package com.bonejah.cryptoapi.controllers;

import com.bonejah.cryptoapi.dtos.CryptoCurrencyDTO;
import com.bonejah.cryptoapi.exceptions.CryptoInternalServerErrorException;
import com.bonejah.cryptoapi.services.CryptoCurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
public class CryptoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private CryptoCurrencyService cryptoCurrencyService;

    @Test
    public void shouldLoadCurrencies() throws Exception {
        var response = ResponseEntity.ok("{\n" +
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
                "}");

        when(restTemplate.exchange("https://min-api.cryptocompare.com/data/blockchain/histo/day?fsym=BTC&api_key=e9ece706fc8b6a27b2b0fc586b2a78d86414009d951548dfebf82c59657c4c93", HttpMethod.GET, null, String.class))
                .thenReturn(response);
        when(restTemplate.exchange("https://min-api.cryptocompare.com/data/blockchain/histo/day?fsym=ETH&api_key=e9ece706fc8b6a27b2b0fc586b2a78d86414009d951548dfebf82c59657c4c93", HttpMethod.GET, null, String.class))
                .thenReturn(response);
        mockMvc.perform(get("/crypto-api/loading-currencies"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldLoadCurrenciesWhenBodyIsEmpty() throws Exception {
        var response = ResponseEntity.ok("");
        when(restTemplate.exchange("https://min-api.cryptocompare.com/data/blockchain/histo/day?fsym=BTC&api_key=e9ece706fc8b6a27b2b0fc586b2a78d86414009d951548dfebf82c59657c4c93", HttpMethod.GET, null, String.class))
                .thenReturn(response);
        when(restTemplate.exchange("https://min-api.cryptocompare.com/data/blockchain/histo/day?fsym=ETH&api_key=e9ece706fc8b6a27b2b0fc586b2a78d86414009d951548dfebf82c59657c4c93", HttpMethod.GET, null, String.class))
                .thenReturn(response);
        mockMvc.perform(get("/crypto-api/loading-currencies"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void shouldThrowErrorWhenLoadCurrencies() throws Exception {
        when(restTemplate.exchange("https://min-api.cryptocompare.com/data/blockchain/histo/day?fsym=BTC&api_key=e9ece706fc8b6a27b2b0fc586b2a78d86414009d951548dfebf82c59657c4c93", HttpMethod.GET, null, String.class))
                .thenReturn(ResponseEntity.internalServerError().build());
        mockMvc.perform(get("/crypto-api/loading-currencies"))
                .andExpect(status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof CryptoInternalServerErrorException));
    }

    @Test
    public void shouldGetCryptosByBTCCurrency() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        List<CryptoCurrencyDTO> lsCryptoCurrencyDTO = new ArrayList<>();
        lsCryptoCurrencyDTO.add(createCryptoCurrencyDTO());
        map.put("cryptos", lsCryptoCurrencyDTO);
        map.put("currentPage", 0);
        map.put("totalItems", 29);
        map.put("totalPages", 3);
        when(cryptoCurrencyService.getCryptosByCurrency(anyString(), any()))
                .thenReturn(map);
        mockMvc.perform(get("/crypto-api/cryptos/btc/symbol"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetMaxAverageTransactionValue() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("max-average-transaction-value", BigDecimal.valueOf(2.14468702177550));
        when(cryptoCurrencyService.getMaxAverageTransactionValueBySymbol(anyString())).thenReturn(map);
        mockMvc.perform(get("/crypto-api/cryptos/{currency}/max-average-transaction-value", "btc"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetMinAverageTransactionValue() throws Exception {
        var currency = "btc";
        HashMap<String, Object> map = new HashMap<>();
        map.put("min-average-transaction-value", BigDecimal.valueOf(0.54834515471025));
        when(cryptoCurrencyService.getMinAverageTransactionValueBySymbol(currency)).thenReturn(map);
        mockMvc.perform(get("/crypto-api/cryptos/{currency}/min-average-transaction-value", currency))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetCryptoById() throws Exception {
        when(cryptoCurrencyService.getCryptoById(anyString())).thenReturn(createCryptoCurrencyDTO());
        mockMvc.perform(get("/crypto-api/cryptos/{id}", "10"))
                .andExpect(status().isOk());
    }

    private CryptoCurrencyDTO createCryptoCurrencyDTO() {
        return CryptoCurrencyDTO.builder()
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
