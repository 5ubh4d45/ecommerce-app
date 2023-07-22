package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> body = categoryService.listCategories();
        return !body.isEmpty() ? new ResponseEntity<>(body, HttpStatus.OK) : ResponseEntity.noContent().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category body = categoryService.createCategory(category);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        Optional<Category> updatedCategory = categoryService.updateCategory(categoryId, category);

        return updatedCategory.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build() : ResponseEntity.of(updatedCategory);
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long categoryId) {
        Optional<Category> deletedCategory = categoryService.deleteCategory(categoryId);

        return deletedCategory.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.of(deletedCategory);
    }
}
