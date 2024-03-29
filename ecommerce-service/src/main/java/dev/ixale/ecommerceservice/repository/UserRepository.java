package dev.ixale.ecommerceservice.repository;

import dev.ixale.ecommerceservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmailIgnoreCase(String email);
}
