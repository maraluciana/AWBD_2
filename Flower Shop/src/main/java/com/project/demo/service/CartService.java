package com.project.demo.service;

import com.project.demo.dto.CartItemRequestDTO;
import com.project.demo.model.Cart;
import com.project.demo.model.CartItem;
import com.project.demo.model.Flower;
import com.project.demo.model.User;
import com.project.demo.repository.CartRepository;
import com.project.demo.repository.FlowerRepository;
import com.project.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final FlowerRepository flowerRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, FlowerRepository flowerRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.flowerRepository = flowerRepository;
    }

    public Optional<Cart> createCart(UUID userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.map(user -> {
            Optional<Cart> openCart = cartRepository.findFirstByUserAndIsCartOpen(user, true);
            openCart.ifPresent(cart -> {
                cart.setCartOpen(false);
                cartRepository.save(cart);
            });

            Cart newCart = new Cart(user, true);
            return Optional.of(cartRepository.save(newCart));
        }).orElse(Optional.empty());
    }

    public Cart addCartItem(UUID cartId, CartItemRequestDTO cartItemDTO) {
        Optional<Cart> openCart = cartRepository.findById(cartId);
        return openCart.map(cart -> {
            if (cart.isCartOpen()) {
                Optional<Flower> flowerOptional = flowerRepository.findById(cartItemDTO.getFlowerId());

                if (flowerOptional.isPresent()) {
                    Flower flower = flowerOptional.get();

                    CartItem cartItem = new CartItem();
                    cartItem.setFlower(flower);
                    cartItem.setQuantity(cartItemDTO.getQuantity());

                    cart.addCartItem(cartItem);

                    // Recalculate total price based on the newly added CartItem
                    int newTotalPrice = calculateTotalPrice(cart);
                    cart.setTotalPrice(newTotalPrice);

                    return cartRepository.save(cart);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }).orElse(null);
    }

    private int calculateTotalPrice(Cart cart) {
        return cart.getCartItems().stream()
                .mapToInt(item -> item.getFlower().getPrice() * item.getQuantity())
                .sum();
    }

    public void closeCart(UUID cartId) {
        Optional<Cart> openCart = cartRepository.findById(cartId);
        openCart.ifPresent(cart -> {
            cart.setCartOpen(false);
            cartRepository.save(cart);
        });
    }
}
