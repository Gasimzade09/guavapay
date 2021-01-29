package com.guavapay.task.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmitResponse {
    private String cardNumber;
    private String accountNumber;
}
