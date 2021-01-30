package com.guavapay.task.security.controller;


import com.guavapay.task.dto.UserDto;
import com.guavapay.task.security.model.dto.JwtAuthenticationRequest;
import com.guavapay.task.security.model.dto.JwtAuthenticationResponse;
import com.guavapay.task.security.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class AuthenticationController {

    private final AuthenticationService service;


    public AuthenticationController(AuthenticationService service) {
        this.service = service;

    }

    @PostMapping("/auth")
    public JwtAuthenticationResponse signIn(@RequestBody JwtAuthenticationRequest request) {
        return service.createAuthenticationToken(request);
    }


    @PostMapping("/reg")
    public void reg(@RequestBody UserDto user){
        service.signUp(user);
    }
}