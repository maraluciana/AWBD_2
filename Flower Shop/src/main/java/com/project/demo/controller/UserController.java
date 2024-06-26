package com.project.demo.controller;

import com.project.demo.dto.LoginRequestDTO;
import com.project.demo.exceptions.InvalidUserException;
import com.project.demo.exceptions.UserNotFoundException;
import com.project.demo.model.User;
import com.project.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<EntityModel<User>> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            EntityModel<User> resource = EntityModel.of(registeredUser);

            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).registerUser(user)).withSelfRel();
            resource.add(selfLink);

            resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).loginUser(new LoginRequestDTO())).withRel("login"));

            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            throw new InvalidUserException("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<EntityModel<User>> loginUser(@RequestBody LoginRequestDTO loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User loggedInUser = userService.login(email, password);

        if (loggedInUser != null) {
            EntityModel<User> resource = EntityModel.of(loggedInUser);

            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).loginUser(loginRequest)).withSelfRel();
            resource.add(selfLink);
            resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).deleteUser(loggedInUser.getId())).withRel("logout"));

            return ResponseEntity.ok(resource);
        } else {
            throw new UserNotFoundException(email);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = this.userService.getAllUsers();

        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "delete user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id",
                    content = @Content), @ApiResponse(responseCode = "404",
            description = "user not found", content = @Content)})
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new UserNotFoundException(userId.toString());
        }
    }
}
