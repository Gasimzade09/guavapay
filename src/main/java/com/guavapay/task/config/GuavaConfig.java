package com.guavapay.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class GuavaConfig {

    @Bean
    public BCryptPasswordEncoder commonPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
