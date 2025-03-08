package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.Entity.Specialty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
@Tag(name = "Category API", description = "카테고리 API")
public class CategoryController {

    private static final Map<Specialty, String> CATEGORY_DISPLAY_NAMES = Map.of(
            Specialty.Labor, "근로 계약서",
            Specialty.Insurance, "보험 계약서",
            Specialty.RealEstate, "부동산 계약서"
    );

    @Operation(summary = "카테고리 목록 조회", description = "지원되는 카테고리 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = Arrays.stream(Specialty.values())
                .map(CATEGORY_DISPLAY_NAMES::get)
                .toList();

        return ResponseEntity.ok(categories);
    }
}
