package com.bonejah.cryptoapi.services;


import com.bonejah.cryptoapi.domains.CryptoCurrencyData;
import com.bonejah.cryptoapi.dtos.CryptoCurrencyDTO;
import com.bonejah.cryptoapi.dtos.CryptoMapper;
import com.bonejah.cryptoapi.enums.CurrencyEnum;
import com.bonejah.cryptoapi.exceptions.CryptoBadRequestException;
import com.bonejah.cryptoapi.exceptions.CryptoInternalServerErrorException;
import com.bonejah.cryptoapi.exceptions.CryptoNotFoundException;
import com.bonejah.cryptoapi.repositories.CryptoCurrencyRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Log4j2
@Service
public class CryptoCurrencyService {

    private final CryptoCurrencyRepository cryptoCurrencyRepository;

    public CryptoCurrencyService(CryptoCurrencyRepository cryptoCurrencyRepository) {
        this.cryptoCurrencyRepository = cryptoCurrencyRepository;
    }

    public void saveCryptoData(String data) {
        log.info("CryptoCurrencyService - [saveCryptoData]...");
        try {
            var jsonNode = new ObjectMapper().readTree(data).get("Data").get("Data");
            List<CryptoCurrencyDTO> cryptoData = new ObjectMapper().readValue(jsonNode.toString(), new TypeReference<>() {
            });
            log.info("CryptoCurrencyService - [saveCryptoData] - cryptoData: {}", cryptoData);
            var lsCryptoCurrencyData = cryptoData
                    .stream()
                    .map(CryptoMapper::toCryptoCurrencyData)
                    .collect(toList());
            log.info("CryptoCurrencyService - [saveCryptoData] - lsCryptoCurrencyData: {}", lsCryptoCurrencyData);
            cryptoCurrencyRepository.saveAll(lsCryptoCurrencyData);
        } catch (JsonProcessingException e) {
            log.error("CryptoCurrencyService - [saveCryptoData] Error: {}", e.getMessage());
            throw new CryptoInternalServerErrorException("Error on save crypto data");
        }
    }

    public HashMap<String, Object> getCryptosByCurrency(String currency, Pageable pageable) {
        log.info("CryptoCurrencyService - [getCryptosByCurrency]...");
        validateCurrencySymbol(currency);
        var lsCryptosCurrency = cryptoCurrencyRepository.findAllBySymbol(currency, pageable);
        log.info("CryptoCurrencyService - [getCryptosByCurrency] - lsCryptosCurrency: {}", lsCryptosCurrency);
        log.info("CryptoCurrencyService - [getCryptosByCurrency] - lsCryptosCurrency size: {}", lsCryptosCurrency.get().count());
        var lsCryptoCurrencyDTO = lsCryptosCurrency
                .stream()
                .map(CryptoMapper::toCryptoCurrencyDTO)
                .collect(toList());
        log.info("CryptoCurrencyService - [getCryptosByCurrency] - lsCryptoCurrencyDTO: {}", lsCryptoCurrencyDTO);
        HashMap<String, Object> map = new HashMap<>();
        map.put("cryptos", lsCryptoCurrencyDTO);
        map.put("currentPage", lsCryptosCurrency.getNumber());
        map.put("totalItems", lsCryptosCurrency.getTotalElements());
        map.put("totalPages", lsCryptosCurrency.getTotalPages());
        return map;
    }

    @Cacheable(value = "minAverageTransactionValueBySymbol", key = "#currency")
    public HashMap<String, Object> getMinAverageTransactionValueBySymbol(String currency) {
        log.info("CryptoCurrencyService - [getMinAverageTransactionValueBySymbol]...");
        validateCurrencySymbol(currency);
        var minAverageTransactionValueBySymbol = cryptoCurrencyRepository.getMinAverageTransactionValueBySymbol(currency);
        if (minAverageTransactionValueBySymbol == null) {
            throw new CryptoNotFoundException("Min average transaction value not found");
        }
        log.info("CryptoCurrencyService - [getMinAverageTransactionValueBySymbol] - minAverageTransactionValueBySymbol: {}", minAverageTransactionValueBySymbol);
        HashMap<String, Object> map = new HashMap<>();
        map.put("min-average-transaction-value", minAverageTransactionValueBySymbol);
        return map;
    }

    @Cacheable(value = "maxAverageTransactionValueCache", key = "#currency")
    public HashMap<String, Object> getMaxAverageTransactionValueBySymbol(String currency) {
        log.info("CryptoCurrencyService - [getMaxAverageTransactionValueBySymbol]...");
        validateCurrencySymbol(currency);
        var maxAverageTransactionValueBySymbol = cryptoCurrencyRepository.getMaxAverageTransactionValueBySymbol(currency);
        if (maxAverageTransactionValueBySymbol == null) {
            throw new CryptoNotFoundException("Max average transaction value not found");
        }
        log.info("CryptoCurrencyService - [getMaxAverageTransactionValueBySymbol] - maxAverageTransactionValueBySymbol: {}", maxAverageTransactionValueBySymbol);
        HashMap<String, Object> map = new HashMap<>();
        map.put("max-average-transaction-value", maxAverageTransactionValueBySymbol);
        return map;
    }

    public CryptoCurrencyDTO getCryptoById(String id) {
        log.info("CryptoCurrencyService - [getCryptoById]...");
        Optional<CryptoCurrencyData> cryptoCurrencyData = cryptoCurrencyRepository.findById(Long.valueOf(id));
        if (cryptoCurrencyData.isEmpty())
            throw new CryptoNotFoundException(String.format(("Crypto with ID: " + id + " not found")));
        log.info("CryptoCurrencyService - [getCryptoById] - cryptoCurrencyData: {}", cryptoCurrencyData);
        return CryptoMapper.toCryptoCurrencyDTO(cryptoCurrencyData.get());
    }

    private void validateCurrencySymbol(String currency) {
        log.info("CryptoCurrencyService - [validateCurrencySymbol]...");
        try {
            CurrencyEnum.valueOf(currency.toUpperCase());
            log.info("CryptoCurrencyService - [validateCurrencySymbol] - currency: {} validated with success", currency);
        } catch (IllegalArgumentException e) {
            log.error("CryptoCurrencyService - [validateCurrencySymbol] - currency: {} not valid", currency);
            throw new CryptoBadRequestException(String.format(("Currency symbol: " + currency + " not valid")));
        }
    }

}
