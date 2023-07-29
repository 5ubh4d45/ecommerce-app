package dev.ixale.ecommerceservice.dto;

public record SignUpRequestDto(
        String username,
        String email,
        String password

) {
}
