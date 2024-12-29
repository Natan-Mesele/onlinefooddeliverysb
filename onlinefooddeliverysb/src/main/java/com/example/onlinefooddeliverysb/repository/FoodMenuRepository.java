package com.example.onlinefooddeliverysb.repository;

import com.example.onlinefooddeliverysb.model.FoodMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodMenuRepository extends JpaRepository<FoodMenu, Long> {
    List<FoodMenu> findByFoodTypeId(Long foodTypeId);
}
