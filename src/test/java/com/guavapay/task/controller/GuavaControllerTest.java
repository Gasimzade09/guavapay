package com.guavapay.task.controller;

import com.guavapay.task.dto.OrderDetailsResponse;
import com.guavapay.task.dto.OrderRequest;
import com.guavapay.task.dto.OrderResponse;
import com.guavapay.task.dto.SubmitResponse;
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
        List<CardType> cardTypes = new ArrayList<>();
        cardTypes.add(CardType.builder()
                .id(1)
                .name("Visa")
                .build());
        Mockito.doReturn(cardTypes)
                .when(guavaService)
                .getAllCardTypes();

        List<CardType> cardTypeList = guavaController.getCardTypes();

        assertNotNull(cardTypeList);
        assertEquals(cardTypes, cardTypeList);
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
        OrderResponse expected = OrderResponse.builder()
                .orderCreationTime(LocalDateTime.now())
                .orderId("789556")
                .orderStatus("Not submitted")
                .build();

        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUserLogin).thenReturn("alishka1991");

        Mockito.doReturn(expected)
                .when(guavaService)
                .addOrder(request, "alishka1991");

        OrderResponse actual = guavaController.addOrder(request);
        assertNotNull(actual);
        assertEquals(expected, actual);

        Mockito.verify(guavaService, Mockito.times(1))
                .addOrder(request, "alishka1991");
        mockedStatic.verify(times(1), SecurityUtil::getCurrentUserLogin);
    }

    @Test
    void testSubmit(){
        Integer orderId = 5;
        String username = "alishka1991";
        SubmitResponse expected = SubmitResponse.builder()
                .cardNumber("123456789025478")
                .accountNumber("A3r%de28pPiRt5")
                .build();

        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUserLogin).thenReturn(username);

        Mockito.doReturn(expected)
                .when(guavaService)
                .submitOrder(orderId, username);

        SubmitResponse actual = guavaController.submit(orderId);

        assertNotNull(actual);
        assertEquals(expected, actual);

        Mockito.verify(guavaService, Mockito.times(1))
                .submitOrder(orderId, username);
        mockedStatic.verify(times(1), SecurityUtil::getCurrentUserLogin);
    }

    @Test
    void testGetOrders(){
        String username = "alishka1991";
        List<OrderResponse> expected = new ArrayList<>();
        expected.add(OrderResponse.builder()
                .orderStatus("Not submitted")
                .orderCreationTime(LocalDateTime.now())
                .orderId("7")
                .build());
        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUserLogin).thenReturn(username);

        Mockito.doReturn(expected)
                .when(guavaService)
                .getOrders(username);

        List<OrderResponse> actual = guavaController.getOrders();

        assertNotNull(actual);
        assertNotNull(actual.get(0));
        assertEquals(expected, actual);

        Mockito.verify(guavaService, Mockito.times(1))
                .getOrders(username);
        mockedStatic.verify(times(1), SecurityUtil::getCurrentUserLogin);
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

        OrderResponse expected = OrderResponse.builder()
                .orderId(orderId.toString())
                .orderCreationTime(LocalDateTime.now().minusHours(2L))
                .orderStatus("Not submitted")
                .build();

        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUserLogin).thenReturn(username);

        Mockito.doReturn(expected)
                .when(guavaService)
                .updateOrder(request, orderId, username);

        OrderResponse actual = guavaController.updateOrder(request,orderId);

        assertNotNull(actual);
        assertEquals(expected, actual);

        Mockito.verify(guavaService, Mockito.times(1))
                .updateOrder(request, orderId, username);
        mockedStatic.verify(times(1), SecurityUtil::getCurrentUserLogin);
    }

    @Test
    void testGetOrderDetails(){
        Integer orderId = 5;
        String username = "alishka1991";
        OrderDetailsResponse expected = OrderDetailsResponse.builder()
                .cardType("MasterCard")
                .urgent(false)
                .codeword("Secret")
                .cardHolderName("Ali Gasimzade")
                .cardPeriod(12)
                .build();

        MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class);
        mockedStatic.when(SecurityUtil::getCurrentUserLogin).thenReturn(username);

        Mockito.doReturn(expected)
                .when(guavaService)
                .getOrderDetails(username, orderId);

        OrderDetailsResponse actual = guavaController.getOrderDetails(orderId);

        assertNotNull(actual);
        assertEquals(expected, actual);

        Mockito.verify(guavaService, Mockito.times(1))
                .getOrderDetails(username, orderId);
        mockedStatic.verify(times(1), SecurityUtil::getCurrentUserLogin);
    }
}