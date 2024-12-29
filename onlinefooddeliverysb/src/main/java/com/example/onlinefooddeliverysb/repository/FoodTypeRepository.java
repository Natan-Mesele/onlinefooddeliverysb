package com.example.onlinefooddeliverysb.repository;

import com.example.onlinefooddeliverysb.model.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTypeRepository extends JpaRepository<FoodType, Long> {
    List<FoodType> findByRestaurantId(Long restaurantId); // Fetch food types for a specific restaurant
}
