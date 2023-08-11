package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.exception.FailedOperationException;
import dev.ixale.ecommerceservice.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {
    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return an {@link Optional} containing the user if found, or an empty {@link Optional} if not found
     * @throws FailedOperationException if the operation fails due to any reason
     */
    Optional<User> readUserByUsername(String username) throws FailedOperationException;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return an {@link Optional} containing the user if found, or an empty {@link Optional} if not found
     * @throws FailedOperationException if the operation fails due to any reason
     */
    Optional<User> readUserById(Long id) throws FailedOperationException;

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve
     * @return an {@link Optional} containing the user if found, or an empty {@link Optional} if not found
     * @throws FailedOperationException if the operation fails due to any reason
     */
    Optional<User> readUserByEmail(String email) throws FailedOperationException;

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return an {@link Optional} containing the created user if the operation is successful, or an empty {@link Optional} if user already exists
     * @throws FailedOperationException if the operation fails due to any reason
     */
    Optional<User> createUser(User user) throws FailedOperationException;

    /**
     * Updates an existing user with the specified username.
     *
     * @param username the username of the user to update
     * @param newUser the new user object containing the updated information
     * @return an {@link Optional} containing the updated user if the operation is successful, or an empty {@link Optional} if the operation fails
     * @throws FailedOperationException if the operation fails due to any reason
     */
    Optional<User> updateUser(String username, User newUser) throws FailedOperationException;

    /**
     * Deletes the user with the specified username.
     *
     * @param username the username of the user to delete
     * @return an {@link Optional} containing the deleted user if the operation is successful, or an empty {@link Optional} if the operation fails
     * @throws FailedOperationException if the operation fails due to any reason
     */
    Optional<User> deleteUser(String username) throws FailedOperationException;

    /**
     * Checks if a user with the specified username and email exists.
     *
     * @param username the username of the user to check
     * @param email the email of the user to check
     * @return {@code true} if the user exists, {@code false} otherwise
     * @throws FailedOperationException if the operation fails due to any reason
     */
    boolean exists(String username, String email)  throws FailedOperationException;

}
