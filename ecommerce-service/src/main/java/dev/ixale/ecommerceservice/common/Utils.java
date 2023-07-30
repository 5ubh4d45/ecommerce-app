package dev.ixale.ecommerceservice.common;

import org.springframework.validation.BindingResult;

import java.util.stream.Collectors;

public class Utils {
    public static String notValid(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(","));
    }
}
