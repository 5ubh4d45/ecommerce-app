package dev.ixale.ecommerceservice.exception;

import dev.ixale.ecommerceservice.common.ApiRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {"dev.ixale.ecommerceservice.controller"})
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = InvalidRequestException.class)
    public final ResponseEntity<ApiRes<Object>> handleInvalidRequestException(InvalidRequestException e) {
        return ResponseEntity.badRequest().body(ApiRes.error(e.getMessage(), e.getDetails()));
    }
    @ExceptionHandler(value = AlreadyExistsException.class)
    public final ResponseEntity<ApiRes<Object>> handleAlreadyExistsException(AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiRes.error(e.getMessage()));
    }
    @ExceptionHandler(value = DoesNotExistsException.class)
    public final ResponseEntity<ApiRes<Object>> handleNotFoundException(DoesNotExistsException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiRes.error(e.getMessage()));
    }
    @ExceptionHandler(value = FailedOperationException.class)
    public final ResponseEntity<ApiRes<Object>> handleOperationFailedException(FailedOperationException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiRes.error(e.getMessage()));
    }
}
