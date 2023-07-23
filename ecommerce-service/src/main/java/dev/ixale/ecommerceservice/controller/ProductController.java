package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.common.ApiResponse;
import dev.ixale.ecommerceservice.dto.ProductDto;
import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.service.ProductService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProducts() {
        List<ProductDto> body = productService.readProducts();
        if (body.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("No products found"));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(body, "Products fetched successfully"));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable Long productId) {
        return productService.readProduct(productId)
                .map(product ->
                        ResponseEntity.ok(ApiResponse.success(product, "Product fetched successfully")))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(ApiResponse.error("Product does not exists")));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDto> body = productService.readProductsByCategory(categoryId);
        if (body.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("No products found for this category: " + categoryId));
        }
        return ResponseEntity.ok(ApiResponse.success(body, "Products fetched successfully"));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody @NotBlank ProductDto productDto) {

        Optional<Category> opt = productService.getValidCategory(productDto.getCategoryId());

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Invalid category"));
        }

        return productService.createProduct(productDto, opt.get())
                .map(product ->
                        ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success(product, "Product created successfully")))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error("Could not create the product")));
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@PathVariable Long productId, @RequestBody @NotBlank ProductDto productDto) {

        Optional<Category> opt = productService.getValidCategory(productDto.getCategoryId());

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Invalid category"));
        }

        return productService.updateProduct(productId, productDto, opt.get())
                .map(product ->
                        ResponseEntity.status(HttpStatus.OK)
                                .body(ApiResponse.success(product, "Product updated successfully")))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error("Could not update the product")));
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> deleteProduct(@PathVariable Long productId) {
        return productService.deleteProduct(productId)
                .map(product ->
                        ResponseEntity.status(HttpStatus.OK)
                                .body(ApiResponse.success(product, "Product deleted successfully")))
                .orElseGet(() ->
                        ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error("Could not delete the product")));
    }
}