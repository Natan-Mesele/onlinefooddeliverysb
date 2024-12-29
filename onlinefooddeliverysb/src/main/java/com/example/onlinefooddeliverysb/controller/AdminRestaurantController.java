package com.example.onlinefooddeliverysb.controller;

import com.example.onlinefooddeliverysb.model.Restaurant;
import com.example.onlinefooddeliverysb.model.User;
import com.example.onlinefooddeliverysb.service.RestaurantService;
import com.example.onlinefooddeliverysb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/admin/restaurant")
public class AdminRestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @PostMapping
    public Restaurant createRestaurant(
            @RequestBody Restaurant restaurant,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        restaurant.setRegistrationDate(restaurant.getRegistrationDate() == null ? LocalDateTime.now() : restaurant.getRegistrationDate());
        return restaurantService.saveRestaurant(restaurant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @PathVariable Long id,
            @RequestBody Restaurant updatedRestaurant,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return restaurantService.getRestaurantById(id)
                .map(existingRestaurant -> {
                    updatedRestaurant.setId(existingRestaurant.getId());
                    return ResponseEntity.ok(restaurantService.saveRestaurant(updatedRestaurant));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        if (restaurantService.getRestaurantById(id).isPresent()) {
            restaurantService.deleteRestaurant(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{restaurantId}/toggle-status")
    public ResponseEntity<Restaurant> toggleRestaurantStatus(
            @PathVariable Long restaurantId
    ) {
        Restaurant updatedRestaurant = restaurantService.toggleRestaurantStatus(restaurantId);
        return ResponseEntity.ok(updatedRestaurant);
    }
}
