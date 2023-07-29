package dev.ixale.ecommerceservice.repository;

import dev.ixale.ecommerceservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Optional<Category> findByNameIgnoreCase(String categoryName);
}
