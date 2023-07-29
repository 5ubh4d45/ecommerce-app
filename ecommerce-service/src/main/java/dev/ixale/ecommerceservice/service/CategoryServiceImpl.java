package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepository = categoryRepo;
    }

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> readCategory(String categoryName) {
        return categoryRepository.findByNameIgnoreCase(categoryName);
    }

    @Override
    public Optional<Category> readCategory(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
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

    @Override
    public Optional<Category> deleteCategory(Long categoryId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (categoryOpt.isEmpty()) {
            return Optional.empty();
        }
        categoryRepository.deleteById(categoryId);

        return categoryOpt;
    }
}
