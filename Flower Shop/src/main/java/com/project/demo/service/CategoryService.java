package com.project.demo.service;

import com.project.demo.model.Category;
import com.project.demo.model.Flower;
import com.project.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Flower> getAllFlowersInCategory(UUID categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        return category.map(Category::getFlowers).orElse(null);
    }
}
