package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.dto.SignupRequestDto;
import com.lawmon.lawmon.dto.LoginRequestDto;
import com.lawmon.lawmon.dto.UserResponseDto;
import com.lawmon.lawmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody SignupRequestDto request) {
        UserResponseDto response = userService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto request) {
        UserResponseDto response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
