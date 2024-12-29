package com.example.onlinefooddeliverysb.controller;

import com.example.onlinefooddeliverysb.model.Cart;
import com.example.onlinefooddeliverysb.model.User;
import com.example.onlinefooddeliverysb.service.CartService;
import com.example.onlinefooddeliverysb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;  // Assuming you have a userService to extract user info from JWT

    @PostMapping("/add/{foodMenuId}")
    public ResponseEntity<Cart> addFoodToCart(
            @PathVariable Long foodMenuId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        // Get the user from the JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Add the food item to the user's cart
        Cart cart = cartService.addFoodToCart(user, foodMenuId);

        // Calculate the total price for all items in the user's cart
        double totalPrice = cart.getItems().stream()
                .mapToDouble(item -> item.getFoodMenu().getPrice() * item.getQuantity())
                .sum();

        // Update the total price in the cart object (optional)
        cart.setTotalPrice(totalPrice);

        return ResponseEntity.ok(cart);
    }


    @GetMapping
    public ResponseEntity<Cart> getCart(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.getCartByUser(user);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove/{foodMenuId}")
    public ResponseEntity<String> removeFoodFromCart(
            @PathVariable Long foodMenuId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        try {
            cartService.removeFoodFromCart(user, foodMenuId);
            return ResponseEntity.ok("Food item removed from cart");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Food item not found in cart");
        }
    }
}

