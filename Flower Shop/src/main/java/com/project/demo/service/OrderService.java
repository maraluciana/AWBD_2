package com.project.demo.service;

import com.project.demo.enums.Enums;
import com.project.demo.model.Cart;
import com.project.demo.model.Order;
import com.project.demo.model.User;
import com.project.demo.repository.CartRepository;
import com.project.demo.repository.OrderRepository;
import com.project.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private CartRepository cartRepository;
    private UserRepository userRepository;


    public OrderService(OrderRepository orderRepository, CartRepository cartRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    public Optional<Order> placeOrder(UUID userId, UUID cartId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);

        return cartOptional.flatMap(cart -> {
            if (cart.isCartOpen()) {
                Order order = new Order();
                order.setUser(cart.getUser());
                order.setCart(cart);
                order.setOrderDate(new Date());
                order.setTotalAmount(cart.getTotalPrice());
                order.setOrderStatus(Enums.OrderStatus.PLACED);

//                cart.setCartOpen(false);
                cartRepository.save(cart);

                return Optional.of(orderRepository.save(order));
            } else {
                return Optional.empty();
            }
        });
    }


    public List<Order> getOrdersByUser(UUID userId) {
        Optional<User> userOptional = this.userRepository.findById(userId);

        return userOptional.map(this.orderRepository::findByUser).orElse(List.of());
    }
}
