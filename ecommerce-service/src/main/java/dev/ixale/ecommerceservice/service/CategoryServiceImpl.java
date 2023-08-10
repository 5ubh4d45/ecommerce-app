package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.dto.CategoryDto;
import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepository = categoryRepo;
    }

    @Override
    public List<CategoryDto> listCategories() {
        return CategoryDto.toCategoryDto(categoryRepository.findAll());
    }

    @Override
    public Optional<CategoryDto> readCategory(String categoryName) {
        Optional<Category> categoryOpt = categoryRepository.findByNameIgnoreCase(categoryName);

        return categoryOpt.map(CategoryDto::toCategoryDto);

    }

    @Override
    public Optional<CategoryDto> readCategory(Long categoryId) {

        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        return categoryOpt.map(CategoryDto::toCategoryDto);
    }

    @Override
    public Optional<CategoryDto> createCategory(CategoryDto categoryDto) {
        Category category = CategoryDto.toCategory(categoryDto, new HashSet<>());

        Optional<Category> categoryOpt = categoryRepository.findByNameIgnoreCase(category.getName());

        if (categoryOpt.isPresent()) {
            return Optional.empty();
        }

        return Optional.of(CategoryDto.toCategoryDto(categoryRepository.save(category)));
    }

    @Override
    public Optional<CategoryDto> updateCategory(Long categoryId, CategoryDto categoryDto) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (categoryOpt.isEmpty()) {
            return Optional.empty();
        }

        Category category = categoryOpt.get();

        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
//        category.setProducts(newCategory.getProducts());
        category.setImageUrl(categoryDto.getImageUrl());

        return Optional.of(CategoryDto.toCategoryDto(categoryRepository.save(category)));
    }

    @Override
    public Optional<CategoryDto> deleteCategory(Long categoryId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (categoryOpt.isEmpty()) {
            return Optional.empty();
        }
        categoryRepository.deleteById(categoryId);

        return categoryOpt.map(CategoryDto::toCategoryDto);
    }

    @Override
    public Optional<Category> getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }
}
