package com.guavapay.task.controller;

import com.guavapay.task.dto.OrderDetailsResponse;
import com.guavapay.task.dto.OrderRequest;
import com.guavapay.task.dto.OrderResponse;
import com.guavapay.task.dto.SubmitResponse;
import com.guavapay.task.entity.CardType;
import com.guavapay.task.security.util.SecurityUtil;
import com.guavapay.task.service.GuavaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.guavapay.task.util.Endpoints.*;

@RestController
public class GuavaController {
    private final GuavaService guavaService;

    public GuavaController(GuavaService guavaService) {
        this.guavaService = guavaService;
    }

    @GetMapping(path = GET_CARD_TYPES)
    public List<CardType> getCardTypes(){
        return guavaService.getAllCardTypes();
    }

    @PostMapping(path = CREATE_ORDER)
    public OrderResponse addOrder(@RequestBody OrderRequest orderRequest){
        String username = SecurityUtil.getCurrentUserLogin();
        return guavaService.addOrder(orderRequest, username);
    }

    @PostMapping(path = SUBMIT)
    public SubmitResponse submit(@PathVariable Integer orderId){
        String username = SecurityUtil.getCurrentUserLogin();
        return guavaService.submitOrder(orderId, username);
    }

    @GetMapping(path = GET_ORDERS)
    public List<OrderResponse> getOrders(){
        String username = SecurityUtil.getCurrentUserLogin();
        return guavaService.getOrders(username);
    }

    @PostMapping(path = UPDATE)
    public OrderResponse updateOrder(@RequestBody OrderRequest orderRequest,
                                     @PathVariable Integer orderId){
        String username = SecurityUtil.getCurrentUserLogin();
        return guavaService.updateOrder(orderRequest, orderId, username);
    }

    @GetMapping(path = GET_ORDER_DETAILS)
    public OrderDetailsResponse getOrderDetails(@PathVariable Integer orderId){
        String username = SecurityUtil.getCurrentUserLogin();
        return guavaService.getOrderDetails(username, orderId);
    }
}
