package com.bonejah.cryptoapi.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

//@Component
public class CryptoErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        // Disable stack trace
        options = options.excluding(ErrorAttributeOptions.Include.STACK_TRACE);

        // Get the default error attributes
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        return errorAttributes;
    }

}
