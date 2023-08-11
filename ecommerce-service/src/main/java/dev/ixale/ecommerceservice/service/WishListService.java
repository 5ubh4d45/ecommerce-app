package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.dto.WishListDto;
import dev.ixale.ecommerceservice.exception.FailedOperationException;
import dev.ixale.ecommerceservice.model.Product;
import dev.ixale.ecommerceservice.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WishListService {
    List<WishListDto> readWishLists() throws FailedOperationException;

    List<WishListDto> readWishListsByUser(Long userId) throws FailedOperationException;

    Optional<WishListDto> readWishList(Long wishListId) throws FailedOperationException;

    Optional<WishListDto> createWishList(WishListDto wishListDto, String userName) throws FailedOperationException;

    Optional<WishListDto> updateWishList(WishListDto wishListDto, Long wishListId, String username) throws FailedOperationException;

    Optional<WishListDto> deleteWishList(Long wishListId, String username) throws FailedOperationException;
}
