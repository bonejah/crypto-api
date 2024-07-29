package com.bonejah.cryptoapi.exceptions;

public class CryptoNotFoundException extends RuntimeException {
    public CryptoNotFoundException(String message) {
        super(message);
    }
}
