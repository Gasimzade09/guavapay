package com.guavapay.task.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CardTypeDto {
    private Integer id;

    private String name;
}
