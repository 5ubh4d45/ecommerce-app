package dev.ixale.ecommerceservice.exception;

public class DoesNotExistsException extends RuntimeException{
    public DoesNotExistsException(String message) {
        super(message);
    }
}
