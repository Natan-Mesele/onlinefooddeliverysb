package com.example.onlinefooddeliverysb.controller;

import com.example.onlinefooddeliverysb.model.FoodType;
import com.example.onlinefooddeliverysb.service.FoodTypeService;
import com.example.onlinefooddeliverysb.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/food-types")
public class FoodTypeController {

    @Autowired
    private FoodTypeService foodTypeService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<FoodType>> getFoodTypesByRestaurant(
            @PathVariable Long restaurantId
    ) {
        return ResponseEntity.ok(foodTypeService.getFoodTypesByRestaurantId(restaurantId));
    }
}
