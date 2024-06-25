package com.project.demo.controller;

import com.project.demo.dto.LoginRequestDTO;
import com.project.demo.model.User;
import com.project.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
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
        User registeredUser = userService.register(user);

        if (registeredUser != null) {
            EntityModel<User> resource = EntityModel.of(registeredUser);

            Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).registerUser(user)).withSelfRel();
            resource.add(selfLink);

            resource.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).loginUser(new LoginRequestDTO())).withRel("login"));

            return ResponseEntity.ok(resource);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}

