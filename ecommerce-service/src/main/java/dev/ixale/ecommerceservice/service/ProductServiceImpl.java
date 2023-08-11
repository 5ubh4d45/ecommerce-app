package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.common.Utils;
import dev.ixale.ecommerceservice.dto.ProductDto;
import dev.ixale.ecommerceservice.exception.FailedOperationException;
import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.model.Product;
import dev.ixale.ecommerceservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = Utils.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<ProductDto> readProducts() throws FailedOperationException {
        try {
            return ProductDto.toProductDto(productRepository.findAll());
        } catch (Exception e) {
            LOGGER.error("Error while fetching products", e);
            throw new FailedOperationException("Error while fetching products");
        }
    }

    @Override
    public Optional<ProductDto> readProduct(Long productId) throws FailedOperationException {
        try {
            Optional<Product> opt = productRepository.findById(productId);
            return opt.map(ProductDto::toProductDto);
        } catch (Exception e) {
            LOGGER.error("Error while fetching product by id", e);
            throw new FailedOperationException("Error while fetching product by id");
        }
    }

    @Override
    public List<ProductDto> readProductsByCategory(Long categoryId) throws FailedOperationException {
        try {
            List<Product> list = productRepository.findProductsByCategoryId(categoryId);
            return ProductDto.toProductDto(list);
        } catch (Exception e) {
            LOGGER.error("Error while fetching products by category", e);
            throw new FailedOperationException("Error while fetching products by category");
        }
    }

    @Override
    public Optional<ProductDto> createProduct(ProductDto productDto, Category category) throws FailedOperationException {
        try {
            List<Product> list = productRepository.findProductsByNameIgnoreCase(productDto.getName());

            // if product with same name already exists, return empty
            if (!list.isEmpty()) {
                return Optional.empty();
            }
            // if category is null, return empty
            Product product = productRepository.save(ProductDto.toProduct(productDto, category));
            return Optional.of(ProductDto.toProductDto(product));
        } catch (Exception e) {
            LOGGER.error("Error while creating product", e);
            throw new FailedOperationException("Error while creating product");
        }
    }

    @Override
    public Optional<ProductDto> updateProduct(Long productId, ProductDto productDto, Category category) throws FailedOperationException {
        try {
            Optional<Product> opt = productRepository.findById(productId);

            // if product not found, return empty
            if (opt.isEmpty()) {
                return Optional.empty();
            }
            // if category is null, return empty (Already checked in controller though dto validation)
//            if (category == null) {
//                return Optional.empty();
//            }

            // set id to productDto
            productDto.setId(productId);
            // update product
            Product product = productRepository.save(ProductDto.toProduct(productDto, category));
            return Optional.of(ProductDto.toProductDto(product));

        } catch (Exception e) {
            LOGGER.error("Error while updating product", e);
            throw new FailedOperationException("Error while updating product");
        }
    }

    @Override
    public Optional<ProductDto> deleteProduct(Long productId) throws FailedOperationException {
        try {
            Optional<Product> opt = productRepository.findById(productId);

            if (opt.isEmpty()) {
                return Optional.empty();
            }
            productRepository.deleteById(productId);
            return opt.map(ProductDto::toProductDto);
        } catch (Exception e) {
            LOGGER.error("Error while deleting product", e);
            throw new FailedOperationException("Error while deleting product");
        }
    }

    @Override
    public Optional<Category> getValidCategory(Long categoryId) throws FailedOperationException {
        try {
            return categoryService.getCategory(categoryId);
        } catch (Exception e) {
            LOGGER.error("Error while fetching category", e);
            throw new FailedOperationException("Error while fetching category");
        }
    }
}
