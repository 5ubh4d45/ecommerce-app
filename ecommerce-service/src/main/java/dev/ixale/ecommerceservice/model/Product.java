package dev.ixale.ecommerceservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private @NotBlank String name;
    @Column(name = "desc")
    private @NotBlank String description;
    @Column(name = "price")
    private @NotBlank Double price;
    @Column(name = "img_url")
    private @NotBlank String imageUrl;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    @Override
    public String toString() {
        StringBuilder categoryString = new StringBuilder();

        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", category=" + category.getId() +
                '}';
    }
}
