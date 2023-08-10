package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.dto.WishListDto;
import dev.ixale.ecommerceservice.model.WishList;
import dev.ixale.ecommerceservice.repository.WishListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishListServiceImpl implements WishListService{

    private static final Logger LOGGER = LoggerFactory.getLogger(WishListServiceImpl.class);

    private final WishListRepository wishListRepository;

    public WishListServiceImpl(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    @Override
    public List<WishListDto> readWishLists() {
        List<WishList> wishLists = wishListRepository.findAll();
        return WishListDto.toDto(wishLists);
    }

    @Override
    public List<WishListDto> readWishListsByUser(Long userId) {
        return null;
    }

    @Override
    public Optional<WishListDto> readWishList(Long wishListId) {
        return Optional.empty();
    }

    @Override
    public Optional<WishListDto> createWishList(WishListDto wishList, Long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<WishListDto> updateWishList(WishListDto wishList, Long wishListId, Long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<WishListDto> deleteWishList(Long wishListId) {
        return Optional.empty();
    }
}
