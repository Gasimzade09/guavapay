package com.guavapay.task.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailsResponse {
    private String cardType;
    private String cardHolderName;
    private Integer cardPeriod;
    private Boolean urgent;
    private String codeword;
}
