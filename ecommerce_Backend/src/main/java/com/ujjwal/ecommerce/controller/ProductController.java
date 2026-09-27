package com.ujjwal.ecommerce.controller;

import com.ujjwal.ecommerce.dto.request.ProductRequest;
import com.ujjwal.ecommerce.dto.response.ProductResponse;
import com.ujjwal.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    @GetMapping("/allProducts")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> productResponseList = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(productResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable @Positive(message = "Product ID must be positive") Long id) {
        ProductResponse productResponse = productService.getProductById(id);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProduct(@RequestParam @NotBlank(message = "Search name cannot be blank") @Size(max = 100, message = "Search name cannot exceed 100 characters") String name) {
        List<ProductResponse> responseList = productService.searchProducts(name);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable @NotBlank(message = "Category name cannot be blank") @Size(max = 100, message = "Category name cannot exceed 100 characters")String categoryName) {
        List<ProductResponse> responseList = productService.getProductsByCategory(categoryName);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @GetMapping("/price")
    public ResponseEntity<List<ProductResponse>> getProductsByPrice(@RequestParam @DecimalMin(value = "0.0", message = "Minimum price cannot be negative") BigDecimal minPrice, @RequestParam @DecimalMin(value = "0.0", message = "Maximum price cannot be negative") BigDecimal maxPrice) {
        List<ProductResponse> responseList = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable @Positive(message = "Product ID must be positive") Long id, @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.updateProduct(id, productRequest);
        return ResponseEntity.status(HttpStatus.OK).body(productResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void>  deleteProduct(@PathVariable @Positive(message = "Product ID must be positive") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
