package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.dto.ProductDto;
import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.model.Product;
import dev.ixale.ecommerceservice.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<ProductDto> readProducts() {
        return ProductDto.toProductDto(productRepository.findAll());
    }

    @Override
    public Optional<ProductDto> readProduct(Long productId) {
        Optional<Product> opt = productRepository.findById(productId);
        return opt.map(ProductDto::toProductDto);
    }

    @Override
    public List<ProductDto> readProductsByCategory(Long categoryId) {
        List<Product> list = productRepository.findProductsByCategoryId(categoryId);
        return ProductDto.toProductDto(list);
    }

    @Override
    public Optional<ProductDto> createProduct(ProductDto productDto, Category category) {
        Product product = productRepository.save(ProductDto.toProduct(productDto, category));
    	return Optional.of(ProductDto.toProductDto(product));
    }

    @Override
    public Optional<ProductDto> updateProduct(Long productId, ProductDto productDto, Category category) {
        productDto.setId(productId);
        return createProduct(productDto, category);
    }

    @Override
    public Optional<ProductDto> deleteProduct(Long productId) {
        Optional<Product> opt = productRepository.findById(productId);

        if(opt.isEmpty()) {
            return Optional.empty();
        }
        productRepository.deleteById(productId);
        return opt.map(ProductDto::toProductDto);
    }

    @Override
    public Optional<Category> getValidCategory(Long categoryId) {
        return categoryService.getCategory(categoryId);
    }
}
