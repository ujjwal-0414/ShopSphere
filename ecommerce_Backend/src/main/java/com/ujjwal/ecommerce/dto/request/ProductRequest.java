package com.ujjwal.ecommerce.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max=150,message = "Product name cannot exceed 150 characters")
    private String name;

    @NotBlank(message = "Product description is required")
    @Size(max=500,message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value="0.01",message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock is required")
    @Min(value=0,message = "Stock cannot be negative")
    private Integer stock;

    @Size(max=500,message = "Image Url cannot exceed 500 characters")
    private String imageUrl;

    @NotBlank(message = "Category name is required")
    private String categoryName;

}
