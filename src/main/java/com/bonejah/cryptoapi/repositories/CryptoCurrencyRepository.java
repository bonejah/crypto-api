package com.bonejah.cryptoapi.repositories;

import com.bonejah.cryptoapi.domains.CryptoCurrencyData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CryptoCurrencyRepository extends JpaRepository<CryptoCurrencyData, Long> {
    Page<CryptoCurrencyData> findAllBySymbol(String name, Pageable pageable);

    @Query("SELECT MAX(c.averageTransactionValue) FROM CryptoCurrencyData c WHERE c.symbol = ?1")
    BigDecimal getMaxAverageTransactionValueBySymbol(String currency);

    @Query("SELECT MIN(c.averageTransactionValue) FROM CryptoCurrencyData c WHERE c.symbol = ?1")
    BigDecimal getMinAverageTransactionValueBySymbol(String currency);

}
