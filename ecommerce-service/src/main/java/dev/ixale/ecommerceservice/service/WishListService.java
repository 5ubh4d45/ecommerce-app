package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.dto.WishListDto;

import java.util.List;
import java.util.Optional;

public interface WishListService {
    List<WishListDto> readWishLists();

    List<WishListDto> readWishListsByUser(Long userId);

    Optional<WishListDto> readWishList(Long wishListId);

    Optional<WishListDto> createWishList(WishListDto wishList, Long userId);

    Optional<WishListDto> updateWishList(WishListDto wishList, Long wishListId, Long userId);

    Optional<WishListDto> deleteWishList(Long wishListId);
}
