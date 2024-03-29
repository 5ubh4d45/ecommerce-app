package dev.ixale.ecommerceservice.repository;

import dev.ixale.ecommerceservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductsByCategoryId(Long categoryId);

    List<Product> findProductsByNameIgnoreCase(String name);

}
