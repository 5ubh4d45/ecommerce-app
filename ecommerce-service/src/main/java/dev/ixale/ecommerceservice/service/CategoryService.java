package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.model.Category;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface CategoryService {

    /**
     * List all categories
     * @return List of all categories
     */
    List<Category> listCategories();

    /**
     * Read a category by name
     * @param categoryName Name of category to be read
     * @return Category with given name
     */
    Optional<Category> readCategory(String categoryName);

    /**
     * Read a category by id
     * @param categoryId Id of category to be read
     * @return Category with given id
     */
    Optional<Category> readCategory(Long categoryId);

    /**
     * Create a new category
     * @param category Category to be created
     * @return Created category
     */
    Category createCategory(Category category);

    /**
     * Update a category
     * @param categoryId Id of category to be updated
     * @param newCategory New category data
     * @return Updated category
     */
    Optional<Category> updateCategory(Long categoryId, Category newCategory);

    /**
     * Delete a category
     * @param categoryId Id of category to be deleted
     * @return Deleted category
     */
    Optional<Category> deleteCategory(Long categoryId);
}
