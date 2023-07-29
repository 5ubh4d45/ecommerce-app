package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.common.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<Category>>> getCategories() {
        List<Category> body = categoryService.listCategories();

        if (body.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("No categories found"));
        }

//        body.forEach((item) -> System.out.println(item.toString()));

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(body, "Categories fetched successfully"));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<Category>> getCategory(@PathVariable Long categoryId) {
        Optional<Category> body = categoryService.readCategory(categoryId);
        return body.map(category ->
                ResponseEntity.ok(ApiResponse.success(category, "Category fetched successfully")))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Category does not exists")));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Category>> createCategory(@RequestBody Category category) {
        Category body = categoryService.createCategory(category);
        return new ResponseEntity<>(
               	ApiResponse.success(body, "Category created successfully"),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        Optional<Category> updatedCategory = categoryService.updateCategory(categoryId, category);

        // return with ternary expression
        return updatedCategory.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Could not find the category"))
                : ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(updatedCategory.get(), "Category updated successfully"));
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponse<Category>> deleteCategory(@PathVariable Long categoryId) {
        Optional<Category> deletedCategory = categoryService.deleteCategory(categoryId);

        // return with functional style expression
        return deletedCategory.map(data ->
                ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(data, "Category deleted successfully")))
                .orElseGet(() ->ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Category does not exists")));
    }
}
