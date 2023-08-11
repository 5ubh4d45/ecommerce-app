package dev.ixale.ecommerceservice.dto;

import dev.ixale.ecommerceservice.enums.Authority;
import dev.ixale.ecommerceservice.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

public record UserDto(
        @NotBlank(message = "Username cannot be blank!")
        String username,

        @NotBlank(message = "First name cannot be blank!")
        String firstName,

        @NotBlank(message = "Last name cannot be blank!")
        String lastName,

        @NotBlank(message = "Email cannot be blank!")
        @Email(message = "Email must be valid!")
        String email,

        @NotBlank(message = "Password cannot be blank!")
        String password

) {
    public User toUser(PasswordEncoder encoder, Set<Authority> authorities) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email.toLowerCase());
        user.setPassword(encoder.encode(password));
        user.setAuthorities(authorities);
        return user;
    }
}
