package com.guavapay.task.security.service;

import com.guavapay.task.config.GuavaConfig;
import com.guavapay.task.dao.UserDao;
import com.guavapay.task.dto.UserDto;
import com.guavapay.task.entity.UserEntity;
import com.guavapay.task.security.exceptions.AuthenticationException;
import com.guavapay.task.security.model.dto.JwtAuthenticationRequest;
import com.guavapay.task.security.model.dto.JwtAuthenticationResponse;
import com.guavapay.task.security.util.TokenUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {AuthenticationService.class, GuavaConfig.class})
@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService service;

    @MockBean
    private UserDao userDao;
    @MockBean
    private TokenUtils tokenUtils;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private BCryptPasswordEncoder encoder;


    @Test
    void testSignUp() {
        UserDto userDto = UserDto.builder()
                .username("alishka1991")
                .password("123456789")
                .build();
        String password = "blablabla";

        Mockito.doReturn(null)
                .when(userDao)
                .getByUsername(userDto.getUsername());

//        Mockito.doReturn(password)
//                .when(encoder)
//                .encode(userDto.getPassword());


        UserDto actual = service.signUp(userDto);

        assertNotNull(actual);
        assertEquals(userDto.getUsername(), actual.getUsername());
        assertEquals(userDto.getPassword(), actual.getPassword());

        Mockito.verify(userDao, Mockito.times(1))
                .getByUsername(userDto.getUsername());

        Mockito.verify(userDao, Mockito.times(1))
                .save(Mockito.any(UserEntity.class));

        Mockito.verify(encoder, Mockito.times(1))
                .encode(userDto.getPassword());
    }

    @Test
    void testSignUp_throwException(){
        UserDto userDto = UserDto.builder()
                .username("alishka1991")
                .password("123456789")
                .build();

        Mockito.doReturn(UserEntity.builder().build())
                .when(userDao)
                .getByUsername(userDto.getUsername());

        assertThrows(AuthenticationException.class, () -> service.signUp(userDto));

        Mockito.verify(userDao, Mockito.times(1))
                .getByUsername(userDto.getUsername());

        Mockito.verify(userDao, Mockito.times(0))
                .save(Mockito.any());

        Mockito.verify(encoder, Mockito.times(0))
                .encode(userDto.getPassword());
    }

    @Test
    void testCreateAuthenticationToken(){
        JwtAuthenticationRequest request = JwtAuthenticationRequest.builder()
                .username("alishka1991")
                .password("123456789")
                .build();
        String token = "Tokentokentokentoken";

        Mockito.doReturn(token)
                .when(tokenUtils)
                .generateToken(request.getUsername());

        JwtAuthenticationResponse actual = service.createAuthenticationToken(request);

        assertNotNull(actual);
        assertEquals(token, actual.getToken());

        Mockito.verify(tokenUtils, Mockito.times(1)).generateToken(request.getUsername());
    }
}