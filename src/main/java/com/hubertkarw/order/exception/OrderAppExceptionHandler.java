package com.hubertkarw.order.exception;

import com.hubertkarw.order.model.ErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class OrderAppExceptionHandler {

    @ExceptionHandler(OrderAppException.class)
    ResponseEntity<ErrorMessage> handleProductAppException(OrderAppException exception) {
        log.warn("OrderAppException: status={}, message={}", exception.getStatus(), exception.getMessage());
        return ResponseEntity.status(exception.getStatus())
                .body(new ErrorMessage(exception.getStatus().value(), exception.getStatus().getReasonPhrase(), exception.getMessage(), exception.getTimestamp()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorMessage> handleExceptions(Exception exception) {
        log.error("Unexpected Exception occurred with message:{}",exception.getMessage() , exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Unknown Error", LocalDateTime.now()));
    }
}
