package dev.ixale.ecommerceservice.common;

import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.stream.Collectors;

public class Utils {
    public static Map<String, String> notValid(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        err -> err.getField(),
                        err -> err.getDefaultMessage() == null ? "" : err.getDefaultMessage()));
    }
}
