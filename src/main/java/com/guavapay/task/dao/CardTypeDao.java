package com.guavapay.task.dao;

import com.guavapay.task.entity.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTypeDao extends JpaRepository<CardType, Integer> {
}
