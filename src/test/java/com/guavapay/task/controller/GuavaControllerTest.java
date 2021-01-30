package com.guavapay.task.controller;

import com.guavapay.task.dto.*;
import com.guavapay.task.entity.CardType;
import com.guavapay.task.security.util.SecurityUtil;
import com.guavapay.task.service.GuavaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

@ContextConfiguration(classes = GuavaController.class)
@ExtendWith(SpringExtension.class)
class GuavaControllerTest {
    @Autowired
    private GuavaController guavaController;
    @MockBean
    private GuavaService guavaService;


    @Test
    void testGetCardTypes(){
        List<CardTypeDto> expected = new ArrayList<>();
        expected.add(CardTypeDto.builder()
                .id(1)
                .name("Visa")
                .build());

        Response response = Response.builder().cardTypes(expected).build();
        Mockito.doReturn(response)
                .when(guavaService)
                .getAllCardTypes();

        Response actual = guavaController.getCardTypes();


        assertNotNull(actual);
        assertNotNull(actual.getCardTypes());
        assertEquals(expected, actual.getCardTypes());

        Mockito.verify(guavaService, Mockito.times(1)).getAllCardTypes();

    }

    @Test
    void testAddOrder(){
        OrderRequest request = OrderRequest.builder()
                .cardHolderName("Ali Gasimzade")
                .cardPeriod(24)
                .urgent(false)
                .codeWord("secret word")
                .cardTypeId(1)
                .build();

        Response expected = Response.builder()
                .orderResponse(OrderResponse.builder()
                        .orderCreationTime(LocalDateTime.now())
                        .orderId("789556")
                        .orderStatus("Not submitted")
                        .build())
                .build();

        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUserLogin).thenReturn("alishka1991");


        Mockito.doReturn(expected)
                .when(guavaService)
                .addOrder(request, "alishka1991");

        Response actual = guavaController.addOrder(request);
        assertNotNull(actual);
        assertNotNull(actual.getOrderResponse());
        assertEquals(expected, actual);

        Mockito.verify(guavaService, Mockito.times(1))
                .addOrder(request, "alishka1991");
        mockedStatic.verify(times(1), SecurityUtil::getCurrentUserLogin);
        mockedStatic.close();
    }

    @Test
    void testSubmit(){
        Integer orderId = 5;
        String username = "alishka1991";
        Response expected = Response.builder()
                .submitResponse(SubmitResponse.builder()
                        .cardNumber("123456789025478")
                        .accountNumber("A3r%de28pPiRt5")
                        .build())
                .build();

        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUserLogin).thenReturn(username);


        Mockito.doReturn(expected)
                .when(guavaService)
                .submitOrder(orderId, username);

        Response actual = guavaController.submit(orderId);

        assertNotNull(actual);
        assertNotNull(actual.getSubmitResponse());
        assertEquals(expected, actual);

        Mockito.verify(guavaService, Mockito.times(1))
                .submitOrder(orderId, username);
        mockedStatic.verify(times(1), SecurityUtil::getCurrentUserLogin);
        mockedStatic.close();
    }

    @Test
    void testGetOrders(){
        String username = "alishka1991";
        List<OrderResponse> orderResponses = new ArrayList<>();
        orderResponses.add(OrderResponse.builder()
                .orderStatus("Not submitted")
                .orderCreationTime(LocalDateTime.now())
                .orderId("7")
                .build());
        Response expected = Response.builder().orderResponses(orderResponses).build();
        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUserLogin).thenReturn(username);


        Mockito.doReturn(expected)
                .when(guavaService)
                .getOrders(username);

        Response actual = guavaController.getOrders();

        assertNotNull(actual);
        assertNotNull(actual.getOrderResponses());
        assertNotNull(actual.getOrderResponses().get(0));
        assertEquals(expected, actual);

        Mockito.verify(guavaService, Mockito.times(1))
                .getOrders(username);
        mockedStatic.verify(times(1), SecurityUtil::getCurrentUserLogin);
        mockedStatic.close();
    }

    @Test
    void testUpdateOrder(){
        Integer orderId = 2;
        String username = "alishka1991";
        OrderRequest request = OrderRequest.builder()
                .cardTypeId(1)
                .cardPeriod(12)
                .cardHolderName("Ali Gasimzade")
                .urgent(false)
                .codeWord("Secret word")
                .build();

        Response expected = Response.builder()
                .orderResponse(OrderResponse.builder()
                        .orderId(orderId.toString())
                        .orderCreationTime(LocalDateTime.now().minusHours(2L))
                        .orderStatus("Not submitted")
                        .build())
                .build();

        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUserLogin).thenReturn(username);


        Mockito.doReturn(expected)
                .when(guavaService)
                .updateOrder(request, orderId, username);

        Response actual = guavaController.updateOrder(request,orderId);

        assertNotNull(actual);
        assertNotNull(actual.getOrderResponse());
        assertEquals(expected, actual);

        Mockito.verify(guavaService, Mockito.times(1))
                .updateOrder(request, orderId, username);
        mockedStatic.verify(times(1), SecurityUtil::getCurrentUserLogin);
        mockedStatic.close();
    }

    @Test
    void testGetOrderDetails(){
        Integer orderId = 5;
        String username = "alishka1991";
        Response expected = Response.builder()
                .orderDetailsResponse(OrderDetailsResponse.builder()
                        .cardType("MasterCard")
                        .urgent(false)
                        .codeword("Secret")
                        .cardHolderName("Ali Gasimzade")
                        .cardPeriod(12)
                        .build())
                .build();

        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUserLogin).thenReturn(username);


        Mockito.doReturn(expected)
                .when(guavaService)
                .getOrderDetails(username, orderId);

        Response actual = guavaController.getOrderDetails(orderId);

        assertNotNull(actual);
        assertNotNull(actual.getOrderDetailsResponse());
        assertEquals(expected, actual);

        Mockito.verify(guavaService, Mockito.times(1))
                .getOrderDetails(username, orderId);
        mockedStatic.verify(times(1), SecurityUtil::getCurrentUserLogin);
        mockedStatic.close();
    }
}