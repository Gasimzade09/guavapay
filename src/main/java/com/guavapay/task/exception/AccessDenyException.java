package com.guavapay.task.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccessDenyException extends RuntimeException{
    private String message;
    private String messageCode;
}
