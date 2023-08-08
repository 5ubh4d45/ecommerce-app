package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.common.ApiRes;
import dev.ixale.ecommerceservice.common.Utils;
import dev.ixale.ecommerceservice.dto.LoginRequestDto;
import dev.ixale.ecommerceservice.dto.LoginResponseDto;
import dev.ixale.ecommerceservice.dto.UserDto;
import dev.ixale.ecommerceservice.enums.Authority;
import dev.ixale.ecommerceservice.exception.AlreadyExistsException;
import dev.ixale.ecommerceservice.exception.InvalidRequestException;
import dev.ixale.ecommerceservice.exception.NotFoundException;
import dev.ixale.ecommerceservice.exception.OperationFailedException;
import dev.ixale.ecommerceservice.model.User;
import dev.ixale.ecommerceservice.service.TokenService;
import dev.ixale.ecommerceservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    /**
     * Performs the login operation using the provided login request.
     *
     * @param loginReq The login request object containing the user's username and password.
     * @param bindingResult The binding result object to check for any validation errors.
     * @return A ResponseEntity object containing the API response with the login response data.
     * @throws InvalidRequestException If the login request is invalid or contains invalid details.
     */
    @Operation(security = @SecurityRequirement(name = "noAuth"))
    @PostMapping("/login")
    public ResponseEntity<ApiRes<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequestDto loginReq,
            BindingResult bindingResult) {

        // check if request is valid
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(
                    "Invalid login details, please put the login details correctly.",
                    Utils.notValid(bindingResult));
        }

        // try authenticate
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginReq.username(), loginReq.password()));
        }
        catch (BadCredentialsException e) {
            throw new InvalidRequestException("Invalid username or password.");
        }


        // generate token
        String token = tokenService.generateToken(authentication);

        return ResponseEntity.ok(ApiRes.success(
                new LoginResponseDto(authentication.getName(), token), "Login successful!"));
    }

    /**
     * Generates a token for the provided authentication object.
     *
     * @param authentication The authentication object representing the user's authenticated credentials.
     * @return A string representation of the generated token.
     */
    @Operation(security = @SecurityRequirement(name = "basicAuth"))
    @PostMapping("/token")
    public String token(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }

    /**
     * Signs up a new user with the provided user details.
     *
     * @param userDto       The UserDto object containing the user details to sign up.
     * @param bindingResult The BindingResult object that holds the validation errors for the user details.
     * @return A ResponseEntity object containing an ApiResponse with a LoginResponseDto if the signup is successful,
     *         or throws an exception if the signup fails.
     * @throws InvalidRequestException     if the provided user details are invalid.
     * @throws AlreadyExistsException      if the provided username or email already exists.
     * @throws OperationFailedException    if the signup operation fails.
     */
    @Operation(security = @SecurityRequirement(name = "noAuth"))
    @PostMapping("/signup")
    public ResponseEntity<ApiRes<LoginResponseDto>> signup(
            @Valid @RequestBody UserDto userDto, BindingResult bindingResult) {

        // check if request is valid
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(
                    "Invalid signup details, please put the signup details correctly.",
                    Utils.notValid(bindingResult));
        }

        // check if username or email already exists
        if (userService.exists(userDto.username(), userDto.email())){
            throw new AlreadyExistsException("Username or email already exists!");
        }

        // if not exists, create user
        User user = userDto.toUser(passEncoder, Set.of(
                Authority.USER,
                Authority.READ,
                Authority.WRITE
                ));

        Optional<User> opt = userService.createUser(user);

        // if user created successfully, return success response or throw exception
        return opt.map(value -> ResponseEntity.ok(ApiRes.success(
                new LoginResponseDto(value.getUsername(), ""),
                        "Signup successful! Please Login to continue.")))
                .orElseThrow(() -> new OperationFailedException("Signup failed!"));
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Not implemented yet!");
    }

}
