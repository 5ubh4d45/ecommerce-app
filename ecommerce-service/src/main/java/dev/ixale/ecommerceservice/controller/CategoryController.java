package dev.ixale.ecommerceservice.controller;

import dev.ixale.ecommerceservice.common.ApiRes;
import dev.ixale.ecommerceservice.common.Utils;
import dev.ixale.ecommerceservice.dto.CategoryDto;
import dev.ixale.ecommerceservice.exception.InvalidRequestException;
import dev.ixale.ecommerceservice.exception.NotFoundException;
import dev.ixale.ecommerceservice.exception.OperationFailedException;
import dev.ixale.ecommerceservice.model.Category;
import dev.ixale.ecommerceservice.service.CategoryService;
import dev.ixale.ecommerceservice.service.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<ApiRes<List<CategoryDto>>> getCategories() {
        List<CategoryDto> body = categoryService.listCategories();

        // throw exception if no categories found
        if (body.isEmpty()) {
            throw new NotFoundException("No categories found");
        }

//        body.forEach((item) -> System.out.println(item.toString()));

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiRes.success(body,
                        "Categories fetched successfully"));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiRes<CategoryDto>> getCategory(@PathVariable Long categoryId) {
        // fetches category from service
        Optional<CategoryDto> body = categoryService.readCategory(categoryId);

        // returns response with the category if found, else returns NotFoundException
        return body.map(category ->
                ResponseEntity.ok(ApiRes.success(category,
                        "Category fetched successfully")))
                .orElseThrow(() -> new NotFoundException("Category does not exists"));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiRes<CategoryDto>> createCategory(
            @Valid @RequestBody CategoryDto category,
            BindingResult bindingResult) {

        // validate category
        if (bindingResult.hasErrors()){
            throw new InvalidRequestException(
                    "Invalid category details, cloud not create category." +
                    " please put the category details correctly.",
                    Utils.extractErrFromValid(bindingResult));
        }

        // create category from service
        Optional<CategoryDto> categoryOpt = categoryService.createCategory(category);

        return categoryOpt.map(body ->
                ResponseEntity.status(HttpStatus.CREATED).body(
                        ApiRes.success(body, "Category created successfully")))
                .orElseThrow(() ->
                    new OperationFailedException("Category could not be created")
        );
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<ApiRes<CategoryDto>> updateCategory(
            @PathVariable Long categoryId, @Valid @RequestBody CategoryDto category,
            BindingResult bindingResult) {

        // validate category
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new InvalidRequestException(
                    "Invalid category details, cloud not update category." +
                    " please put the category details correctly.",
                    Utils.extractErrFromValid(bindingResult));
        }

        // update category from service
        Optional<CategoryDto> updatedCategory = categoryService.updateCategory(categoryId, category);

        // return the updated category if found, else throw NotFoundException
        return updatedCategory.map(data -> ResponseEntity.status(HttpStatus.OK).body(
                        ApiRes.success(updatedCategory.get(), "Category updated successfully")))
                .orElseThrow(() -> new NotFoundException("Category does not exists"));
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiRes<CategoryDto>> deleteCategory(@PathVariable Long categoryId) {

        // delete category from service
        Optional<CategoryDto> deletedCategory = categoryService.deleteCategory(categoryId);

        // return the deleted category if found, else throw NotFoundException
        return deletedCategory.map(data -> ResponseEntity.status(HttpStatus.OK).body(
                        ApiRes.success(data, "Category deleted successfully")))
                .orElseThrow(() -> new NotFoundException("Category does not exists"));
    }
}
