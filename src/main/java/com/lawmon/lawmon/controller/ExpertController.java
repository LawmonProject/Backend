package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.dto.ExpertProfileDto;
import com.lawmon.lawmon.dto.SignupRequestDto;
import com.lawmon.lawmon.dto.UserResponseDto;
import com.lawmon.lawmon.service.ExpertService;
import com.lawmon.lawmon.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/experts")
@RequiredArgsConstructor
public class ExpertController {
    private final ExpertService expertService;
    private final UserService userService;

    @GetMapping("/email/{email}")
    public ResponseEntity<ExpertProfileDto> getExpertProfile(@PathVariable String email) {
        ExpertProfileDto profile = expertService.getExpertProfile(email);
        return profile != null ? ResponseEntity.ok(profile) : ResponseEntity.notFound().build();
    }

    @PutMapping("/email/{email}")
    public ResponseEntity<ExpertProfileDto> updateExpertProfile(@PathVariable String email,
                                                                @RequestBody ExpertProfileDto dto) {
        ExpertProfileDto updatedProfile = expertService.updateExpertProfile(email, dto);
        return updatedProfile != null ? ResponseEntity.ok(updatedProfile) : ResponseEntity.notFound().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody SignupRequestDto requestDto) {
        UserResponseDto userResponse = userService.signup(requestDto);
        return ResponseEntity.ok(userResponse);
    }
}
