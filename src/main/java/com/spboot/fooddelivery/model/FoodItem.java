package com.spboot.fooddelivery.model;

import com.spboot.fooddelivery.enums.Cuisine;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="FOOD_ITEM")
public class FoodItem {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "IS_VEG")
	private boolean isVeg;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "CUISINE")
	private Cuisine cuisine;
	
//	@OneToOne(mappedBy = "foodItem")
//	private OrderItem orderItem;
//	
//	@OneToOne(mappedBy = "foodItem")
//	private RestaurantItem restaurantItem;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getIsVeg() {
		return isVeg;
	}

	public void setIsVeg(boolean isVeg) {
		this.isVeg = isVeg;
	}

	public Cuisine getCuisine() {
		return cuisine;
	}

	public void setCuisine(Cuisine cuisine) {
		this.cuisine = cuisine;
	}

//	public OrderItem getOrderItem() {
//		return orderItem;
//	}
//
//	public void setOrderItem(OrderItem orderItem) {
//		this.orderItem = orderItem;
//	}
//
//	public RestaurantItem getRestaurantItem() {
//		return restaurantItem;
//	}
//
//	public void setRestaurantItem(RestaurantItem restaurantItem) {
//		this.restaurantItem = restaurantItem;
//	}
	
}
