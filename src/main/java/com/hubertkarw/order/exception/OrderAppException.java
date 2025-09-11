package com.hubertkarw.order.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderAppException extends RuntimeException{

    private LocalDateTime timestamp;
    private HttpStatus status;

    public OrderAppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}
