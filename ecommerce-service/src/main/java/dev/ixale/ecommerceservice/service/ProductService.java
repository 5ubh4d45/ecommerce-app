package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.dto.ProductDto;
import dev.ixale.ecommerceservice.exception.FailedOperationException;
import dev.ixale.ecommerceservice.model.Category;

import java.util.List;
import java.util.Optional;


/**
 * The ProductService interface provides methods for managing products.
 */
public interface ProductService {

    /**
     * Reads and returns a list of ProductDto objects.
     *
     * @return a List of ProductDto objects, or an empty list if no products are found
     * @throws FailedOperationException if the operation fails for any reason
     */
    List<ProductDto> readProducts() throws FailedOperationException;

    /**
     * Reads a product by its ID and returns an Optional object containing a ProductDto if the product exists, or an empty Optional if the product does not exist.
     *
     * @param productId the ID of the product to be read
     * @return an Optional object containing a ProductDto if the product exists, or an empty Optional if the product does not exist
     * @throws FailedOperationException if the operation fails for any reason
     */
    Optional<ProductDto> readProduct(Long productId) throws FailedOperationException;

    /**
     * Reads products by category and returns a list of ProductDto objects that belong to the specified category.
     *
     * @param categoryId the ID of the category to filter the products by
     * @return a list of ProductDto objects that belong to the specified category, or an empty list if no products are found
     * @throws FailedOperationException if the operation fails for any reason
     */
    List<ProductDto> readProductsByCategory(Long categoryId) throws FailedOperationException;

    /**
     * Create a new product and associate it with the specified category. Returns an optional ProductDto object if the creation is successful.
     *
     * @param productDto the ProductDto object containing the details of the new product
     * @param category the Category object representing the category to associate the product with
     * @return an optional ProductDto object if the creation is successful, otherwise an empty optional if the product already exists
     * @throws FailedOperationException if the operation fails for any reason
     */
    Optional<ProductDto> createProduct(ProductDto productDto, Category category) throws FailedOperationException;

    /**
     * Update an existing product and associate it with the specified category.
     * Returns an optional ProductDto object if the update is successful.
     *
     * @param productId the unique identifier of the product to update
     * @param productDto the ProductDto object containing the updated details of the product
     * @param category the Category object representing the category to associate the updated product with
     * @return an optional ProductDto object if the update is successful, otherwise an empty optional if the product does not exist
     * @throws FailedOperationException if the operation fails for any reason
     */
    Optional<ProductDto> updateProduct(Long productId, ProductDto productDto, Category category) throws FailedOperationException;

    /**
     * Delete an existing product.
     *
     * @param productId the unique identifier of the product to delete
     * @return an optional ProductDto object if the deletion is successful, otherwise an empty optional, if the product does not exist
     * @throws FailedOperationException if the operation fails for any reason
     */
    Optional<ProductDto> deleteProduct(Long productId) throws FailedOperationException;

    /**
     * Get a valid category.
     *
     * @param categoryId the unique identifier of the category to retrieve
     * @return an optional Category object if the category is valid, otherwise an empty optional, if the category does not exist
     * @throws FailedOperationException if the operation fails for any reason
     */
    Optional<Category> getValidCategory(Long categoryId) throws FailedOperationException;
}
