package com.example.onlinefooddeliverysb.service;

import com.example.onlinefooddeliverysb.model.FoodType;
import com.example.onlinefooddeliverysb.repository.FoodTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodTypeService {

    @Autowired
    private FoodTypeRepository foodTypeRepository;

    public FoodType saveFoodType(FoodType foodType) {
        return foodTypeRepository.save(foodType);
    }

    public Optional<FoodType> getFoodTypeById(Long id) {
        return foodTypeRepository.findById(id);
    }

    public List<FoodType> getFoodTypesByRestaurantId(Long restaurantId) {
        return foodTypeRepository.findByRestaurantId(restaurantId);
    }

    public void deleteFoodType(Long id) {
        foodTypeRepository.deleteById(id);
    }
}
