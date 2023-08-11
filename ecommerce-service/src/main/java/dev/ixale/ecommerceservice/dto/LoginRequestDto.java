package dev.ixale.ecommerceservice.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * A data transfer object representing a login request.
 * This class is used to transfer login request data between different layers of the application.
 */
public record LoginRequestDto(
        @NotBlank(message = "Username cannot be blank!")
        String username,

        @NotBlank(message = "Password cannot be blank!")
        String password
) {
}
