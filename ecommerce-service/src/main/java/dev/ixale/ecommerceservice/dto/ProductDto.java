package dev.ixale.ecommerceservice.dto;

import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.model.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * ProductDto is a class that is used to transfer data between the client and the server.
 * It is used to create a new product. It is also used to update an existing product.
 * <b>id</b> is not required when creating a new product. <b>id</b> is required when updating an existing product.
 */
@Getter
@Setter
public class ProductDto {
    private Long id;
    @NotBlank(message = "Name cannot be blank!")
    private String name;
    @NotBlank(message = "Description cannot be blank!")
    private String description;
    @NotBlank(message = "Price cannot be blank!")
    private Double price;
    @NotBlank(message = "Image URL cannot be blank!")
    private String imageUrl;
    @NotBlank(message = "Category ID cannot be blank!")
    private Long categoryId;

    public ProductDto() {
    }

    public ProductDto(String name, String description, Double price, String imageUrl, Long categoryId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public ProductDto(Long id, String name, String description, Double price, String imageUrl, Long categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    /**
     * Converts a ProductDto to a Product
     * @param product Product
     * @return ProductDTO object
     */
    public static ProductDto toProductDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getImageUrl(), product.getCategory().getId());
    }

    /**
     * Converts a List of Products to a List of ProductDtos
     * @param products List of Product
     * @return List of ProductDTO objects
     */
    public static List<ProductDto> toProductDto(List<Product> products) {
        return products.stream().map(ProductDto::toProductDto).toList();
    }

    /**
     * Converts a ProductDto to a Product
     * @param dto ProductDto
     * @param category Category
     * @return Product object
     */
    public static Product toProduct(ProductDto dto, Category category) {
        return new Product(dto.getId(), dto.getName(), dto.getDescription(), dto.getPrice(), dto.getImageUrl(), category);
    }

}
