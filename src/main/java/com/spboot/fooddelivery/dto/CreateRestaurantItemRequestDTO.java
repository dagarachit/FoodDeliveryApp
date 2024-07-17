package com.spboot.fooddelivery.dto;

import com.spboot.fooddelivery.enums.Cuisine;

public class CreateRestaurantItemRequestDTO {

	private int restaurantId;
	private int rating;
	private float price;
	private boolean isBestSeller;
	// FoodItem
	private String name;
	private String description;
	private boolean isVeg;
	private Cuisine cuisine;
	
	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean getIsBestSeller() {
		return isBestSeller;
	}

	public void setIsBestSeller(boolean isBestSeller) {
		this.isBestSeller = isBestSeller;
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

}
