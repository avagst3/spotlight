package com.spotlight.back.spotlight.controllers;

import com.spotlight.back.spotlight.models.dtos.UserLoginDto;
import com.spotlight.back.spotlight.models.dtos.UserRegisterDto;
import com.spotlight.back.spotlight.models.entities.User;
import com.spotlight.back.spotlight.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody UserRegisterDto dto) {
        return userService.register(dto);
    }

    @PostMapping("/login")
    public User login(@RequestBody UserLoginDto dto) {
        User user = userService.login(dto);
        if (user == null)
            throw new RuntimeException("Invalid credentials");
        return user;
    }
}
