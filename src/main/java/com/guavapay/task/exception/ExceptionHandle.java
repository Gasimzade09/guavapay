package com.guavapay.task.exception;

import com.guavapay.task.dto.ErrorResponse;
import com.guavapay.task.dto.Response;
import com.guavapay.task.security.exceptions.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(AccessDenyException.class)
    public Response handleAccessDenyException(AccessDenyException e){
        return Response.builder()
                .errorResponse(ErrorResponse.builder()
                        .message(e.getMessage())
                        .errorCode(e.getMessageCode())
                        .build())
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public Response handleAuthenticationException(AuthenticationException e){
        return Response.builder()
                .errorResponse(ErrorResponse.builder()
                        .message(e.getMessage())
                        .build())
                .build();
    }

}