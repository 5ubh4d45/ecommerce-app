package dev.ixale.ecommerceservice.exception;

public class FailedOperationException extends RuntimeException{
    public FailedOperationException(String message) {
        super(message);
    }
}
