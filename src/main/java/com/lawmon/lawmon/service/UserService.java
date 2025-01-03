package com.lawmon.lawmon.service;

import com.lawmon.lawmon.Entity.Member;
import com.lawmon.lawmon.dto.SignupRequestDto;
import com.lawmon.lawmon.dto.LoginRequestDto;
import com.lawmon.lawmon.dto.UserResponseDto;
import com.lawmon.lawmon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto signup(SignupRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .profileImage(request.getProfileImage())
                .password(request.getPassword()) // 비밀번호 암호화 생략
                .specialty(Member.Specialty.valueOf(request.getSpecialty().toUpperCase())) // USER 또는 EXPERT
                .build();

        userRepository.save(member);

        return UserResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .specialty(member.getSpecialty().name())
                .build();
    }

    public UserResponseDto login(LoginRequestDto request) {
        Member member = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!member.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return UserResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .specialty(member.getSpecialty().name())
                .build();
    }
}
