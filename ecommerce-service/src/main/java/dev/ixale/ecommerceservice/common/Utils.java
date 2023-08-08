package dev.ixale.ecommerceservice.common;

import org.springframework.validation.BindingResult;

import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    /**
     * Extracts error messages from a BindingResult object and returns them as a Map.
     *
     * @param bindingResult the BindingResult object containing the field errors
     * @return a Map containing the field names as keys and the error messages as values
     */
    public static Map<String, String> extractErrFromValid(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        err -> err.getField(),
                        err -> err.getDefaultMessage() == null ? "" : err.getDefaultMessage()));
    }
}
