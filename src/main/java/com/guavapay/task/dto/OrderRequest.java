package com.guavapay.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    private Integer cardTypeId;
    private String cardHolderName;
    private Integer cardPeriod;
    private Boolean urgent;
    private String codeWord;
}
