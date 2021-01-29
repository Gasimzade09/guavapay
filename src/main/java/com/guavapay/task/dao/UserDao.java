package com.guavapay.task.dao;

import com.guavapay.task.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<UserEntity, Integer> {
    UserEntity getByUsername(String username);
}
