package com.example.onlinefooddeliverysb.controller;

import com.example.onlinefooddeliverysb.model.FoodType;
import com.example.onlinefooddeliverysb.model.Restaurant;
import com.example.onlinefooddeliverysb.model.User;
import com.example.onlinefooddeliverysb.service.FoodTypeService;
import com.example.onlinefooddeliverysb.service.RestaurantService;
import com.example.onlinefooddeliverysb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food-types")
public class AdminFoodTypeController {

    @Autowired
    private FoodTypeService foodTypeService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    @PostMapping("/{restaurantId}")
    public ResponseEntity<FoodType> createFoodType(
            @RequestBody FoodType foodType,
            @PathVariable Long restaurantId,  // Changed from @RequestParam to @PathVariable
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        if (user == null) {
            throw new RuntimeException("User not found or invalid token");
        }

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        foodType.setRestaurant(restaurant);

        System.out.println("Creating food type: " + foodType);
        return ResponseEntity.ok(foodTypeService.saveFoodType(foodType));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodType> updateFoodType(
            @PathVariable Long id,
            @RequestBody FoodType updatedFoodType,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return foodTypeService.getFoodTypeById(id)
                .map(existingFoodType -> {
                    updatedFoodType.setId(existingFoodType.getId());
                    updatedFoodType.setRestaurant(existingFoodType.getRestaurant());
                    return ResponseEntity.ok(foodTypeService.saveFoodType(updatedFoodType));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a Food Type
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodType(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        if (foodTypeService.getFoodTypeById(id).isPresent()) {
            foodTypeService.deleteFoodType(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
