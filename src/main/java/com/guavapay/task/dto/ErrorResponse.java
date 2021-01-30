package com.guavapay.task.dto;

import lombok.Builder;

@Builder
public class ErrorResponse {
    private String message;
    private String errorCode;
}
