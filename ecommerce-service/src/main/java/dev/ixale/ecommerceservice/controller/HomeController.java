package dev.ixale.ecommerceservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class HomeController {
    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    private final JwtDecoder jwtDecoder;

    public HomeController(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @GetMapping("/")
    public String home() {
        return "Hello World!";
    }


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


    @Operation(security = @SecurityRequirement(name = "noAuth"))
    @GetMapping(path = "/tokenDetails")
    public ResponseEntity<Map<String, String>> getTokenDetails(@RequestBody TokenBody body) {
        Jwt jwt = jwtDecoder.decode(body.token());

        Map<String, String> map = new HashMap<>();
        map.put("Scope", jwt.getClaim("scope"));
        map.put("User Name", jwt.getClaim("username"));
        map.put("Subject", jwt.getSubject());
        map.put("Issued at", Objects.requireNonNull(jwt.getIssuedAt()).toString());
        map.put("Expires at:", Objects.requireNonNull(jwt.getExpiresAt()).toString());
        map.put("Token Value", jwt.getTokenValue());

        return ResponseEntity.ok(map);
    }

    public record TokenBody(String token) {}

}
