package com.project.demo.service;

import com.project.demo.dto.FlowerRequestDTO;
import com.project.demo.model.Category;
import com.project.demo.model.Flower;
import com.project.demo.repository.CategoryRepository;
import com.project.demo.repository.FlowerRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FlowerService {

    private final FlowerRepository flowerRepository;
    private final CategoryRepository categoryRepository;

    public FlowerService(FlowerRepository flowerRepository, CategoryRepository categoryRepository) {
        this.flowerRepository = flowerRepository;
        this.categoryRepository = categoryRepository;
    }

    public Flower addFlower(FlowerRequestDTO flowerRequestDTO) {
        Optional<Category> category = categoryRepository.findById(flowerRequestDTO.getCategoryId());

        if (category.isPresent()) {
            Flower flower = new Flower(
                    flowerRequestDTO.getName(),
                    flowerRequestDTO.getDescription(),
                    flowerRequestDTO.getPrice(),
                    flowerRequestDTO.getImageUrl(),
                    flowerRequestDTO.isAvailable(),
                    category.get()
            );

            return flowerRepository.save(flower);
        } else {
            return null;
        }
    }


    public List<Flower> getAllFlowers() {
        return flowerRepository.findAll();
    }

    public Flower changeAvailability(UUID flowerId) {
        Optional<Flower> optionalFlower = flowerRepository.findById(flowerId);

        return optionalFlower.map(flower -> {
            flower.setAvailability(!flower.getAvailability());
            return flowerRepository.save(flower);
        }).orElse(null);
    }

}
