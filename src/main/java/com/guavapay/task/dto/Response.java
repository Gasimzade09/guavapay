package com.guavapay.task.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.guavapay.task.entity.CardType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class Response {
    private ErrorResponse errorResponse;
    private OrderDetailsResponse orderDetailsResponse;
    private OrderResponse orderResponse;
    private SubmitResponse submitResponse;
    private UserDto userDto;
    private List<CardTypeDto> cardTypes;
    private List<OrderResponse> orderResponses;
}
