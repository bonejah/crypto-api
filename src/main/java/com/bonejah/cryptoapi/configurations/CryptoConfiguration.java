package com.bonejah.cryptoapi.configurations;

import com.bonejah.cryptoapi.exceptions.CryptoErrorAttributes;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CryptoConfiguration {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().build();
    }

    @Bean
    public ErrorAttributes getErrorAttributes() {
        return new CryptoErrorAttributes();
    }

}
