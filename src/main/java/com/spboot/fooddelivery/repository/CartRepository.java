package com.spboot.fooddelivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spboot.fooddelivery.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	void deleteByRestaurantId(int id);

	List<Cart> findAllByRestaurantId(int id);

}
