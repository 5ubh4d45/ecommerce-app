package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.dto.CategoryDto;
import dev.ixale.ecommerceservice.exception.FailedOperationException;
import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepository = categoryRepo;
    }

    @Override
    public List<CategoryDto> listCategories() throws FailedOperationException {
        try {
            return CategoryDto.toCategoryDto(categoryRepository.findAll());
        } catch (Exception e) {
            LOGGER.error("Error while fetching categories: {}", e.getMessage());
            throw new FailedOperationException("Error while fetching categories");
        }
    }

    @Override
    public Optional<CategoryDto> readCategory(String categoryName) throws FailedOperationException {
        try {
            Optional<Category> categoryOpt = categoryRepository.findByNameIgnoreCase(categoryName);

            return categoryOpt.map(CategoryDto::toCategoryDto);
        } catch (Exception e) {
            LOGGER.error("Error while fetching category.", e);
            throw new FailedOperationException("Error while fetching category");
        }
    }

    @Override
    public Optional<CategoryDto> readCategory(Long categoryId) throws FailedOperationException {
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

            return categoryOpt.map(CategoryDto::toCategoryDto);
        } catch (Exception e) {
            LOGGER.error("Error while fetching category.", e);
            throw new FailedOperationException("Error while fetching category");
        }
    }

    @Override
    public Optional<CategoryDto> createCategory(CategoryDto categoryDto) throws FailedOperationException {
        try {
            Category category = CategoryDto.toCategory(categoryDto, new HashSet<>());

            Optional<Category> categoryOpt = categoryRepository.findByNameIgnoreCase(category.getName());

            if (categoryOpt.isPresent()) {
                return Optional.empty();
            }

            return Optional.of(CategoryDto.toCategoryDto(categoryRepository.save(category)));
        } catch (Exception e) {
            LOGGER.error("Error while creating category.", e);
            throw new FailedOperationException("Error while creating category");
        }
    }

    @Override
    public Optional<CategoryDto> updateCategory(Long categoryId, CategoryDto categoryDto)
            throws FailedOperationException {
        try {
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
        } catch (Exception e) {
            LOGGER.error("Error while updating category.", e);
            throw new FailedOperationException("Error while updating category");
        }
    }

    @Override
    public Optional<CategoryDto> deleteCategory(Long categoryId) throws FailedOperationException {
        try {
            Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

            if (categoryOpt.isEmpty()) {
                return Optional.empty();
            }
            categoryRepository.deleteById(categoryId);

            return categoryOpt.map(CategoryDto::toCategoryDto);
        } catch (Exception e) {
            LOGGER.error("Error while deleting category.", e);
            throw new FailedOperationException("Error while deleting category");
        }
    }

    @Override
    public Optional<Category> getCategory(Long categoryId) throws FailedOperationException {
        try {
            return categoryRepository.findById(categoryId);
        } catch (Exception e) {
            LOGGER.error("Error while fetching category.", e);
            throw new FailedOperationException("Error while fetching category");
        }
    }
}
