package com.lawmon.lawmon.controller;

import com.lawmon.lawmon.Entity.Specialty;
import com.lawmon.lawmon.repository.ChatMessageRepo;
import com.lawmon.lawmon.repository.ChatRoomRepo;
import com.lawmon.lawmon.repository.ExpertRepository;
import com.lawmon.lawmon.repository.UserRepository;
import com.lawmon.lawmon.security.JwtAuthenticationFilter;
import com.lawmon.lawmon.security.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false) // Security 필터 제거
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private ChatMessageRepo chatMessageRepo;
    @MockBean
    private ChatRoomRepo chatRoomMongoRepo;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ExpertRepository expertRepository;

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
