package com.guavapay.task.controller;

import com.guavapay.task.dto.*;
import com.guavapay.task.security.util.SecurityUtil;
import com.guavapay.task.service.GuavaService;
import org.springframework.web.bind.annotation.*;

import static com.guavapay.task.util.Endpoints.*;

@RestController
public class GuavaController {
    private final GuavaService guavaService;

    public GuavaController(GuavaService guavaService) {
        this.guavaService = guavaService;
    }

    @GetMapping(path = GET_CARD_TYPES)
    public Response getCardTypes(){
        return guavaService.getAllCardTypes();
    }

    @PostMapping(path = CREATE_ORDER)
    public Response addOrder(@RequestBody OrderRequest orderRequest){
        String username = SecurityUtil.getCurrentUserLogin();
        return guavaService.addOrder(orderRequest, username);
    }

    @PostMapping(path = SUBMIT)
    public Response submit(@PathVariable Integer orderId){
        String username = SecurityUtil.getCurrentUserLogin();
        return guavaService.submitOrder(orderId, username);
    }

    @GetMapping(path = GET_ORDERS)
    public Response getOrders(){
        String username = SecurityUtil.getCurrentUserLogin();
        return guavaService.getOrders(username);
    }

    @PostMapping(path = UPDATE)
    public Response updateOrder(@RequestBody OrderRequest orderRequest,
                                @PathVariable Integer orderId){
        String username = SecurityUtil.getCurrentUserLogin();
        return guavaService.updateOrder(orderRequest, orderId, username);
    }

    @GetMapping(path = GET_ORDER_DETAILS)
    public Response getOrderDetails(@PathVariable Integer orderId){
        String username = SecurityUtil.getCurrentUserLogin();
        return guavaService.getOrderDetails(username, orderId);
    }
}
