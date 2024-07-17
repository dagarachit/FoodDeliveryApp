package com.spboot.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spboot.fooddelivery.model.FoodItem;

public interface FoodItemRepository extends JpaRepository<FoodItem, Integer>{

}
