package com.project.demo.service;

import com.project.demo.model.User;
import com.project.demo.repository.CategoryRepository;
import com.project.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        return this.userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = this.userRepository.findByEmail(email).orElse(null);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

    public Optional<User> getUserById(UUID userId) {
        return this.userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public void deleteUser(UUID userId) {
        this.userRepository.deleteById(userId);
    }
}
