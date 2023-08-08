package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.common.ApiRes;
import dev.ixale.ecommerceservice.exception.NotFoundException;
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

        // throw exception if no categories found
        if (body.isEmpty()) {
            throw new NotFoundException("No categories found");
        }

//        body.forEach((item) -> System.out.println(item.toString()));

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiRes.success(body, "Categories fetched successfully"));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiRes<Category>> getCategory(@PathVariable Long categoryId) {
        // fetches category from service
        Optional<Category> body = categoryService.readCategory(categoryId);

        // returns response with the category if found, else returns NotFoundException
        return body.map(category ->
                ResponseEntity.ok(ApiRes.success(category, "Category fetched successfully")))
                .orElseThrow(() -> new NotFoundException("Category does not exists"));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiRes<Category>> createCategory(@RequestBody Category category) {
        // TODO: validate category
        // create category from service
        Category body = categoryService.createCategory(category);
        return new ResponseEntity<>(
               	ApiRes.success(body, "Category created successfully"),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiRes<Category>> updateCategory(
            @PathVariable Long categoryId,
           @RequestBody Category category) {

        // TODO: validate category
        // update category from service
        Optional<Category> updatedCategory = categoryService.updateCategory(categoryId, category);

        // return the updated category if found, else throw NotFoundException
        return updatedCategory.map(data -> ResponseEntity.status(HttpStatus.OK).body(
                        ApiRes.success(updatedCategory.get(), "Category updated successfully")))
                .orElseThrow(() -> new NotFoundException("Category does not exists"));
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiRes<Category>> deleteCategory(@PathVariable Long categoryId) {

        // TODO: validate category
        // delete category from service
        Optional<Category> deletedCategory = categoryService.deleteCategory(categoryId);

        // return the deleted category if found, else throw NotFoundException
        return deletedCategory.map(data -> ResponseEntity.status(HttpStatus.OK).body(
                        ApiRes.success(data, "Category deleted successfully")))
                .orElseThrow(() -> new NotFoundException("Category does not exists"));
    }
}
