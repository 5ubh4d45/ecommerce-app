package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.common.ApiResponse;
import dev.ixale.ecommerceservice.dto.LoginRequestDto;
import dev.ixale.ecommerceservice.dto.LoginResponseDto;
import dev.ixale.ecommerceservice.dto.SignUpRequestDto;
import dev.ixale.ecommerceservice.enums.UserAuthority;
import dev.ixale.ecommerceservice.model.User;
import dev.ixale.ecommerceservice.service.TokenService;
import dev.ixale.ecommerceservice.service.UserService;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
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

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginReq) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginReq.username(), loginReq.password()));
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid username or password!"));
        }


        String token = tokenService.generateToken(authentication);

        return ResponseEntity.ok(ApiResponse.success(
                new LoginResponseDto(authentication.getName(), token), "Login successful!"));
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<LoginResponseDto>> signup(@RequestBody SignUpRequestDto signupReq) {
        if (userService.exists(signupReq.username(), signupReq.email())){
            return ResponseEntity.badRequest().body(ApiResponse.error("Username or email already exists!"));
        }

        User user = new User();
        user.setUsername(signupReq.username());
        user.setEmail(signupReq.email());
        user.setPassword(passEncoder.encode(signupReq.password()));
        user.setAuthorities(Set.of(UserAuthority.USER, UserAuthority.READ, UserAuthority.WRITE));

        Optional<User> opt = userService.createUser(user);

        return opt.map(value -> ResponseEntity.ok(ApiResponse.success(
                new LoginResponseDto(value.getUsername(), ""), "Signup successful!")))
                .orElseGet(() -> ResponseEntity.badRequest().body(ApiResponse.error("Signup failed!")));
    }

    @PostMapping("/logout")
    public String logout() {
        return "logout";
    }

}
