package com.project.demo.controller;

import com.project.demo.dto.FlowerRequestDTO;
import com.project.demo.model.Flower;
import com.project.demo.service.FlowerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/flowers")
public class FlowerController {

    private FlowerService flowerService;

    public FlowerController(FlowerService flowerService) {
        this.flowerService = flowerService;
    }

    @Operation(summary = "Add a new flower")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Flower added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Flower.class),
                            examples = @ExampleObject(
                                    value = "{ \"id\": 1, \"name\": \"Rose\", \"description\": \"A beautiful red rose\", \"price\": 1999, \"imageUrl\": \"http://example.com/images/rose.jpg\", \"available\": true, \"categoryId\": \"3fa85f64-5717-4562-b3fc-2c963f66afa6\" }"
                            )
                    )}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @PostMapping
    @CircuitBreaker(name = "addFlowerCircuitBreaker", fallbackMethod = "addFlowerFallback")
    public ResponseEntity<EntityModel<Flower>> addFlower(@RequestBody FlowerRequestDTO flowerRequestDTO) {
        Flower savedFlower = flowerService.addFlower(flowerRequestDTO);

        EntityModel<Flower> flowerModel = EntityModel.of(savedFlower);
        WebMvcLinkBuilder linkToFlowers = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllFlowers());
        flowerModel.add(linkToFlowers.withRel("all-flowers"));

        return new ResponseEntity<>(flowerModel, HttpStatus.CREATED);
    }

    public ResponseEntity<EntityModel<String>> addFlowerFallback(FlowerRequestDTO flowerRequestDTO, Throwable throwable) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.error("Error occurred while adding a new flower with request {}: {}", flowerRequestDTO, throwable.getMessage());

        String errorMessage = "The service is currently unavailable. Please try again later. " +
                "If the problem persists, contact support with the following error details: " +
                throwable.getMessage();

        EntityModel<String> errorModel = EntityModel.of(errorMessage);
        return new ResponseEntity<>(errorModel, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Operation(summary = "Get all flowers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Flower.class))})
    })
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Flower>>> getAllFlowers() {
        List<Flower> flowers = this.flowerService.getAllFlowers();
        List<EntityModel<Flower>> flowerModels = flowers.stream()
                .map(flower -> EntityModel.of(flower,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllFlowers())
                                .slash(flower.getId())
                                .withSelfRel()))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Flower>> collectionModel = CollectionModel.of(flowerModels,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllFlowers()).withSelfRel());

        return new ResponseEntity<>(collectionModel, HttpStatus.OK);
    }

    @Operation(summary = "Change availability of a flower")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flower availability changed",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Flower.class))}),
            @ApiResponse(responseCode = "404", description = "Flower not found",
                    content = @Content)
    })
    @PutMapping("/{flowerId}/availability")
    @CircuitBreaker(name = "changeAvailabilityCircuitBreaker", fallbackMethod = "changeAvailabilityFallback")
    public ResponseEntity<EntityModel<Flower>> changeAvailability(@PathVariable UUID flowerId) {
        Flower updatedFlower = flowerService.changeAvailability(flowerId);

        if (updatedFlower != null) {
            EntityModel<Flower> flowerModel = EntityModel.of(updatedFlower);
            WebMvcLinkBuilder linkToFlowers = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllFlowers());
            flowerModel.add(linkToFlowers.withRel("all-flowers"));

            return new ResponseEntity<>(flowerModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<EntityModel<String>> changeAvailabilityFallback(UUID flowerId, Throwable throwable) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.error("Error occurred while changing availability for flowerId {}: {}", flowerId, throwable.getMessage());

        String errorMessage = "The service is currently unavailable. Please try again later. " +
                "If the problem persists, contact support with the following error details: " +
                throwable.getMessage();

        EntityModel<String> errorModel = EntityModel.of(errorMessage);
        return new ResponseEntity<>(errorModel, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
