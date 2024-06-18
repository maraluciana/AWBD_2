package com.project.demo.repository;

import com.project.demo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    // You can add custom queries or methods here if needed
}
