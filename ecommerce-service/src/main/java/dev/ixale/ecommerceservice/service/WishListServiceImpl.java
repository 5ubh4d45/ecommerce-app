package dev.ixale.ecommerceservice.service;

import dev.ixale.ecommerceservice.common.Utils;
import dev.ixale.ecommerceservice.dto.WishListDto;
import dev.ixale.ecommerceservice.exception.DoesNotExistsException;
import dev.ixale.ecommerceservice.exception.FailedOperationException;
import dev.ixale.ecommerceservice.model.Product;
import dev.ixale.ecommerceservice.model.User;
import dev.ixale.ecommerceservice.model.WishList;
import dev.ixale.ecommerceservice.repository.ProductRepository;
import dev.ixale.ecommerceservice.repository.WishListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WishListServiceImpl implements WishListService{

    private static final Logger LOGGER = Utils.getLogger(WishListServiceImpl.class);

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public WishListServiceImpl(WishListRepository wishListRepository, ProductRepository productRepository, UserService userService) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Override
    public List<WishListDto> readWishLists() {
        try {
            List<WishList> wishLists = wishListRepository.findAll();
            return WishListDto.toDto(wishLists);
        } catch (Exception e) {
            LOGGER.error("Error while fetching wishlists", e);
            throw new FailedOperationException("Error while fetching wishlists");
        }
    }

    @Override
    public List<WishListDto> readWishListsByUser(Long userId) {
        try {
            List<WishList> wishLists = wishListRepository.findByUserId(userId);

            return WishListDto.toDto(wishLists);
        } catch (Exception e) {
            LOGGER.error("Error while fetching wishlists", e);
            throw new FailedOperationException("Error while fetching wishlists");
        }
    }

    @Override
    public Optional<WishListDto> readWishList(Long wishListId) {
        try {
            Optional<WishList> wishList = wishListRepository.findById(wishListId);
            return wishList.map(WishListDto::toDto);
        } catch (Exception e) {
            LOGGER.error("Error while fetching wishlists", e);
            throw new FailedOperationException("Error while fetching wishlists");
        }
    }

    @Override
    public Optional<WishListDto> createWishList(WishListDto wishListDto, String username) {
        try {
            Optional<User> userOpt = userService.readUserByUsername(username);
            // if user does not exists, return empty [because we also have in-memory authentication]
            if (userOpt.isEmpty()) {
                return Optional.empty();
            }
            User user = userOpt.get();

            boolean exists = wishListRepository.existsByNameIgnoreCaseAndUserId(wishListDto.getName(), user.getId());

            // if wishlist with same name exists, return empty
            if (exists) {
                return Optional.empty();
            }

            // fetch products from product repository
            Set<Product> products = wishListDto.getProductIds().stream()
                    .map(id -> productRepository.findById(id)
                            .orElseThrow(() -> new DoesNotExistsException("Product does not exists: " + id)))
                    .collect(Collectors.toSet());

            // create new wishlist
            WishList newWishList = WishListDto.toEntity(wishListDto, user, products);

            Instant now = Instant.now();
            newWishList.setCreatedAt(now);
            newWishList.setModifiedAt(now);

            // save and wishlist
            WishList savedWishList = wishListRepository.save(newWishList);
            return Optional.of(WishListDto.toDto(savedWishList));
        } catch (Exception e) {
            LOGGER.error("Error while creating wishlists", e);
            throw new FailedOperationException("Error while creating wishlists");
        }
    }

    @Override
    public Optional<WishListDto> updateWishList(WishListDto wishListDto, Long wishListId, String username) {
        try {
            // check if wishlist exists
            Optional<WishList> wishListOpt = wishListRepository.findById(wishListId);
            if (wishListOpt.isPresent()) {

                Optional<User> userOpt = userService.readUserByUsername(username);
                // if user does not exists, return empty [because we also have in-memory authentication]
                if (userOpt.isEmpty()) {
                    return Optional.empty();
                }
                User user = userOpt.get();

                // fetch products from product repository
                Set<Product> newProductSet = wishListDto.getProductIds().stream()
                        .map(id -> productRepository.findById(id)
                                .orElseThrow(() -> new DoesNotExistsException("Product does not exists: " + id)))
                        .collect(Collectors.toSet());

                WishList wishList = wishListOpt.get();

                // verify if wishlist belongs to same user
                if (!wishList.getUser().getId().equals(user.getId())) {
                    return Optional.empty();
                }

                // update wishlist
                wishList.setModifiedAt(Instant.now());
                wishList.setName(wishListDto.getName());
                wishList.setDescription(wishListDto.getDescription());
                wishList.setProducts(newProductSet);

                WishList savedWishList = wishListRepository.save(wishList);
                return Optional.of(WishListDto.toDto(savedWishList));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            LOGGER.error("Error while updating wishlists", e);
            throw new FailedOperationException("Error while updating wishlists");
        }
    }

    @Override
    public Optional<WishListDto> deleteWishList(Long wishListId, String username) {
        try {
            // check if wishlist exists
            Optional<WishList> wishList = wishListRepository.findById(wishListId);
            if (wishList.isPresent()) {
                // verify if wishlist belongs to same user
                if (!wishList.get().getUser().getUsername().equals(username)) {
                    return Optional.empty();
                }

                wishListRepository.delete(wishList.get());
                return wishList.map(WishListDto::toDto);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            LOGGER.error("Error while deleting wishlists", e);
            throw new FailedOperationException("Error while deleting wishlists");
        }
    }
}
