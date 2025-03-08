package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.Entity.Specialty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final Map<Specialty, String> CATEGORY_DISPLAY_NAMES = Map.of(
            Specialty.Labor, "근로 계약서",
            Specialty.Insurance, "보험 계약서",
            Specialty.RealEstate, "부동산 계약서"
    );

    @Test
    @DisplayName("GET /categories - 카테고리 목록 조회")
    void getCategories_shouldReturnCategoryList() throws Exception {
        List<String> expectedCategories = CATEGORY_DISPLAY_NAMES.values().stream().toList();

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", containsInAnyOrder(expectedCategories.toArray())));
    }
}
