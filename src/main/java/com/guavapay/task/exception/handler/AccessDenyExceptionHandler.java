package com.guavapay.task.exception.handler;

import com.guavapay.task.dto.ErrorResponse;
import com.guavapay.task.exception.AccessDenyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccessDenyExceptionHandler {

    @ExceptionHandler(AccessDenyException.class)
    public ErrorResponse handleAccessDenyException(AccessDenyException e){
        return ErrorResponse.builder()
                .message(e.getMessage())
                .errorCode(e.getMessageCode())
                .build();
    }
}