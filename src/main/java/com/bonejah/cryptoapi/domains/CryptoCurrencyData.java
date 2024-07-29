package com.bonejah.cryptoapi.domains;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "crypto_currency_data")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoCurrencyData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idCrypto;
    private String symbol;
    private Long time;
    private Long transactionCount;
    private Long transactionCountAllTime;
    private Long largeTransactionCount;

    @Column(precision = 18, scale = 14)
    private BigDecimal averageTransactionValue;

    private Long blockHeight;
    private Long currentSupply;


}
