package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.common.ApiRes;
import dev.ixale.ecommerceservice.dto.ProductDto;
import dev.ixale.ecommerceservice.exception.NotFoundException;
import dev.ixale.ecommerceservice.exception.OperationFailedException;
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
    public ResponseEntity<ApiRes<List<ProductDto>>> getProducts() {
        // fetches products from service
        List<ProductDto> body = productService.readProducts();

        // throw exception if no products found
        if (body.isEmpty()) {
            throw new NotFoundException("No products found");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiRes.success(body, "Products fetched successfully"));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiRes<ProductDto>> getProduct(@PathVariable Long productId) {
        // fetches product from service, if not found throws NotFoundException
        return productService.readProduct(productId)
                .map(product -> ResponseEntity.ok(
                        ApiRes.success(product, "Product fetched successfully")))
                .orElseThrow(() -> new NotFoundException("Product does not exists"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiRes<List<ProductDto>>> getProductsByCategory(@PathVariable Long categoryId) {
        // fetches products from service
        List<ProductDto> body = productService.readProductsByCategory(categoryId);

        // throw NotFoundException if no products found
        if (body.isEmpty()) {
            throw new NotFoundException("No products found for this category");
        }
        return ResponseEntity.ok(ApiRes.success(body, "Products fetched successfully"));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiRes<ProductDto>> createProduct(@RequestBody @NotBlank ProductDto productDto) {
        // TODO: validate product
        // fetches category from service
        Optional<Category> opt = productService.getValidCategory(productDto.getCategoryId());

        // throw NotFoundException if category not found
        if (opt.isEmpty()) {
            throw new NotFoundException("Invalid category");
        }

        // create product from service and return response, else throw OperationFailedException
        return productService.createProduct(productDto, opt.get())
                .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(
                        ApiRes.success(product, "Product created successfully")))
                .orElseThrow(() -> new OperationFailedException("Could not create the product"));
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiRes<ProductDto>> updateProduct(
            @PathVariable Long productId,
            @RequestBody @NotBlank ProductDto productDto) {

        // TODO: validate product
        // fetches category from service
        Optional<Category> opt = productService.getValidCategory(productDto.getCategoryId());

        // throw NotFoundException if category not found
        if (opt.isEmpty()) {
            throw new NotFoundException("Invalid category");
        }

        // update product from service and return response, else throw OperationFailedException
        return productService.updateProduct(productId, productDto, opt.get())
                .map(product -> ResponseEntity.status(HttpStatus.OK).body(
                        ApiRes.success(product, "Product updated successfully")))
                .orElseThrow(() -> new OperationFailedException("Could not update the product"));
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiRes<ProductDto>> deleteProduct(@PathVariable Long productId) {
        // delete product from service and return response, else throw NotFoundException
        return productService.deleteProduct(productId)
                .map(product -> ResponseEntity.status(HttpStatus.OK).body(
                        ApiRes.success(product, "Product deleted successfully")))
                .orElseThrow(() -> new NotFoundException("Could not delete the product"));
    }
}
