package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.model.User;
import dev.ixale.ecommerceservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

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
    public Optional<User> readUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> readUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> readUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public boolean exists(String username, String email) {
        return userRepository.findByUsername(username).isPresent() || userRepository.findByEmailIgnoreCase(email).isPresent();
    }

    @Override
    public Optional<User> createUser(User user) {
        LOGGER.debug("\nNew creation request for user: {}", user);

        if (exists(user.getUsername(), user.getEmail())) {
            return Optional.empty();
        }
        userRepository.save(user);

        LOGGER.debug("\nUser created: {}", user);

        return readUserByUsername(user.getUsername());
    }

    @Override
    public Optional<User> updateUser(String username, User newUser) {
        LOGGER.debug("\nNew update request for user: {}", newUser);

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
    }

    @Override
    public Optional<User> deleteUser(String username) {
        LOGGER.debug("\nNew delete request for user: {}", username);

        Optional<User> opt = userRepository.findByUsername(username);
        if (opt.isPresent()) {
            User user = opt.get();
            userRepository.delete(user);

            LOGGER.debug("\nUser deleted: {}", user);

            return Optional.of(user);
        }
        return Optional.empty();
    }

}
