package com.spboot.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spboot.fooddelivery.model.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

	void deleteByCartId(int id);

}
