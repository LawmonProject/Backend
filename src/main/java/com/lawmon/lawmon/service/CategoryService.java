package com.lawmon.lawmon.service;

import com.lawmon.lawmon.Entity.Specialty;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    public List<String> getCategories() {
        return Arrays.stream(Specialty.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
