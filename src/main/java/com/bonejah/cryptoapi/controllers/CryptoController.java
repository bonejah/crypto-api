package com.bonejah.cryptoapi.controllers;

import com.bonejah.cryptoapi.dtos.CryptoCurrencyDTO;
import com.bonejah.cryptoapi.enums.CurrencyEnum;
import com.bonejah.cryptoapi.exceptions.CryptoInternalServerErrorException;
import com.bonejah.cryptoapi.services.CryptoCurrencyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/crypto-api")
public class CryptoController {

    private static final Logger log = LogManager.getLogger(CryptoController.class);
    public static final String DATA_BLOCKCHAIN_HISTO_DAY_FSYM = "data/blockchain/histo/day?fsym=";
    public static final String API_KEY = "&api_key=";

    private final RestTemplate restTemplate;

    private final CryptoCurrencyService cryptoCurrencyService;

    @Value("${crypto-compare-url}")
    private String cryptoCompareUrl;

    @Value("${crypto-api-key}")
    private String cryptoApiKey;

    public CryptoController(RestTemplate restTemplate, CryptoCurrencyService cryptoCurrencyService) {
        this.restTemplate = restTemplate;
        this.cryptoCurrencyService = cryptoCurrencyService;
    }

    @GetMapping(value = "loading-currencies")
    public void loadingCurrencies() {
        log.info("CryptoController - [loadingCurrencies]...");
        loadingData(CurrencyEnum.BTC.name());
        loadingData(CurrencyEnum.ETH.name());
        log.info("CryptoController - [loadingCurrencies] - Data loaded with success!");
    }

    @GetMapping(value = "cryptos/{currency}/symbol", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> getCryptosByCurrency(@PathVariable String currency,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "3") int size) {
        log.info("CryptoController - [getCryptosByCurrency]...");
        log.info("CryptoController - [getCryptosByCurrency] - currency: {}", currency);
        var pageRequest = PageRequest.of(page, size);
        return cryptoCurrencyService.getCryptosByCurrency(currency, pageRequest);
    }

    @GetMapping(value = "cryptos/{currency}/max-average-transaction-value", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> getMaxAverageTransactionValue(@PathVariable String currency) {
        log.info("CryptoController - [getMaxAverageTransactionValueBySymbol]...");
        log.info("CryptoController - [getMaxAverageTransactionValueBySymbol] - currency: {}", currency);
        return cryptoCurrencyService.getMaxAverageTransactionValueBySymbol(currency);
    }

    @GetMapping(value = "cryptos/{currency}/min-average-transaction-value", produces = MediaType.APPLICATION_JSON_VALUE)
    public HashMap<String, Object> getMinAverageTransactionValue(@PathVariable String currency) {
        log.info("CryptoController - [getMinAverageTransactionValue]...");
        log.info("CryptoController - [getMinAverageTransactionValue] - currency: {}", currency);
        return cryptoCurrencyService.getMinAverageTransactionValueBySymbol(currency);
    }

    @GetMapping(value = "cryptos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CryptoCurrencyDTO getCryptoById(@PathVariable String id) {
        log.info("CryptoController - [getCryptoById]...");
        log.info("CryptoController - [getCryptoById] - currency: {}", id);
        return cryptoCurrencyService.getCryptoById(id);
    }

    private void loadingData(String symbol) {
        log.info("CryptoController - [loadingData]...");
        var url = cryptoCompareUrl + DATA_BLOCKCHAIN_HISTO_DAY_FSYM + symbol + API_KEY + cryptoApiKey;
        log.info("CryptoController - [loadingData] - url: {}", url.replace(url.substring(url.indexOf(API_KEY)), ""));
        var response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        if (response.getStatusCode().is2xxSuccessful() && !Objects.requireNonNull(response.getBody()).isBlank()) {
            cryptoCurrencyService.saveCryptoData(response.getBody());
            return;
        }
        throw new CryptoInternalServerErrorException("Error on loading historical currencies");
    }


}
