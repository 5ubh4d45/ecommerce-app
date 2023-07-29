package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    public Optional<User> readUserByUsername(String username);

    public Optional<User> readUserById(Long id);

    public Optional<User> readUserByEmail(String email);

    public Optional<User> createUser(User user);

    public Optional<User> updateUser(String username, User newUser);

    public Optional<User> deleteUser(String username);

    public boolean exists(String username, String email);

}
