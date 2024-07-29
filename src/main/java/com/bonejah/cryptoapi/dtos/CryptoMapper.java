package com.bonejah.cryptoapi.dtos;

import com.bonejah.cryptoapi.domains.CryptoCurrencyData;
import org.springframework.stereotype.Component;

@Component
public class CryptoMapper {

    public static CryptoCurrencyData toCryptoCurrencyData(CryptoCurrencyDTO dto) {
        return CryptoCurrencyData.builder()
                .idCrypto(dto.getId())
                .symbol(dto.getSymbol())
                .time(dto.getTime())
                .transactionCount(dto.getTransactionCount())
                .transactionCountAllTime(dto.getTransactionCountAllTime())
                .largeTransactionCount(dto.getLargeTransactionCount())
                .averageTransactionValue(dto.getAverageTransactionValue())
                .blockHeight(dto.getBlockHeight())
                .currentSupply(dto.getCurrentSupply())
                .build();
    }

    public static CryptoCurrencyDTO toCryptoCurrencyDTO(CryptoCurrencyData data) {
        return CryptoCurrencyDTO.builder()
                .id(data.getId())
                .symbol(data.getSymbol())
                .time(data.getTime())
                .transactionCount(data.getTransactionCount())
                .transactionCountAllTime(data.getTransactionCountAllTime())
                .largeTransactionCount(data.getLargeTransactionCount())
                .averageTransactionValue(data.getAverageTransactionValue())
                .blockHeight(data.getBlockHeight())
                .currentSupply(data.getCurrentSupply())
                .build();
    }
}
