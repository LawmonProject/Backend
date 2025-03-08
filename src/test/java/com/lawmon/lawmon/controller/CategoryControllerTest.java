package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리 목록 조회 API 테스트")
    void getCategories_ShouldReturnCategoryList() throws Exception {
        // Given: Mock 데이터 설정
        List<String> categories = Arrays.asList("근로 계약서", "보험 계약서", "부동산 계약서");
        given(categoryService.getCategories()).willReturn(categories);

        // When & Then: API 테스트 수행 및 검증
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk()) // 응답 상태 200 확인
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.data.categories[0]").value("근로 계약서"))
                .andExpect(jsonPath("$.data.categories[1]").value("보험 계약서"))
                .andExpect(jsonPath("$.data.categories[2]").value("부동산 계약서"));
    }
}
