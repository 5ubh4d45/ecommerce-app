package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.common.ApiRes;
import dev.ixale.ecommerceservice.common.Utils;
import dev.ixale.ecommerceservice.dto.WishListDto;
import dev.ixale.ecommerceservice.exception.DoesNotExistsException;
import dev.ixale.ecommerceservice.exception.InvalidRequestException;
import dev.ixale.ecommerceservice.model.Product;
import dev.ixale.ecommerceservice.model.User;
import dev.ixale.ecommerceservice.service.ProductService;
import dev.ixale.ecommerceservice.service.UserService;
import dev.ixale.ecommerceservice.service.WishListService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wishlists")
public class WishListController {
    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiRes<List<WishListDto>>> getWishLists() {
        // fetches wish lists from service
        List<WishListDto> body = wishListService.readWishLists();

        // throw exception if no wish lists found
        if (body.isEmpty()) {
            throw new DoesNotExistsException("No wish lists found");
        }

        return ResponseEntity.ok(
                ApiRes.success(body, "Wish lists fetched successfully"));
    }

    @GetMapping("/{wishListId}")
    public ResponseEntity<ApiRes<WishListDto>> getWishList(@PathVariable Long wishListId) {
        // fetches wish list from service
        Optional<WishListDto> body = wishListService.readWishList(wishListId);

        // throw exception if no wish list found
        return body.map(wishListDto -> ResponseEntity.ok(
                        ApiRes.success(wishListDto, "Wish list fetched successfully")))
                .orElseThrow(() -> new DoesNotExistsException("Wish list not found"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiRes<List<WishListDto>>> getWishListsByUser(@PathVariable Long userId) {
        // fetches wish lists from service
        List<WishListDto> body = wishListService.readWishListsByUser(userId);

        // throw exception if no wish lists found
        if (body.isEmpty()) {
            throw new DoesNotExistsException("No wishlists found");
        }

        return ResponseEntity.ok(
                ApiRes.success(body, "Wishlists fetched successfully"));
    }

    @PostMapping("/")
    public ResponseEntity<ApiRes<WishListDto>> createWishList(@Valid @RequestBody WishListDto wishListDto,
                                                              Principal principal, BindingResult bindingResult) {
        // throw exception if validation fails
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Invalid Wishlist details, could not create the wishlist" +
                    " Please check the 'data' to see details.",
                    Utils.extractErrFromValid(bindingResult));
        }

        String username = principal.getName();

        // creates wish list in service
        Optional<WishListDto> body = wishListService.createWishList(wishListDto, username);

        // throw exception if wish list not created
        return body.map(wishListDto1 -> ResponseEntity.ok(
                        ApiRes.success(wishListDto1, "Wish list created successfully")))
                .orElseThrow(() -> new DoesNotExistsException("Wish list not created"));
    }

    @PutMapping("/{wishListId}")
    public ResponseEntity<ApiRes<WishListDto>> updateWishList(@Valid @RequestBody WishListDto wishListDto,
                                                              @PathVariable Long wishListId, Principal principal,
                                                              BindingResult bindingResult) {

        // throw exception if validation fails
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException("Invalid Wishlist details, could not update the wishlist" +
                    " Please check the 'data' to see details.",
                    Utils.extractErrFromValid(bindingResult));
        }

        String username = principal.getName();

        // updates wish list in service
        Optional<WishListDto> body = wishListService.updateWishList(wishListDto, wishListId, username);

        // throw exception if wish list not updated
        return body.map(wishListDto1 -> ResponseEntity.ok(
                        ApiRes.success(wishListDto1, "Wish list updated successfully")))
                .orElseThrow(() -> new DoesNotExistsException("Wish list not updated"));
    }

    @DeleteMapping("/{wishListId}")
    public ResponseEntity<ApiRes<WishListDto>> deleteWishList(@PathVariable Long wishListId, Principal principal) {
        String username = principal.getName();

        // deletes wish list in service
        Optional<WishListDto> body = wishListService.deleteWishList(wishListId, username);

        // throw exception if wish list not deleted
        return body.map(wishListDto1 -> ResponseEntity.ok(
                        ApiRes.success(wishListDto1, "Wish list deleted successfully")))
                .orElseThrow(() -> new DoesNotExistsException("Wish list not deleted"));
    }

}
