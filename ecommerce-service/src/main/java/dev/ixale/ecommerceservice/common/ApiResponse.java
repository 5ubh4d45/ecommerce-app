package dev.ixale.ecommerceservice.common;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * ApiResponse is a generic class that is used to return a response to the client
 * @param <T> type of data
 */
public class ApiResponse<T> {
    private final Boolean success;
    private final String message;
    private final T data;
    private final LocalDateTime timeStamp;

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public T getData() {
        return data;
    }

    /**
     * Create a success ApiResponse
     * @param data data to be returned
     * @param message success message
     * @return ApiResponse with success = true
     * @param <T> type of data
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Create an error ApiResponse
     * @param message error message
     * @return ApiResponse with success = false
     * @param <T> type of data
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public ApiResponse(Boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.timeStamp = LocalDateTime.now(Clock.systemUTC());
        this.data = data;
    }
}
