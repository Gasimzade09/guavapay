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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.guavapay.task.util.Util.generateAccountNumber;
import static com.guavapay.task.util.Util.generateCardNumber;

@Service
public class GuavaService {
    private final CardTypeDao typeDao;
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final CardDao cardDao;

    public GuavaService(CardTypeDao typeDao, UserDao userDao, OrderDao orderDao, CardDao cardDao) {
        this.typeDao = typeDao;
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.cardDao = cardDao;
    }

    public List<CardType> getAllCardTypes() {
        return typeDao.findAll();
    }

    public OrderResponse addOrder(OrderRequest orderRequest, String username) {
        UserEntity userEntity = userDao.getByUsername(username);
        CardType cardType = typeDao.getOne(orderRequest.getCardTypeId());
        CardEntity cardEntity = CardEntity.builder()
                .cardHolderName(orderRequest.getCardHolderName())
                .cardType(cardType)
                .cardPeriod(orderRequest.getCardPeriod())
                .urgent(orderRequest.getUrgent())
                .codeWord(orderRequest.getCodeWord())
                .build();
        OrderEntity orderEntity = OrderEntity.builder()
                .cardType(cardType)
                .creationTime(LocalDateTime.now())
                .user(userEntity)
                .status("Not submited")
                .card(cardEntity)
                .build();

        orderEntity = orderDao.save(orderEntity);

        return OrderResponse.builder()
                .orderCreationTime(orderEntity.getCreationTime())
                .orderId(orderEntity.getId().toString())
                .orderStatus(orderEntity.getStatus())
                .build();
    }

    public SubmitResponse submitOrder(Integer orderId, String username) {
        OrderEntity orderEntity = orderDao.getOne(orderId);
        if (!orderEntity.getUser().getUsername().equals(username)){
            throw new AccessDenyException("You can submit only yours orders", "10401");
        }if (orderEntity.getStatus().equals("Submitted")){
            throw new AccessDenyException("This order is already submitted", "10402");
        }
        String cardNumber = generateCardNumber();
        String accountNumber = generateAccountNumber();
        boolean status = true;
        while (status){
            if (cardDao.getByCardNumber(cardNumber) != null){
                cardNumber = generateCardNumber();
            }if (cardDao.getByAccountNumber(accountNumber) != null){
                accountNumber = generateAccountNumber();
            }else {
                status = false;
            }
        }


        orderEntity.setStatus("Submitted");
        CardEntity cardEntity = orderEntity.getCard();
        cardEntity.setCardNumber(cardNumber);
        cardEntity.setAccountNumber(accountNumber);
        cardDao.save(cardEntity);

        return SubmitResponse.builder()
                .accountNumber(accountNumber)
                .cardNumber(cardNumber)
                .build();
    }

    public List<OrderResponse> getOrders(String username) {
        List<OrderEntity> orderEntities = userDao.getByUsername(username).getOrderEntities();
        List<OrderResponse> responses = new ArrayList<>();
        for (OrderEntity e : orderEntities) {
            OrderResponse response = OrderResponse.builder()
                    .orderStatus(e.getStatus())
                    .orderId(e.getId().toString())
                    .orderCreationTime(e.getCreationTime())
                    .build();
            responses.add(response);
        }
        return responses;
    }

    public OrderResponse updateOrder(OrderRequest orderRequest, Integer orderId, String username) {
        OrderEntity order = orderDao.getOne(orderId);
        CardEntity card = order.getCard();
        if (order.getStatus().equals("Submitted")){
            throw new AccessDenyException("You can't update this order!", "10402");
        }else if (!(order.getUser().getUsername().equals(username))){
            throw new AccessDenyException("You can't update this order!", "10401");
        }
        order.setCardType(typeDao.getOne(orderRequest.getCardTypeId()));
        card.setCardHolderName(orderRequest.getCardHolderName());
        card.setCardPeriod(orderRequest.getCardPeriod());
        card.setCodeWord(orderRequest.getCodeWord());
        card.setUrgent(orderRequest.getUrgent());
        cardDao.save(card);
        orderDao.save(order);
        return OrderResponse.builder()
                .orderId(orderId.toString())
                .orderStatus(order.getStatus())
                .orderCreationTime(order.getCreationTime())
                .build();
    }

    public OrderDetailsResponse getOrderDetails(String username, Integer orderId) {
        OrderEntity order = orderDao.getOne(orderId);
        if (!order.getUser().getUsername().equals(username)){
            throw new AccessDenyException("You have no permission to view this order", "10401");
        }

        return OrderDetailsResponse.builder()
                .cardHolderName(order.getCard().getCardHolderName())
                .cardType(order.getCardType().getName())
                .cardPeriod(order.getCard().getCardPeriod())
                .codeword(order.getCard().getCodeWord())
                .urgent(order.getCard().getUrgent())
                .build();
    }
}
