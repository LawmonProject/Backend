package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.dto.LoginRequestDto;
import com.lawmon.lawmon.dto.LoginResponseDto;
import com.lawmon.lawmon.dto.SignupRequestDto;
import com.lawmon.lawmon.security.JwtUtil;
import com.lawmon.lawmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto request) {
        userService.signup(request);
        return ResponseEntity.ok("회원가입 성공!");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        LoginResponseDto response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}