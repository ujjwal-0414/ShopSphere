package com.ujjwal.ecommerce.service;

import com.ujjwal.ecommerce.dto.request.ProductRequest;
import com.ujjwal.ecommerce.dto.response.ProductResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    List<ProductResponse> searchProducts(String name);

    List<ProductResponse> getProductsByCategory(String categoryName);

    List<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    ProductResponse updateProduct(Long id, ProductRequest productRequest);

    void deleteProduct(Long id);

}
