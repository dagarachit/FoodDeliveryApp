package com.spboot.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spboot.fooddelivery.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{

	void deleteByRestaurantId(int id);

}
