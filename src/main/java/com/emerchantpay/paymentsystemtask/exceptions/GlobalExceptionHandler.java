package com.emerchantpay.paymentsystemtask.exceptions;

import com.emerchantpay.paymentsystemtask.enums.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
public class GlobalExceptionHandler {
    Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String DUPLICATE_MERCHANT_C = "PAYMENT_SYSTEM.MER_DESCR_STAT_C";

    @ExceptionHandler({MerchantException.class, TransactionException.class})
    public ResponseEntity<Object> handleBusinessException(Exception exception) {

        log.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleSqlException(Exception exception) {

        if(exception.getMessage().contains(DUPLICATE_MERCHANT_C)){
            log.error(exception.getMessage(), exception);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Message.DUPLICATE_MERCHANT.getName());
        }

        log.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

    @ExceptionHandler({HttpClientErrorException.Forbidden.class})
    public ResponseEntity<Object> handleForbiddenException(HttpClientErrorException.Forbidden exception) {

        log.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Message.FORBIDDEN.getName());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception exception) {

        log.error(exception.getMessage(), exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }

}