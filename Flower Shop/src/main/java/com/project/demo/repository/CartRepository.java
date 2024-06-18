package com.project.demo.repository;

import com.project.demo.model.Cart;
import com.project.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findFirstByUserAndIsCartOpen(User user, boolean isCartOpen);
}
