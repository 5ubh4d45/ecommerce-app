package dev.ixale.ecommerceservice.common;

import dev.ixale.ecommerceservice.controller.AuthController;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * Returns a logger instance for a given class.
     *
     * @param clazz the class for which the logger is requested
     * @return the logger instance
     */
    public static Logger getLogger(@NotNull Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
