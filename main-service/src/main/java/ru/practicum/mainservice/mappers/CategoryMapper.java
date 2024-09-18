package ru.practicum.mainservice.mappers;

import ru.practicum.mainservice.model.category.Category;
import ru.practicum.mainservice.model.category.dto.CategoryDto;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(
                categoryDto.getId() != null ? categoryDto.getId() : 0,
                categoryDto.getName() != null ? categoryDto.getName() : ""
        );
    }
}
