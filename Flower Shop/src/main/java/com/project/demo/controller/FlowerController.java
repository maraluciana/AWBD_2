package com.project.demo.controller;

import com.project.demo.dto.FlowerRequestDTO;
import com.project.demo.model.Flower;
import com.project.demo.service.FlowerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/flowers")
public class FlowerController {

    private FlowerService flowerService;

    public FlowerController(FlowerService flowerService) {
        this.flowerService = flowerService;
    }

    @PostMapping
    public ResponseEntity<Flower> addFlower(@RequestBody FlowerRequestDTO flowerRequestDTO) {
        Flower savedFlower = flowerService.addFlower(flowerRequestDTO);

        if (savedFlower != null) {
            return new ResponseEntity<>(savedFlower, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping
    public ResponseEntity<List<Flower>> getAllFlowers() {
        List<Flower> flowers = this.flowerService.getAllFlowers();
        return new ResponseEntity<>(flowers, HttpStatus.OK);
    }

    @PutMapping("/{flowerId}/availability")
    public ResponseEntity<Flower> changeAvailability(@PathVariable UUID flowerId) {
        Flower updatedFlower = this.flowerService.changeAvailability(flowerId);

        if (updatedFlower != null) {
            return new ResponseEntity<>(updatedFlower, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
