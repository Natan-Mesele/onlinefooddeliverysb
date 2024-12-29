package com.example.onlinefooddeliverysb.controller;

import com.example.onlinefooddeliverysb.model.FoodMenu;
import com.example.onlinefooddeliverysb.service.FoodMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/food-menus")
public class FoodMenuController {

    @Autowired
    private FoodMenuService foodMenuService;

    @GetMapping("/food-type/{foodTypeId}")
    public ResponseEntity<List<FoodMenu>> getFoodMenusByFoodType(
            @PathVariable Long foodTypeId
    ) {
        return ResponseEntity.ok(foodMenuService.getFoodMenusByFoodTypeId(foodTypeId));
    }

    @GetMapping
    public ResponseEntity<List<FoodMenu>> getAllFoodMenus() {
        return ResponseEntity.ok(foodMenuService.getAllFoodMenus());
    }

    // Get food menu by ID
    @GetMapping("/{id}")
    public ResponseEntity<FoodMenu> getFoodMenuById(@PathVariable Long id) {
        return foodMenuService.getFoodMenuById(id) != null
                ? ResponseEntity.ok(foodMenuService.getFoodMenuById(id))
                : ResponseEntity.notFound().build();
    }
}
