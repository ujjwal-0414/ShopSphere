package com.ujjwal.ecommerce.service.impl;

import com.ujjwal.ecommerce.dto.request.ProductRequest;
import com.ujjwal.ecommerce.dto.response.ProductResponse;
import com.ujjwal.ecommerce.entity.Category;
import com.ujjwal.ecommerce.entity.Product;
import com.ujjwal.ecommerce.exception.BadRequestException;
import com.ujjwal.ecommerce.exception.ResourceNotFoundException;
import com.ujjwal.ecommerce.mapper.ProductMapper;
import com.ujjwal.ecommerce.repository.CategoryRepository;
import com.ujjwal.ecommerce.repository.ProductRepository;
import com.ujjwal.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Category category = categoryRepository
                .findByNameIgnoreCase(productRequest.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + productRequest.getCategoryName()));
        // dto to entity
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setImageUrl(productRequest.getImageUrl());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        // entity to dto
        return productMapper.mapToProductResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts(){
        return productRepository.findAllWithCategory()
                .stream()
                .map(productMapper::mapToProductResponse)// to return product response not product as findAll will give product
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id){
        Product product = productRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found with id:"  + id));
        return productMapper.mapToProductResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String name){
        return productRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(productMapper::mapToProductResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(
            String categoryName
    ) {

        // checks whether a specific category exists in your database before attempting to fetch or process any related products
        if (categoryRepository
                .findByNameIgnoreCase(categoryName)
                .isEmpty()) {

            throw new ResourceNotFoundException(
                    "Category not found: " + categoryName
            );
        }

        return productRepository
                .findByCategoryNameIgnoreCase(categoryName)
                .stream()
                .map(productMapper::mapToProductResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice){

        //validates the price filter range
        if(minPrice.compareTo(maxPrice) > 0){
            throw new BadRequestException(
                    "Minimum Price cannot be greater than Maximum Price"
            );
        }
        return productRepository
                .findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(productMapper::mapToProductResponse)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest){

        //Fetching & Validating the Existing Product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found with id:" + id));

        //Fetching & Validating the Assigned Category
        Category category = categoryRepository
                .findByNameIgnoreCase(productRequest.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: "
                                + productRequest.getCategoryName()
                ));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setImageUrl(productRequest.getImageUrl());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);

        return productMapper.mapToProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found with id:" + id));
        productRepository.delete(product);
    }

}
