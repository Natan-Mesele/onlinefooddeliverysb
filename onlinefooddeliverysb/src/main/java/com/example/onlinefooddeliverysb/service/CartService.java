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

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private FoodMenuRepository foodMenuRepository;

    public Cart addFoodToCart(User user, Long foodMenuId) throws Exception {
        // Fetch the user's cart or create a new one
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return newCart;
                });

        // Find the food menu item
        FoodMenu foodMenu = foodMenuRepository.findById(foodMenuId)
                .orElseThrow(() -> new RuntimeException("Food menu not found"));

        // Check if the item is already in the cart
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getFoodMenu().equals(foodMenu))
                .findFirst()
                .orElse(null);

        // If the item is in the cart, update the quantity; otherwise, add it
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.calculateTotalPrice();
        } else {
            cartItem = new CartItem();
            cartItem.setFoodMenu(foodMenu);
            cartItem.setCart(cart);
            cartItem.setQuantity(1);
            cartItem.calculateTotalPrice();
            cart.getItems().add(cartItem);
        }

        // Calculate the total price of the cart
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getTotalPrice())
                .sum();
        cart.setTotalPrice(totalPrice);

        // Save the cart and return
        return cartRepository.save(cart);
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new ArrayList<>());
        return cartRepository.save(cart);
    }

    public double calculateTotalPriceForUser(User user) {
        // Find the user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));

        // Sum up the totalPrice of all items in the cart
        return cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
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

