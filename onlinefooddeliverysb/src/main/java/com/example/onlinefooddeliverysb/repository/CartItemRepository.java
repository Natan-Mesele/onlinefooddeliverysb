package com.example.onlinefooddeliverysb.repository;

import com.example.onlinefooddeliverysb.model.Cart;
import com.example.onlinefooddeliverysb.model.CartItem;
import com.example.onlinefooddeliverysb.model.FoodMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndFoodMenu(Cart cart, FoodMenu foodMenu);
}
