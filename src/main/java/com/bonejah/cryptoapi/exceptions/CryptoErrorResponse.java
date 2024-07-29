package com.bonejah.cryptoapi.exceptions;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CryptoErrorResponse implements Serializable {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public CryptoErrorResponse(LocalDateTime timestamp,
                               int status,
                               String error,
                               String message,
                               String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

}
