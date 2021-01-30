package com.guavapay.task.service;

import com.guavapay.task.dao.CardDao;
import com.guavapay.task.dao.CardTypeDao;
import com.guavapay.task.dao.OrderDao;
import com.guavapay.task.dao.UserDao;
import com.guavapay.task.dto.OrderDetailsResponse;
import com.guavapay.task.dto.OrderRequest;
import com.guavapay.task.dto.OrderResponse;
import com.guavapay.task.dto.SubmitResponse;
import com.guavapay.task.entity.CardEntity;
import com.guavapay.task.entity.CardType;
import com.guavapay.task.entity.OrderEntity;
import com.guavapay.task.entity.UserEntity;
import com.guavapay.task.exception.AccessDenyException;
import com.guavapay.task.util.Util;
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

@ContextConfiguration(classes = GuavaService.class)
@ExtendWith(SpringExtension.class)
class GuavaServiceTest {
    @Autowired
    private GuavaService guavaService;

    @MockBean
    private CardTypeDao typeDao;
    @MockBean
    private UserDao userDao;
    @MockBean
    private OrderDao orderDao;
    @MockBean
    private CardDao cardDao;

    @Test
    void testGetAllCardTypes(){
        List<CardType> expected = new ArrayList<>();
        expected.add(CardType.builder()
                .id(1)
                .name("Visa")
                .build());

        Mockito.doReturn(expected)
                .when(typeDao)
                .findAll();

        List<CardType> actual = guavaService.getAllCardTypes();

        assertNotNull(actual);
        assertNotNull(actual.get(0));
        assertEquals(expected.size(), actual.size());
        assertEquals(expected, actual);
        assertEquals(expected.get(0), actual.get(0));

    }

    @Test
    void testAddOrder(){
        String username = "alishka1991";
        OrderRequest request = OrderRequest.builder()
                .cardHolderName("Ali Gasimzade")
                .cardPeriod(24)
                .urgent(false)
                .codeWord("secret word")
                .cardTypeId(1)
                .build();

        CardType cardType = CardType.builder()
                .name("Visa")
                .id(1)
                .build();

        CardEntity cardEntity = CardEntity.builder()
                .cardHolderName(request.getCardHolderName())
                .cardType(cardType)
                .cardPeriod(request.getCardPeriod())
                .urgent(request.getUrgent())
                .codeWord(request.getCodeWord())
                .build();

        UserEntity user = UserEntity.builder()
                .role("ROLE_USER")
                .username(username)
                .id(1)
                .password("blablabla")
                .build();


        OrderEntity orderEntity = OrderEntity.builder()
                .id(1)
                .cardType(cardType)
                .creationTime(LocalDateTime.now())
                .user(user)
                .status("Not submitted")
                .card(cardEntity)
                .build();

        Mockito.doReturn(user)
                .when(userDao)
                .getByUsername(username);

        Mockito.doReturn(cardType)
                .when(typeDao)
                .getOne(1);

        Mockito.doReturn(orderEntity)
                .when(orderDao)
                .save(Mockito.any(OrderEntity.class));

        OrderResponse actual = guavaService.addOrder(request, username);
        assertNotNull(actual);

        Mockito.verify(userDao, Mockito.times(1))
                .getByUsername(username);

        Mockito.verify(typeDao, Mockito.times(1))
                .getOne(request.getCardTypeId());

        Mockito.verify(orderDao, Mockito.times(1))
                .save(Mockito.any(OrderEntity.class));
    }

