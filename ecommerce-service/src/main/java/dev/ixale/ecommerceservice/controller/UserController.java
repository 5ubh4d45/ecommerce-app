package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.common.ApiRes;
import dev.ixale.ecommerceservice.exception.NotFoundException;
import dev.ixale.ecommerceservice.model.User;
import dev.ixale.ecommerceservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update")
    public ResponseEntity<ApiRes<User>> update(@RequestBody User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Deletes a user with the provided JWT token.
     *
     * @param jwt The JWT token of the user to be deleted.
     * @return A ResponseEntity object containing an ApiResponse with a success message if the user is deleted successfully,
     *         or throws an exception if the user is not found.
     * @throws NotFoundException if the user is not found.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiRes<String>> delete(@AuthenticationPrincipal Jwt jwt) {
        Optional<User> opt = userService.deleteUser(jwt.getClaimAsString("username"));
        if (opt.isEmpty()) {
            throw new NotFoundException("User not found!");
        }
        return ResponseEntity.ok(ApiRes.success(
                "User deleted successfully!", "User deleted successfully!"));
    }

}
