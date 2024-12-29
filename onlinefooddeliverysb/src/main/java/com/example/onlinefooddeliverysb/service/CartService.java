package com.example.onlinefooddeliverysb.service;

import com.example.onlinefooddeliverysb.model.Cart;
import com.example.onlinefooddeliverysb.model.CartItem;
import com.example.onlinefooddeliverysb.model.FoodMenu;
import com.example.onlinefooddeliverysb.model.User;
import com.example.onlinefooddeliverysb.repository.CartItemRepository;
import com.example.onlinefooddeliverysb.repository.CartRepository;
import com.example.onlinefooddeliverysb.repository.FoodMenuRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FoodMenuRepository foodMenuRepository;

    public Cart addFoodToCart(User user, Long foodMenuId) {
        // Find the user's cart, or create a new one if it doesn't exist
        Cart cart = cartRepository.findByUser(user).orElse(null);

        if (cart == null) {
            // If the cart doesn't exist, create a new cart and set the user
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        // Find the food menu by ID
        FoodMenu foodMenu = foodMenuRepository.findById(foodMenuId)
                .orElseThrow(() -> new RuntimeException("Food menu not found"));

        // Check if the food menu is already in the cart
        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndFoodMenu(cart, foodMenu);

        if (existingCartItem.isPresent()) {
            // If it exists, increase the quantity
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.calculateTotalPrice();
            cartItemRepository.save(cartItem);
        } else {
            // If it's a new food menu, add it to the cart
            CartItem newCartItem = new CartItem();
            newCartItem.setFoodMenu(foodMenu);
            newCartItem.setCart(cart);
            newCartItem.setQuantity(1);
            newCartItem.calculateTotalPrice();
            cartItemRepository.save(newCartItem);

            // Add the new cart item to the cart's items list
            cart.getItems().add(newCartItem);
        }

        // Save the cart (if modifications were made)
        cartRepository.save(cart);
        return cart;
    }

    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public void removeFoodFromCart(User user, Long foodMenuId) {
        // Find the user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Find the food item in the cart
        FoodMenu foodMenu = foodMenuRepository.findById(foodMenuId)
                .orElseThrow(() -> new RuntimeException("Food menu not found"));

        // Find the cart item
        Optional<CartItem> cartItem = cartItemRepository.findByCartAndFoodMenu(cart, foodMenu);

        if (cartItem.isPresent()) {
            // If the item is in the cart, remove it
            cartItemRepository.delete(cartItem.get());
        } else {
            throw new RuntimeException("Food menu not found in the cart");
        }
    }
}

