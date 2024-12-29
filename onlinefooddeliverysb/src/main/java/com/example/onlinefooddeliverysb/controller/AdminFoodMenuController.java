package com.example.onlinefooddeliverysb.controller;

import com.example.onlinefooddeliverysb.model.FoodMenu;
import com.example.onlinefooddeliverysb.model.FoodType;
import com.example.onlinefooddeliverysb.model.User;
import com.example.onlinefooddeliverysb.service.FoodMenuService;
import com.example.onlinefooddeliverysb.service.FoodTypeService;
import com.example.onlinefooddeliverysb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food-menus")
public class AdminFoodMenuController {

    @Autowired
    private FoodMenuService foodMenuService;

    @Autowired
    private FoodTypeService foodTypeService;

    @Autowired
    private UserService userService;

    @PostMapping("/{foodTypeId}")
    public ResponseEntity<FoodMenu> createFoodMenu(
            @RequestBody FoodMenu foodMenu,
            @PathVariable Long foodTypeId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        if (user == null) {
            throw new RuntimeException("User not found or invalid token");
        }

        FoodType foodType = foodTypeService.getFoodTypeById(foodTypeId)
                .orElseThrow(() -> new RuntimeException("FoodType not found"));

        foodMenu.setFoodType(foodType);

        return ResponseEntity.ok(foodMenuService.saveFoodMenu(foodMenu));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodMenu(@PathVariable Long id) {
        foodMenuService.deleteFoodMenu(id);
        return ResponseEntity.noContent().build();
    }
}
