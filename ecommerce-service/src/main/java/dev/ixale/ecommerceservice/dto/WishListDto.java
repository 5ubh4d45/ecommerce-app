package dev.ixale.ecommerceservice.dto;

import dev.ixale.ecommerceservice.model.Product;
import dev.ixale.ecommerceservice.model.User;
import dev.ixale.ecommerceservice.model.WishList;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class WishListDto {
    private Long id;

    @NotBlank(message = "Name cannot be blank!")
    private String name;

    @NotBlank(message = "Description cannot be blank!")
    private String description;

    private Set<Long> productIds;

    public WishListDto(Long id, String name, String description, Set<Long> productIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.productIds = productIds;
    }

    public WishListDto(String name, String description, Set<Long> productIds) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.productIds = productIds;
    }

    public static WishListDto toDto(WishList wishList) {
        return new WishListDto(
            wishList.getId(),
            wishList.getName(),
            wishList.getDescription(),
            wishList.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toSet())
        );
    }

    public static List<WishListDto> toDto(List<WishList> wishLists) {
        return wishLists.stream()
            .map(WishListDto::toDto)
            .collect(Collectors.toList());
    }

    public static WishList toEntity(WishListDto wishListDto, User user, Set<Product> products) {
        return new WishList(
            wishListDto.getId(),
            wishListDto.getName(),
            wishListDto.getDescription(),
            user,
            products
        );
    }

    @Override
    public String toString() {
        return "WishListDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", productIds=" + productIds.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", ")) +
                '}';
    }
}
