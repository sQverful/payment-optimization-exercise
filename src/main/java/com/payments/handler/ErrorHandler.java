package com.payments.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.payments.exception.ErrorsException;
import com.payments.model.ErrorResponse;
import com.payments.model.ErrorsResponse;

@ControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(value = {ErrorsException.class})
    public ResponseEntity<ErrorsResponse> validationException(ErrorsException errorsException) {
        return ResponseEntity.badRequest().body(
                ErrorsResponse
                        .builder()
                        .errors(errorsException.getErrors())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> catchAllException(Exception exception) {
        log.error("Error ", exception);
        return new ResponseEntity<>(
                ErrorResponse
                        .builder()
                        .description("An unexpected error occurred: " + exception.getMessage())
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