    @Test
    void testSubmitOrder(){
        String username = "alishka1991";
        Integer orderId = 1;
        String accountNumber = "201e5C98A2u125";
        String cardNumber = "5142598756125400";
        OrderEntity orderEntity = OrderEntity.builder()
                .id(1)
                .creationTime(LocalDateTime.now())
                .status("Not submitted")
                .card(CardEntity.builder().build())
                .user(UserEntity.builder().username(username).build())
                .build();

        Mockito.doReturn(orderEntity)
                .when(orderDao)
                .getOne(orderId);

        Mockito.doReturn(null)
                .when(cardDao)
                .getByAccountNumber(accountNumber);

        Mockito.doReturn(null)
                .when(cardDao)
                .getByCardNumber(cardNumber);

        MockedStatic<Util> mockedStatic = mockStatic(Util.class);
        mockedStatic.when(Util::generateAccountNumber).thenReturn(accountNumber);
        mockedStatic.when(Util::generateCardNumber).thenReturn(cardNumber);

        SubmitResponse actual = guavaService.submitOrder(orderId, username);
        assertNotNull(actual);
        assertEquals(cardNumber, actual.getCardNumber());
        assertEquals(accountNumber, actual.getAccountNumber());

        Mockito.verify(orderDao, Mockito.times(1)).getOne(orderId);
        Mockito.verify(cardDao, Mockito.times(1)).getByCardNumber(cardNumber);
        Mockito.verify(cardDao, Mockito.times(1)).getByAccountNumber(accountNumber);
        Mockito.verify(cardDao, Mockito.times(1)).save(Mockito.any(CardEntity.class));
        mockedStatic.verify(times(1), Util::generateAccountNumber);
        mockedStatic.verify(times(1), Util::generateCardNumber);
    }

    @Test
    void testSubmitOrder_submitted(){
        String username = "alishka1991";
        Integer orderId = 1;

        OrderEntity orderEntity = OrderEntity.builder()
                .id(1)
                .creationTime(LocalDateTime.now())
                .status("Submitted")
                .card(CardEntity.builder().build())
                .user(UserEntity.builder().username(username).build())
                .build();

        Mockito.doReturn(orderEntity)
                .when(orderDao)
                .getOne(orderId);



        assertThrows(AccessDenyException.class, () -> guavaService.submitOrder(orderId, username));
    }

