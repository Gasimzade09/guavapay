package com.guavapay.task.security.service;


import com.guavapay.task.dao.UserDao;
import com.guavapay.task.entity.UserEntity;
import com.guavapay.task.security.model.Role;
import com.guavapay.task.security.model.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SecurityUserService
        implements UserDetailsService {

    private final UserDao userDao;

    public SecurityUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(
            String email) throws UsernameNotFoundException {

        UserEntity user = userDao.getByUsername(email);

        return buildSecurityUser(user);
    }

    private SecurityUser buildSecurityUser(UserEntity user) {
        return SecurityUser.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.singletonList(toSecurityRole(user.getRole())))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true).build();
    }

    private Role toSecurityRole(String role) {
        Role enumRole = null;
        try {
            enumRole = enumRole.valueOf(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enumRole;

    }


}