package com.project.demo.repository;

import com.project.demo.model.Flower;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface FlowerRepository extends JpaRepository<Flower, UUID> {
}
