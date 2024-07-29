package com.bonejah.cryptoapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CryptoCurrencyDTO {

    private Long id;
    private String symbol;
    private Long time;

    @JsonProperty("zero_balance_addresses_all_time")
    private Long zeroBalanceAddressesAllTime;

    @JsonProperty("unique_addresses_all_time")
    private Long uniqueAddressesAllTime;

    @JsonProperty("new_addresses")
    private Long new_addresses;

    @JsonProperty("active_addresses")
    private Long activeAddresses;

    @JsonProperty("transaction_count")
    private Long transactionCount;

    @JsonProperty("transaction_count_all_time")
    private Long transactionCountAllTime;

    @JsonProperty("large_transaction_count")
    private Long largeTransactionCount;

    @JsonProperty("average_transaction_value")
    @Column(precision = 18, scale = 14)
    private BigDecimal averageTransactionValue;

    @JsonProperty("block_height")
    private Long blockHeight;

    @JsonProperty("hashrate")
    private BigDecimal hashrate;

    private BigDecimal difficulty;

    @JsonProperty("block_time")
    private BigDecimal blockTime;

    @JsonProperty("block_size")
    private Long blockSize;

    @JsonProperty("current_supply")
    private Long currentSupply;

}
