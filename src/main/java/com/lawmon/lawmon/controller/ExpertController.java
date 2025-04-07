package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.dto.ExpertListDto;
import com.lawmon.lawmon.dto.ExpertProfileDto;
import com.lawmon.lawmon.Entity.ExpertCategory;
import com.lawmon.lawmon.Entity.Specialty;
import com.lawmon.lawmon.service.ExpertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/experts")
@RequiredArgsConstructor
public class ExpertController {

    private final ExpertService expertService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadProfileImage(@RequestParam String email, @RequestParam MultipartFile file) throws IOException {
        String imageUrl = expertService.uploadProfileImageLocally(email, file);
        return ResponseEntity.ok(imageUrl);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ExpertListDto>> getExpertsByCategory(@PathVariable ExpertCategory category) {
        List<ExpertListDto> experts = expertService.getExpertsByCategory(category);
        return ResponseEntity.ok(experts);
    }

    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<List<ExpertListDto>> getExpertsBySpecialty(@PathVariable Specialty specialty) {
        List<ExpertListDto> experts = expertService.getExpertsBySpecialty(specialty);
        return ResponseEntity.ok(experts);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ExpertProfileDto> getExpertByEmail(@PathVariable String email) {
        ExpertProfileDto expertDto = expertService.getExpertByEmail(email);
        return ResponseEntity.ok(expertDto);
    }
}