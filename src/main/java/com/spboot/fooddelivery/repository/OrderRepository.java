package com.spboot.fooddelivery.repository;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spboot.fooddelivery.model.Order;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Integer>{

	List<Order> findAllByUserIdOrderByIdDesc(int consumerId,Pageable pageable);
}
