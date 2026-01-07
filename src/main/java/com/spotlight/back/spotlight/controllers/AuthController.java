package com.spotlight.back.spotlight.controllers;

import com.spotlight.back.spotlight.models.dtos.UserLoginDto;
import com.spotlight.back.spotlight.models.dtos.UserRegisterDto;
import com.spotlight.back.spotlight.models.dtos.UserResponceDto;
import com.spotlight.back.spotlight.services.UserService;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User created successfully",content = @Content(schema = @Schema(implementation = UserResponceDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public UserResponceDto register(@RequestBody UserRegisterDto dto) {
        return userService.register(dto);
    }

    @Operation(summary = "Login a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User logged in successfully",content = @Content(schema = @Schema(implementation = UserResponceDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public UserResponceDto login(@RequestBody UserLoginDto dto) {
        return userService.login(dto);
    }
}
