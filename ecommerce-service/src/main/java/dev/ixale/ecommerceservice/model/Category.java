package dev.ixale.ecommerceservice.model;

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
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "name")
	private @NotBlank String name;
	@Column(name = "desc")
    private @NotBlank String description;
    @Column(name = "img_url")
    private @NotBlank String imageUrl;

}
