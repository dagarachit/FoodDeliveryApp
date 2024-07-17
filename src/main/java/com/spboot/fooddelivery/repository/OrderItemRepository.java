package com.spboot.fooddelivery.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spboot.fooddelivery.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{

	@Query(value = "select item from OrderItem item " +
            "join item.order ord join ord.user u where u.id = :userId")
    List<OrderItem> findPersonalizedItemsForUser(@Param("userId") int userId, Pageable pageable);
}
