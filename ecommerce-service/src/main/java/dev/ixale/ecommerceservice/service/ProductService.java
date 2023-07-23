package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.dto.ProductDto;
import dev.ixale.ecommerceservice.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface ProductService {

    /**
     * List all products
     * @return List<ProductDto>
     */
    public List<ProductDto> readProducts();

    /**
     * Read a product by id
     * @param productId Long
     * @return Optional<ProductDto>
     */
    public Optional<ProductDto> readProduct(Long productId);

    /**
     * Read products by category
     * @param categoryId Long
     * @return List<ProductDto>
     */
    public List<ProductDto> readProductsByCategory(Long categoryId);

    /**
     * Create a new product
     * @param productDto ProductDto
     * @param category Category
     * @return Optional<ProductDto>
     */
    public Optional<ProductDto> createProduct(ProductDto productDto, Category category);

    /**
     * Update an existing product
     * @param productDto ProductDto
     * @param category Category
     * @return Optional<ProductDto>
     */
    public Optional<ProductDto> updateProduct(Long productId, ProductDto productDto, Category category);

    /**
     * Delete an existing product
     * @param productId Long
     * @return Optional<ProductDto>
     */
    public Optional<ProductDto> deleteProduct(Long productId);

    /**
     * Get a valid category
     * @param categoryId Long
     * @return Category if valid, empty Optional otherwise
     */
    public Optional<Category> getValidCategory(Long categoryId);
}
