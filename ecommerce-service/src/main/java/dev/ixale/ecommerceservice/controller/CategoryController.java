package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.common.ApiRes;
import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.service.CategoryService;
import dev.ixale.ecommerceservice.service.CategoryServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiRes<List<Category>>> getCategories() {
        List<Category> body = categoryService.listCategories();

        if (body.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiRes.error("No categories found"));
        }

//        body.forEach((item) -> System.out.println(item.toString()));

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiRes.success(body, "Categories fetched successfully"));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiRes<Category>> getCategory(@PathVariable Long categoryId) {
        Optional<Category> body = categoryService.readCategory(categoryId);
        return body.map(category ->
                ResponseEntity.ok(ApiRes.success(category, "Category fetched successfully")))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiRes.error("Category does not exists")));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiRes<Category>> createCategory(@RequestBody Category category) {
        Category body = categoryService.createCategory(category);
        return new ResponseEntity<>(
               	ApiRes.success(body, "Category created successfully"),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiRes<Category>> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        Optional<Category> updatedCategory = categoryService.updateCategory(categoryId, category);

        // return with ternary expression
        return updatedCategory.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiRes.error("Could not find the category"))
                : ResponseEntity.status(HttpStatus.OK).body(ApiRes.success(updatedCategory.get(), "Category updated successfully"));
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiRes<Category>> deleteCategory(@PathVariable Long categoryId) {
        Optional<Category> deletedCategory = categoryService.deleteCategory(categoryId);

        // return with functional style expression
        return deletedCategory.map(data ->
                ResponseEntity.status(HttpStatus.OK).body(ApiRes.success(data, "Category deleted successfully")))
                .orElseGet(() ->ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiRes.error("Category does not exists")));
    }
}
