package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.dto.CategoryDto;
import dev.ixale.ecommerceservice.exception.FailedOperationException;
import dev.ixale.ecommerceservice.model.Category;

import java.util.List;
import java.util.Optional;


public interface CategoryService {

    /**
     * List all categories
     *
     * @return List of all categories
     * @throws FailedOperationException If an error occurs while listing the categories
     */
    List<CategoryDto> listCategories() throws FailedOperationException;

    /**
     * Read a category by name
     *
     * @param categoryName Name of category to be read
     * @return Category with given name
     * @throws FailedOperationException If an error occurs while reading the category
     */
    Optional<CategoryDto> readCategory(String categoryName) throws FailedOperationException;

    /**
     * Read a category by id
     *
     * @param categoryId Id of category to be read
     * @return Category with given id
     * @throws FailedOperationException If an error occurs while reading the category
     */
    Optional<CategoryDto> readCategory(Long categoryId) throws FailedOperationException;

    /**
     * Create a new category
     *
     * @param category Category to be created
     * @return Created category
     * @throws FailedOperationException If an error occurs while creating the category
     */
    Optional<CategoryDto> createCategory(CategoryDto category) throws FailedOperationException;

    /**
     * Update a category
     *
     * @param categoryId Id of category to be updated
     * @param newCategory New category data
     * @return Updated category
     * @throws FailedOperationException If an error occurs while updating the category
     */
    Optional<CategoryDto> updateCategory(Long categoryId, CategoryDto newCategory) throws FailedOperationException;

    /**
     * Delete a category
     *
     * @param categoryId Id of category to be deleted
     * @return Deleted category
     * @throws FailedOperationException If an error occurs while deleting the category
     */
    Optional<CategoryDto> deleteCategory(Long categoryId) throws FailedOperationException;

    /**
     * Retrieves the category with the specified categoryId.
     *
     * @param categoryId The id of the category to retrieve
     * @return An Optional containing the Category with the given id, or empty if no category is found
     * @throws FailedOperationException If an error occurs while retrieving the category
     */
    Optional<Category> getCategory(Long categoryId) throws FailedOperationException;
}
