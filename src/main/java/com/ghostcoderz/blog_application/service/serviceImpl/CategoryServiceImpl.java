package com.ghostcoderz.blog_application.service.serviceImpl;

import com.ghostcoderz.blog_application.entity.Category;
import com.ghostcoderz.blog_application.exceptions.ResourceNotFoundException;
import com.ghostcoderz.blog_application.payload.CategoryDto;
import com.ghostcoderz.blog_application.repository.CategoryRepo;
import com.ghostcoderz.blog_application.service.serviceInterface.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepo categoryRepo, ModelMapper modelMapper) {
        this.categoryRepo = categoryRepo;
        this.modelMapper = modelMapper;
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category addedCategory = this.categoryRepo.save(dtoToCategory(categoryDto));
        return categoryToDto(addedCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {
        Category category = dtoToCategory(categoryDto);
        Category categoryInDB = this.categoryRepo.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Category", "id" , categoryId.longValue()));

        categoryInDB.setCategoryTitle(
                category.getCategoryTitle()
        );
        categoryInDB.setCategoryDescription(
                category.getCategoryDescription()
        );
        this.categoryRepo.save(categoryInDB);

        return categoryToDto(categoryInDB);
    }

    @Override
    public CategoryDto getCategoryById(Integer categoryId) {
        Category categoryInDB = this.categoryRepo.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException(
                        "Category", "id" , categoryId.longValue()));

        return categoryToDto(categoryInDB);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return this.categoryRepo.findAll().stream()
                .map(this::categoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        this.categoryRepo.deleteById(categoryId);
    }

    private Category dtoToCategory(CategoryDto categoryDto)
    {
        return this.modelMapper.map(categoryDto, Category.class);
    }

    private CategoryDto categoryToDto(Category category)
    {
        return this.modelMapper.map(category, CategoryDto.class);
    }

}
