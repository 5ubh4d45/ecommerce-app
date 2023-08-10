package dev.ixale.ecommerceservice.repository;

import dev.ixale.ecommerceservice.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

}