    @Test
    void testSubmitOrder_accessDeny(){
        String username = "alishka1991";
        Integer orderId = 1;

        OrderEntity orderEntity = OrderEntity.builder()
                .id(1)
                .creationTime(LocalDateTime.now())
                .status("Submitted")
                .card(CardEntity.builder().build())
                .user(UserEntity.builder().username("username").build())
                .build();

        Mockito.doReturn(orderEntity)
                .when(orderDao)
                .getOne(orderId);

        assertThrows(AccessDenyException.class, () -> guavaService.submitOrder(orderId, username));
        Mockito.verify(cardDao, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(cardDao, Mockito.times(0)).getByCardNumber(Mockito.any());
        Mockito.verify(cardDao, Mockito.times(0)).getByAccountNumber(Mockito.any());
    }

    @Test
    void testGetOrders(){
        String username = "alishka1991";
        List<OrderEntity> orderEntities = new ArrayList<>();
        orderEntities.add(OrderEntity.builder()
                .id(1)
                .status("Not submitted")
                .creationTime(LocalDateTime.now().minusHours(5L))
                .build());
        UserEntity user = UserEntity.builder()
                .username(username)
                .id(1)
                .orderEntities(orderEntities)
                .build();

        Mockito.doReturn(user)
                .when(userDao)
                .getByUsername(username);

        List<OrderResponse> actual = guavaService.getOrders(username);

        assertNotNull(actual);
        assertNotNull(actual.get(0));
        assertEquals(1, actual.size());

        Mockito.verify(userDao, Mockito.times(1)).getByUsername(username);
    }

    @Test
    void testUpdateOrder(){
        Integer orderId = 1;
        String username = "alishka1991";

        OrderRequest request = OrderRequest.builder()
                .codeWord("New secret")
                .urgent(true)
                .cardTypeId(1)
                .cardHolderName("Gulabatin Gambargulieva")
                .cardPeriod(12)
                .build();

        OrderEntity orderEntity = OrderEntity.builder()
                .user(UserEntity.builder().username(username).build())
                .status("Not submitted")
                .card(CardEntity.builder().build())
                .creationTime(LocalDateTime.now())
                .build();

        CardType cardType = CardType.builder()
                .id(request.getCardTypeId())
                .name("Visa")
                .build();

        Mockito.doReturn(orderEntity)
                .when(orderDao)
                .getOne(orderId);

        Mockito.doReturn(cardType)
                .when(typeDao)
                .getOne(request.getCardTypeId());

        OrderResponse actual = guavaService.updateOrder(request, orderId, username);

        assertNotNull(actual);

        Mockito.verify(orderDao, Mockito.times(1)).save(Mockito.any(OrderEntity.class));
        Mockito.verify(cardDao, Mockito.times(1)).save(Mockito.any(CardEntity.class));
        Mockito.verify(orderDao, Mockito.times(1)).getOne(orderId);
    }

    @Test
    void testUpdateOrder_accessDenySubmitted(){
        Integer orderId = 1;
        String username = "alishka1991";

        OrderRequest request = OrderRequest.builder()
                .codeWord("New secret")
                .urgent(true)
                .cardTypeId(1)
                .cardHolderName("Gulabatin Gambargulieva")
                .cardPeriod(12)
                .build();
        OrderEntity orderEntity = OrderEntity.builder()
                .user(UserEntity.builder().username(username).build())
                .status("Submitted")
                .card(CardEntity.builder().build())
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.doReturn(orderEntity)
                .when(orderDao)
                .getOne(orderId);

        assertThrows(AccessDenyException.class, () -> guavaService.updateOrder(request, orderId, username));
        Mockito.verify(orderDao, Mockito.times(0)).save(Mockito.any(OrderEntity.class));
        Mockito.verify(cardDao, Mockito.times(0)).save(Mockito.any(CardEntity.class));
        Mockito.verify(orderDao, Mockito.times(1)).getOne(orderId);
    }

    @Test
    void testUpdateOrder_accessDenyForUser(){
        Integer orderId = 1;
        String username = "alishka1991";

        OrderRequest request = OrderRequest.builder()
                .codeWord("New secret")
                .urgent(true)
                .cardTypeId(1)
                .cardHolderName("Gulabatin Gambargulieva")
                .cardPeriod(12)
                .build();
        OrderEntity orderEntity = OrderEntity.builder()
                .user(UserEntity.builder().username("username").build())
                .status("Not submitted")
                .card(CardEntity.builder().build())
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.doReturn(orderEntity)
                .when(orderDao)
                .getOne(orderId);

        assertThrows(AccessDenyException.class, () -> guavaService.updateOrder(request, orderId, username));
        Mockito.verify(orderDao, Mockito.times(0)).save(Mockito.any(OrderEntity.class));
        Mockito.verify(cardDao, Mockito.times(0)).save(Mockito.any(CardEntity.class));
        Mockito.verify(orderDao, Mockito.times(1)).getOne(orderId);
    }

    @Test
    void testGetOrderDetails(){
        String username = "alishka1991";
        Integer orderId = 1;
        CardEntity card = CardEntity.builder()
                .cardPeriod(12)
                .codeWord("Secret")
                .cardHolderName("Gulebatin Ismixanova")
                .urgent(true).build();
        CardType type = CardType.builder().name("Visa").id(1).build();
        OrderEntity orderEntity = OrderEntity.builder()
                .user(UserEntity.builder().username(username).build())
                .status("Not submitted")
                .card(card)
                .cardType(type)
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.doReturn(orderEntity)
                .when(orderDao)
                .getOne(orderId);

        OrderDetailsResponse actual = guavaService.getOrderDetails(username, orderId);

        assertNotNull(actual);
        assertEquals(type.getName(), actual.getCardType());
        assertEquals(card.getCodeWord(), actual.getCodeword());
        assertEquals(card.getCardPeriod(), actual.getCardPeriod());
        assertEquals(card.getCardHolderName(), actual.getCardHolderName());
        assertEquals(card.getUrgent(), actual.getUrgent());

        Mockito.verify(orderDao, Mockito.times(1)).getOne(orderId);
    }

    @Test
    void testGetOrderDetails_accessDeny(){
        String username = "alishka1991";
        Integer orderId = 1;

        CardType type = CardType.builder().name("Visa").id(1).build();
        OrderEntity orderEntity = OrderEntity.builder()
                .user(UserEntity.builder().username("username").build())
                .status("Not submitted")
                .creationTime(LocalDateTime.now())
                .build();

        Mockito.doReturn(orderEntity)
                .when(orderDao)
                .getOne(orderId);

        assertThrows(AccessDenyException.class, () -> guavaService.getOrderDetails(username, orderId));
    }
}