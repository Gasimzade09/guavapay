package com.guavapay.task.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private String orderId;
    private LocalDateTime orderCreationTime;
    private String orderStatus;
}
