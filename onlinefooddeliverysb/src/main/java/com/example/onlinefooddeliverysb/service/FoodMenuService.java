package com.example.onlinefooddeliverysb.service;

import com.example.onlinefooddeliverysb.model.FoodMenu;
import com.example.onlinefooddeliverysb.repository.FoodMenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodMenuService {

    @Autowired
    private FoodMenuRepository foodMenuRepository;

    public FoodMenu saveFoodMenu(FoodMenu foodMenu) {
        return foodMenuRepository.save(foodMenu);
    }

    public List<FoodMenu> getAllFoodMenus() {
        return foodMenuRepository.findAll();
    }

    public FoodMenu getFoodMenuById(Long id) {
        return foodMenuRepository.findById(id).orElse(null);
    }

    public List<FoodMenu> getFoodMenusByFoodTypeId(Long foodTypeId) {
        return foodMenuRepository.findByFoodTypeId(foodTypeId);
    }

    public void deleteFoodMenu(Long id) {
        foodMenuRepository.deleteById(id);
    }
}
