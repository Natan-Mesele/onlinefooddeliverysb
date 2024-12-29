package com.example.onlinefooddeliverysb.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class FoodMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private double price;

    @ElementCollection
    private List<String> ingredients;

    @ManyToOne
    @JoinColumn(name = "food_type_id", nullable = false)
    private FoodType foodType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public FoodMenu(String title, String description, double price, List<String> ingredients, FoodType foodType) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.ingredients = ingredients;
        this.foodType = foodType;
    }

    public FoodMenu() {
    }
}
