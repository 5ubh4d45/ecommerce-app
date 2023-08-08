package dev.ixale.ecommerceservice.exception;

public class OperationFailedException extends RuntimeException{
    public OperationFailedException(String message) {
        super(message);
    }
}
