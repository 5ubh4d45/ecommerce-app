package dev.ixale.ecommerceservice.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

/**
 * ApiRes is a generic class that is used to return a response to the client
 * @param <T> type of data
 */
public class ApiRes<T> {
    private final Boolean success;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;
    private final Instant timeStamp;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public T getData() {
        return data;
    }

    /**
     * Create a success ApiRes with a message and body
     * @param data data to be returned
     * @param message success message
     * @return ApiRes with success = true
     * @param <T> type of data
     */
    public static <T> ApiRes<T> success(T data, String message) {
        return new ApiRes<>(true, message, data);
    }

    /**
     * Create an error ApiRes with a message
     * @param message error message
     * @return ApiRes with success = false
     * @param <T> type of data
     */
    public static <T> ApiRes<T> error(String message) {
        return new ApiRes<>(false, message, null);
    }
    /**
     * Create an error ApiRes with a message and a error details object
     * Preferably a <b>Map of {@code <String, String>}</b>
     * @param message error message
     * @param details error details object to be passed onto the response.
     * @return ApiRes with success = false
     * @param <T> type of data
     */
    public static <T> ApiRes<T> error(String message, T details) {
        return new ApiRes<>(false, message, details);
    }

    private ApiRes(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.timeStamp = Instant.now();
        this.data = data;
    }

}
