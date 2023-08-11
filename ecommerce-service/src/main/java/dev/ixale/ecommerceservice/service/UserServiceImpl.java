package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.common.Utils;
import dev.ixale.ecommerceservice.exception.FailedOperationException;
import dev.ixale.ecommerceservice.model.User;
import dev.ixale.ecommerceservice.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = Utils.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = readUserByUsername(username);

        if (opt.isEmpty()) {
            LOGGER.debug("\nUser not found: {}", username);
        } else {
            LOGGER.debug("\nUser Found: {}", opt.get().getUsername());
        }

        return opt.orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Optional<User> readUserByUsername(String username) throws FailedOperationException {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            LOGGER.error("\nFailed to read user by username: {}, Error: {}", username, e.getMessage());
            throw new FailedOperationException("Failed to read user by username");
        }
    }

    @Override
    public Optional<User> readUserById(Long id) throws FailedOperationException {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            LOGGER.error("\nFailed to read user by id: {}, Error: {}", id, e.getMessage());
            throw new FailedOperationException("Failed to read user by id");
        }
    }

    @Override
    public Optional<User> readUserByEmail(String email) {
        try {
            return userRepository.findByEmailIgnoreCase(email);
        } catch (Exception e) {
            LOGGER.error("\nFailed to read user by email: {}, Error: {}", email, e.getMessage());
            throw new FailedOperationException("Failed to read user by email");
        }
    }

    @Override
    public boolean exists(String username, String email) throws FailedOperationException {
        try {
            return userRepository.findByUsername(username).isPresent() || userRepository.findByEmailIgnoreCase(email).isPresent();
        } catch (Exception e) {
            LOGGER.error("\nFailed to check if user exists: {}, Error: {}", username, e.getMessage());
            throw new FailedOperationException("Failed to check if user exists");
        }
    }

    @Override
    public Optional<User> createUser(User user) {
        LOGGER.debug("\nNew creation request for user: {}", user);

        try {
            if (exists(user.getUsername(), user.getEmail())) {
                return Optional.empty();
            }
            userRepository.save(user);

            LOGGER.debug("\nUser created: {}", user);

            return readUserByUsername(user.getUsername());
        } catch (Exception e) {
            LOGGER.error("\nFailed to create user: {}, Error: {}", user, e.getMessage());
            throw new FailedOperationException("Failed to create user");
        }
    }

    @Override
    public Optional<User> updateUser(String username, User newUser) {
        LOGGER.debug("\nNew update request for user: {}", newUser);

        try {
            if (exists(username, newUser.getEmail())) {
                return Optional.empty();
            }
            Optional<User> opt = userRepository.findByUsername(username);
            if (opt.isPresent()) {
                User user = opt.get();
//            Disabled some non changing fields
                user.setUsername(newUser.getUsername());
                user.setPassword(newUser.getPassword());
                user.setFirstName(newUser.getFirstName());
                user.setLastName(newUser.getLastName());
                user.setEmail(newUser.getEmail());
//            user.setAuthorities(newUser.getAuthoritiesSet());
                userRepository.save(user);

                LOGGER.debug("\nUser updated: {}", user);

                return Optional.of(user);
            }

            return Optional.empty();
        } catch (Exception e) {
            LOGGER.error("\nFailed to update user: {}", newUser, e);
            throw new FailedOperationException("Failed to update user");
        }
    }

    @Override
    public Optional<User> deleteUser(String username) {
        LOGGER.debug("\nNew delete request for user: {}", username);

        try {
            Optional<User> opt = userRepository.findByUsername(username);
            if (opt.isPresent()) {
                User user = opt.get();
                userRepository.delete(user);

                LOGGER.debug("\nUser deleted: {}", user);

                return Optional.of(user);
            }
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.error("\nFailed to delete user: {}", username, e);
            throw new FailedOperationException("Failed to delete user");
        }
    }

}
