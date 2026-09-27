package com.ujjwal.ecommerce.service.impl;

import com.ujjwal.ecommerce.dto.request.CategoryRequest;
import com.ujjwal.ecommerce.dto.response.CategoryResponse;
import com.ujjwal.ecommerce.entity.Category;
import com.ujjwal.ecommerce.exception.ConflictException;
import com.ujjwal.ecommerce.exception.ResourceNotFoundException;
import com.ujjwal.ecommerce.mapper.CategoryMapper;
import com.ujjwal.ecommerce.repository.CategoryRepository;
import com.ujjwal.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest){
        if(categoryRepository.findByNameIgnoreCase(categoryRequest.getName()).isPresent()){
            throw new ConflictException("Category already exists");
        }

        // dto to entity
        Category category =  new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        Category savedCategory = categoryRepository.save(category);
        // entity to dto
        return categoryMapper.mapToCategoryResponse(savedCategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapToCategoryResponse) // to return category response not category as findAll will give category
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found with id: "+id));
        return categoryMapper.mapToCategoryResponse(category);
    }

    @Override
    public CategoryResponse updateCategory(Long id,CategoryRequest categoryRequest){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found with id: "+id));

        // if category name is same and only the letters case is changed then it not be considered as category change
        if(!category.getName()
                .equalsIgnoreCase(categoryRequest.getName())){
            if(categoryRepository.findByNameIgnoreCase(categoryRequest.getName()).isPresent()){
                throw new ConflictException("Category already exists");
            }
        }

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.mapToCategoryResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found with id: "+id));
        categoryRepository.delete(category);
    }

}
