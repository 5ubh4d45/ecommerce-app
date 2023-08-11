package dev.ixale.ecommerceservice.repository;

import dev.ixale.ecommerceservice.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findByUserId(Long userId);

    boolean existsByNameIgnoreCaseAndUserId(String name, Long userId);
}
