package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.model.User;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class HomeController {
    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/user")
    public String user(Authentication authentication) {

        return "Hello " + authentication.getName() + "!"
                + " with authorities " + authentication.getAuthorities();
    }


    @GetMapping("/admin")
    public String admin(Authentication authentication) {
        LOGGER.debug("\nAdmin endpoint hit: " + authentication.getName() + "\n"
                + "with authorities " + authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" ")));

        return "Hello " + authentication.getName() + "!"
                + " with authorities " + authentication.getAuthorities();
    }

    @GetMapping("/token")
    public ResponseEntity<String> getToken(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(
                "Scope: " + jwt.getClaim("scope")
                        + "\nName: " + jwt.getClaim("name")
                        + "\nSubject: " + jwt.getSubject()
                        + "\nIssued at: " + Objects.requireNonNull(jwt.getIssuedAt())
                        + "\nExpires at: " + Objects.requireNonNull(jwt.getExpiresAt())
                        + "\nToken Value: " + jwt.getTokenValue()
        );
    }

    public record TokenBody(String token) {}

}
