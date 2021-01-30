package com.guavapay.task.security.service;


import com.guavapay.task.dao.UserDao;
import com.guavapay.task.dto.UserDto;
import com.guavapay.task.entity.UserEntity;
import com.guavapay.task.security.exceptions.AuthenticationException;
import com.guavapay.task.security.model.Role;
import com.guavapay.task.security.model.dto.JwtAuthenticationRequest;
import com.guavapay.task.security.model.dto.JwtAuthenticationResponse;
import com.guavapay.task.security.util.TokenUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthenticationService {

    private final TokenUtils tokenUtils;

    private final BCryptPasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final UserDao userDao;

    public AuthenticationService(TokenUtils tokenUtils,
                                 AuthenticationManager authenticationManager,
                                 UserDao userDao,
                                 @Qualifier("commonPasswordEncoder") BCryptPasswordEncoder encoder) {
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.encoder = encoder;
    }


    public JwtAuthenticationResponse createAuthenticationToken(JwtAuthenticationRequest request) {
        authenticate(request.getUsername(), request.getPassword());
        String token = tokenUtils.generateToken(request.getUsername());
        return new JwtAuthenticationResponse(token);
    }

    public void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials", e);
        }
    }



    public UserEntity signUp(UserDto user){
        UserEntity checkUserName = userDao.getByUsername(user.getUsername());
        if (checkUserName == null){
            String password = encoder.encode(user.getPassword());
            UserEntity userEntity = UserEntity.builder()
                    .username(user.getUsername())
                    .password(password)
                    .role(Role.ROLE_USER.toString())
                    .build();

            userDao.save(userEntity);
            return userEntity;
        } else {
            throw new AuthenticationException("This user is already exists");
        }
    }
}
