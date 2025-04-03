package com.lawmon.lawmon.service;

import com.lawmon.lawmon.Entity.Expert;
import com.lawmon.lawmon.Entity.ExpertCategory;
import com.lawmon.lawmon.Entity.Specialty;
import com.lawmon.lawmon.dto.ExpertListDto;
import com.lawmon.lawmon.dto.ExpertProfileDto;
import com.lawmon.lawmon.repository.ExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpertService {
    private final ExpertRepository expertRepository;
    private final LocalFileService localFileService;

    public List<ExpertListDto> getAllExperts() {
        return expertRepository.findAll().stream()
                .map(expert -> new ExpertListDto(expert.getName(), expert.getSpecialty()))
                .collect(Collectors.toList());
    }

    public ExpertProfileDto getExpertProfile(String email) {
        Expert expert = expertRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 이메일을 가진 전문가를 찾을 수 없습니다: " + email));
        return convertToDto(expert);
    }

    public ExpertProfileDto getExpertByEmail(String email) {
        Expert expert = expertRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "전문가를 찾을 수 없습니다: " + email));
        return convertToDto(expert);
    }

    public List<ExpertListDto> getExpertsByCategory(ExpertCategory category) {
        return expertRepository.findByCategory(category).stream()
                .map(expert -> new ExpertListDto(expert.getName(), expert.getSpecialty()))
                .collect(Collectors.toList());
    }

    public List<ExpertListDto> getExpertsBySpecialty(Specialty specialty) {
        return expertRepository.findBySpecialty(specialty).stream()
                .map(expert -> new ExpertListDto(expert.getName(), expert.getSpecialty()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ExpertProfileDto updateExpertProfile(String email, ExpertProfileDto dto) {
        Expert expert = expertRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 이메일을 가진 전문가를 찾을 수 없습니다: " + email));

        Optional.ofNullable(dto.getName()).ifPresent(expert::setName);
        Optional.ofNullable(dto.getSpecialty()).ifPresent(expert::setSpecialty);
        Optional.ofNullable(dto.getLicenseNumber()).ifPresent(expert::setLicenseNumber);
        Optional.ofNullable(dto.getCategory()).ifPresent(expert::setCategory);
        Optional.ofNullable(dto.getPhoneNumber()).ifPresent(expert::setPhoneNumber);
        Optional.ofNullable(dto.getExperience()).ifPresent(expert::setExperience);
        Optional.ofNullable(dto.getProfileImageUrl()).ifPresent(expert::setProfileImageUrl);

        expertRepository.save(expert);
        return convertToDto(expert);
    }

    @Transactional
    public String uploadProfileImageLocally(String email, MultipartFile file) throws IOException {
        Expert expert = expertRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "전문가를 찾을 수 없습니다"));

        String relativeUrl = localFileService.saveProfileImage(file);
        expert.setProfileImageUrl(relativeUrl);
        expertRepository.save(expert);
        return relativeUrl;
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