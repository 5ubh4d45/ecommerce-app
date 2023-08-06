package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.common.ApiRes;
import dev.ixale.ecommerceservice.common.Utils;
import dev.ixale.ecommerceservice.dto.LoginRequestDto;
import dev.ixale.ecommerceservice.dto.LoginResponseDto;
import dev.ixale.ecommerceservice.dto.UserDto;
import dev.ixale.ecommerceservice.enums.Authority;
import dev.ixale.ecommerceservice.model.User;
import dev.ixale.ecommerceservice.service.TokenService;
import dev.ixale.ecommerceservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder passEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                            UserService userService, TokenService tokenService,
                            PasswordEncoder passEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
        this.passEncoder = passEncoder;
    }

    @Operation(security = @SecurityRequirement(name = "noAuth"))
    @PostMapping("/login")
    public ResponseEntity<ApiRes<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto loginReq,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiRes.error(
                    Utils.notValid(bindingResult)));
        }

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginReq.username(), loginReq.password()));
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(ApiRes.error("Invalid username or password!"));
        }


        String token = tokenService.generateToken(authentication);

        return ResponseEntity.ok(ApiRes.success(
                new LoginResponseDto(authentication.getName(), token), "Login successful!"));
    }

    @Operation(security = @SecurityRequirement(name = "basicAuth"))
    @PostMapping("/token")
    public String token(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }

    @Operation(security = @SecurityRequirement(name = "noAuth"))
    @PostMapping("/signup")
    public ResponseEntity<ApiRes<LoginResponseDto>> signup(
            @Valid @RequestBody UserDto userDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(ApiRes.error(
                    Utils.notValid(bindingResult)));
        }

        if (userService.exists(userDto.username(), userDto.email())){
            return ResponseEntity.badRequest().body(
                    ApiRes.error("Username or email already exists!"));
        }

        User user = userDto.toUser(passEncoder, Set.of(
                Authority.USER,
                Authority.READ,
                Authority.WRITE
                ));

        Optional<User> opt = userService.createUser(user);

        return opt.map(value -> ResponseEntity.ok(ApiRes.success(
                new LoginResponseDto(value.getUsername(), ""),
                        "Signup successful! Please Login to continue.")))
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body(ApiRes.error("Signup failed!")));
    }


    @PostMapping("/logout")
    public String logout() {
        return "logout";
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiRes<String>> delete(@AuthenticationPrincipal Jwt jwt) {
        Optional<User> opt = userService.deleteUser(jwt.getClaimAsString("username"));
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiRes.error("User not found!"));
        }
        return ResponseEntity.ok(ApiRes.success(
                "User deleted successfully!", "User deleted successfully!"));
    }

}
