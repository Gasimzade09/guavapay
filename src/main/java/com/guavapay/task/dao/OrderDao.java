package com.guavapay.task.dao;

import com.guavapay.task.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<OrderEntity, Integer> {
}
