package dev.ixale.ecommerceservice.dto;

/**
 * Represents a response DTO for login request.
 */
public record LoginResponseDto(
        String username,
        String token
) {
}
