package com.project.demo.controller;

import com.project.demo.model.Category;
import com.project.demo.model.Flower;
import com.project.demo.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.addCategory(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }


    @GetMapping("/{categoryId}/flowers")
    public ResponseEntity<List<Flower>> getAllFlowersInCategory(@PathVariable UUID categoryId) {
        List<Flower> flowers = categoryService.getAllFlowersInCategory(categoryId);
        return new ResponseEntity<>(flowers, HttpStatus.OK);
    }
}
