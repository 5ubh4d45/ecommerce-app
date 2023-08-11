package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.exception.FailedOperationException;
import org.springframework.security.core.Authentication;

public interface TokenService {
    /**
     * Generates a JSON Web Token (JWT) for the given authentication object.
     *
     * @param authentication the authentication object containing user credentials
     *                       and other relevant information
     * @return a string representation of the generated JWT
     * @throws FailedOperationException if an error occurs during JWT generation
     */
    String generateJwt(Authentication authentication) throws FailedOperationException;
}
