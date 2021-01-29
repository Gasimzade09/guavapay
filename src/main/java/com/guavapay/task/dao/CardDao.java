package com.guavapay.task.dao;

import com.guavapay.task.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.smartcardio.Card;

public interface CardDao extends JpaRepository<CardEntity, Integer> {
    CardEntity getByCardNumber(String cardNumber);

    CardEntity getByAccountNumber(String accountNumber);
}
