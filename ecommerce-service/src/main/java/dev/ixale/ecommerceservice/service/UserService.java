package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    Optional<User> readUserByUsername(String username);

    Optional<User> readUserById(Long id);

    Optional<User> readUserByEmail(String email);

    Optional<User> createUser(User user);

    Optional<User> updateUser(String username, User newUser);

    Optional<User> deleteUser(String username);

    boolean exists(String username, String email);

}
