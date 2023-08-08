package dev.ixale.ecommerceservice.dto;

import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.model.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CategoryDto {
    private Long id;
    @NotBlank(message = "Name cannot be blank!")
    private String name;
    @NotBlank(message = "Description cannot be blank!")
    private String description;
    @NotBlank(message = "Image URL cannot be blank!")
    private String imageUrl;

    public CategoryDto() {
    }

    public CategoryDto(String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public CategoryDto(Long id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getDescription(), category.getImageUrl());
    }

    public static Category toCategory(CategoryDto dto, Set<Product> products) {
        return new Category(dto.getId(), dto.getName(), dto.getDescription(), dto.getImageUrl(), products);
    }

    public static List<CategoryDto> toCategoryDto(List<Category> categories) {
        return categories.stream().map(CategoryDto::toCategoryDto).toList();
    }
}
