package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepository = categoryRepo;
    }

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> readCategory(String categoryName) {
        return categoryRepository.findByNameIgnoreCase(categoryName);
    }

    public Optional<Category> readCategory(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    public Optional<Category> updateCategory(Long categoryId, Category newCategory) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (categoryOpt.isEmpty()) {
            return Optional.empty();
        }

        Category category = categoryOpt.get();

        category.setName(newCategory.getName());
        category.setDescription(newCategory.getDescription());
//        category.setProducts(newCategory.getProducts());
        category.setImageUrl(newCategory.getImageUrl());

        return Optional.of(categoryRepository.save(category));
    }

    public Optional<Category> deleteCategory(Long categoryId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (categoryOpt.isEmpty()) {
            return Optional.empty();
        }
        categoryRepository.deleteById(categoryId);

        return categoryOpt;
    }
}
