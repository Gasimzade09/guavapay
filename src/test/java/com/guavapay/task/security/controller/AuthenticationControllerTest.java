package com.guavapay.task.security.controller;

import com.guavapay.task.dto.Response;
import com.guavapay.task.dto.UserDto;
import com.guavapay.task.security.model.dto.JwtAuthenticationRequest;
import com.guavapay.task.security.model.dto.JwtAuthenticationResponse;
import com.guavapay.task.security.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = AuthenticationController.class)
@ExtendWith(SpringExtension.class)
class AuthenticationControllerTest {
    @Autowired
    private AuthenticationController controller;
    @MockBean
    private AuthenticationService service;

    @Test
    void testSignIn() {
        JwtAuthenticationRequest request = JwtAuthenticationRequest.builder()
                .username("alishka1991")
                .password("123456789")
                .build();
        JwtAuthenticationResponse expected = JwtAuthenticationResponse.builder()
                .token("tokentokentokentoken")
                .build();

        Mockito.doReturn(expected)
                .when(service)
                .createAuthenticationToken(request);

        JwtAuthenticationResponse actual = controller.signIn(request);
        assertNotNull(actual);
        assertEquals(expected, actual);
        assertEquals(expected.getToken(), actual.getToken());

        Mockito.verify(service, Mockito.times(1)).createAuthenticationToken(request);

    }

    @Test
    void testReg() {
        UserDto userDto = UserDto.builder()
                .username("alishka1991")
                .password("123456789")
                .build();
        Response expected = Response.builder()
                .userDto(userDto)
                .build();

        Mockito.doReturn(expected)
                .when(service)
                .signUp(userDto);

        Response actual = controller.reg(userDto);

        assertNotNull(actual);
        assertNotNull(actual.getUserDto());
        assertNotNull(actual.getUserDto());

        Mockito.verify(service, Mockito.times(1)).signUp(userDto);
    }
}