package com.lawmon.lawmon.service;

import com.lawmon.lawmon.Entity.*;
import com.lawmon.lawmon.dto.LoginRequestDto;
import com.lawmon.lawmon.dto.SignupRequestDto;
import com.lawmon.lawmon.dto.UserResponseDto;
import com.lawmon.lawmon.repository.UserRepository;
import com.lawmon.lawmon.repository.ExpertRepository;
import com.lawmon.lawmon.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ExpertRepository expertRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResponseDto signup(SignupRequestDto request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        if (request.getRole() == Role.EXPERT) {
            Expert expert = new Expert();
            expert.setEmail(request.getEmail());
            expert.setPassword(encodedPassword);
            expert.setRole(Role.EXPERT);
            expert.setName(request.getName());
            expert.setSpecialty(request.getSpecialty());
            expert.setLicenseNumber(request.getLicenseNumber());
            expert.setCategory(request.getCategory());

            Expert savedExpert = expertRepository.save(expert);

            return new UserResponseDto(
                    savedExpert.getId(),
                    savedExpert.getEmail(),
                    savedExpert.getName(),
                    savedExpert.getRole().name()
            );
        } else {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(encodedPassword);
            user.setRole(Role.USER);
            user.setUsername(request.getName());

            User savedUser = userRepository.save(user);

            return new UserResponseDto(
                    savedUser.getId(),
                    savedUser.getEmail(),
                    savedUser.getUsername(),
                    savedUser.getRole().name()
            );
        }
    }

    public String login(LoginRequestDto request) {
        BaseUser user = userRepository.findByEmail(request.getEmail())
                .map(BaseUser.class::cast)
                .orElseGet(() -> expertRepository.findByEmail(request.getEmail())
                        .map(BaseUser.class::cast)
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음")));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호 불일치");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }
}
