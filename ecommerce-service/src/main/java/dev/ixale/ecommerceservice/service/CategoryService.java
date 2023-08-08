package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.dto.CategoryDto;
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
    List<CategoryDto> listCategories();

    /**
     * Read a category by name
     * @param categoryName Name of category to be read
     * @return Category with given name
     */
    Optional<CategoryDto> readCategory(String categoryName);

    /**
     * Read a category by id
     * @param categoryId Id of category to be read
     * @return Category with given id
     */
    Optional<CategoryDto> readCategory(Long categoryId);

    /**
     * Create a new category
     * @param category Category to be created
     * @return Created category
     */
    Optional<CategoryDto> createCategory(CategoryDto category);

    /**
     * Update a category
     * @param categoryId Id of category to be updated
     * @param newCategory New category data
     * @return Updated category
     */
    Optional<CategoryDto> updateCategory(Long categoryId, CategoryDto newCategory);

    /**
     * Delete a category
     * @param categoryId Id of category to be deleted
     * @return Deleted category
     */
    Optional<CategoryDto> deleteCategory(Long categoryId);

    /**
     * Retrieves the category with the specified categoryId.
     *
     * @param categoryId The id of the category to retrieve
     * @return An Optional containing the Category with the given id, or empty if no category is found
     */
    Optional<Category> getCategory(Long categoryId);
}
