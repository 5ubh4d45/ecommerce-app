package dev.ixale.ecommerceservice.exception;


import java.util.Map;

public class InvalidRequestException extends IllegalArgumentException {
    private Map<String, String> details = null;

    public InvalidRequestException(String msg) {
        super(msg);
    }
    public InvalidRequestException(String msg, Map<String, String> details){
        super(msg);
        this.details = details;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}
