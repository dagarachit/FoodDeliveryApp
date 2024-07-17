package com.spboot.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spboot.fooddelivery.model.entity.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	
	Role findByName(String name);

}
