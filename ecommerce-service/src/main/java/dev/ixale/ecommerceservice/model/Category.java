package dev.ixale.ecommerceservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "name", unique = true)
	private @NotBlank String name;
	@Column(name = "desc")
    private @NotBlank String description;
    @Column(name = "img_url")
    private @NotBlank String imageUrl;

    @OneToMany(mappedBy = "category",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    @Override
    public String toString() {
        StringBuilder productsString = new StringBuilder();
        for (Product product : products) {
            productsString.append(product.toString());
            productsString.append("\n");
        }
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                "}\n" +
                productsString;
    }
}
