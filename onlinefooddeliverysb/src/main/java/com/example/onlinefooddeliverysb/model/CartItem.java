package com.example.onlinefooddeliverysb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "food_menu_id", nullable = false)
    private FoodMenu foodMenu;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference
    private Cart cart;

    private int quantity;

    private double totalPrice;

    public void calculateTotalPrice() {
        this.totalPrice = foodMenu.getPrice() * quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FoodMenu getFoodMenu() {
        return foodMenu;
    }

    public void setFoodMenu(FoodMenu foodMenu) {
        this.foodMenu = foodMenu;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CartItem(Long id, FoodMenu foodMenu, Cart cart, int quantity, double totalPrice) {
        this.id = id;
        this.foodMenu = foodMenu;
        this.cart = cart;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public CartItem() {
    }
}
