package com.lawmon.lawmon.service;

import com.lawmon.lawmon.Entity.Expert;
import com.lawmon.lawmon.dto.ExpertProfileDto;
import com.lawmon.lawmon.repository.ExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpertService {
    private final ExpertRepository expertRepository;

    public ExpertProfileDto getExpertProfile(String email) {
        Expert expert = expertRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일을 가진 전문가를 찾을 수 없습니다: " + email));

        return convertToDto(expert);
    }

    @Transactional
    public ExpertProfileDto updateExpertProfile(String email, ExpertProfileDto dto) {
        Expert expert = expertRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일을 가진 전문가를 찾을 수 없습니다: " + email));

        if (dto.getName() != null) expert.setName(dto.getName());
        if (dto.getSpecialty() != null) expert.setSpecialty(dto.getSpecialty());
        if (dto.getLicenseNumber() != null) expert.setLicenseNumber(dto.getLicenseNumber());
        if (dto.getCategory() != null) expert.setCategory(dto.getCategory());
        if (dto.getPhoneNumber() != null) expert.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getExperience() != null) expert.setExperience(dto.getExperience());
        if (dto.getProfileImageUrl() != null) expert.setProfileImageUrl(dto.getProfileImageUrl());

        expertRepository.save(expert);
        return convertToDto(expert);
    }

    private ExpertProfileDto convertToDto(Expert expert) {
        return ExpertProfileDto.builder()
                .name(expert.getName())
                .specialty(expert.getSpecialty())
                .licenseNumber(expert.getLicenseNumber())
                .category(expert.getCategory())
                .email(expert.getEmail())
                .phoneNumber(expert.getPhoneNumber())
                .experience(expert.getExperience())
                .profileImageUrl(expert.getProfileImageUrl())
                .build();
    }
}